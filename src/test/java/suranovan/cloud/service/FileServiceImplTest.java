package suranovan.cloud.service;

import org.junit.jupiter.api.*;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class
FileServiceImplTest {

    private final static String uploadPath = System.getProperty("user.home") + File.separator;

    FileServiceImpl fileService = new FileServiceImpl(uploadPath);

    @BeforeEach
    public void setUpEach() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn("testUser");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        var testDir = new File(uploadPath + user);
        testDir.mkdir();
    }

    @AfterAll
    public static void clear() {
        String userName = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        //очистка папки от тестовых файлов
        File testDir = new File(uploadPath + userName);
        Arrays.stream(testDir.listFiles()).forEach(file -> file.delete());
        testDir.delete();
    }

    @Test
    void listUsersFiles() {
        int limit = 2;

        //Соблюдается ли ограничение по количеству выводимых файлов
        Assertions.assertEquals(limit, fileService.listUsersFiles(limit).size());
    }

    @Test
    void addFileToCloud() throws IOException {
        String fileName = "123.txt";
        Path path = Paths.get(uploadPath + fileName);
        File testFile = new File(String.valueOf(path));
        if (!testFile.exists()) {
            testFile.createNewFile();
        }

        byte[] emptyData = new byte[0];

        FileInputStream fis = new FileInputStream(String.valueOf(path));
        MediaType mime = MediaType.parseMediaType(Files.probeContentType(path));

        MultipartFile multipartFileEmpty = new MockMultipartFile(fileName, emptyData);
        MultipartFile multipartFile = new MockMultipartFile(
                "fileName",
                fileName,
                mime.toString(),
                IOUtils.toByteArray(fis));

        //Если файл пуст
        Assertions.assertFalse(fileService.addFileToCloud(fileName, multipartFileEmpty));
        //Прошла ли загрузка файла
        Assertions.assertTrue(fileService.addFileToCloud(fileName, multipartFile));
    }

    @Test
    void getFileFromCloud() throws IOException {
        File file = new File(fileService.getUsersDir() + "/" + "test.txt");
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.write("test");
        fw.close();

        var isFromMethod = fileService.getFileFromCloud(file.getName());
        var is = isFromMethod.getBody().getInputStream();

        //не пустой ли inputstream  при чтении файла
        Assertions.assertNotEquals(is.readAllBytes().length, 0);
        is.close();
    }

    @Test
    void deleteFile() throws IOException {
        File testFile = new File(uploadPath + "testUser/124.png");
        testFile.createNewFile();
        fileService.deleteFile("124.png");

        //существует ли файл после удаления
        Assertions.assertFalse(testFile.exists());
    }

    @Test
    void renameFile() throws IOException {
        String newFileName = "new123.txt";
        File testFile = new File(fileService.getUsersDir() + "/" + "name.txt");
        testFile.createNewFile();
        String newFileNameFromRequest = "{ \"filename\": \"" + newFileName + "\"}";

        //поскольку на это момент всего 1 файл проверка его имени
        fileService.renameFile(testFile.getName(), newFileNameFromRequest);
        Assertions.assertEquals(newFileName, fileService.getUsersDir().listFiles()[0].getName());
    }
}