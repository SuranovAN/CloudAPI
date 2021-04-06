package suranovan.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import suranovan.cloud.model.response.CloudFile;
import suranovan.cloud.service.FileServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/cloud/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Value("${cloud.upload.path}")
    private String uploadPath;

    FileServiceImpl fileService = new FileServiceImpl(uploadPath) {
        @Override
        public File getUsersDir() {
            return new File(uploadPath);
        }
    };

    private String getDirectUserPath(String userName){
        return uploadPath + userName;
    }

    @GetMapping("/list/{userName}")
    public List<CloudFile> listFiles(@PathVariable String userName) {
        uploadPath = getDirectUserPath(userName);
        var list = fileService.listUsersFiles(3);
        uploadPath = "c:/temp/";
        return list;
    }

    @PostMapping(value = "/file/{userName}", consumes = "multipart/form-data")
    public void file(@RequestParam("filename") String fileName,
                     @RequestParam("file") MultipartFile file,
                     @PathVariable String userName) throws IOException {
        fileService.addFileToCloud(fileName, file);
    }

    @GetMapping(value = "/file/{userName}")
    public ResponseEntity<InputStreamResource> getFile(@RequestParam("filename") String fileName,
                                                       @PathVariable String userName) throws IOException {
        return fileService.getFileFromCloud(fileName);
    }


    @PutMapping("/file/{userName}")
    public void putFile(@RequestParam("filename") String fileName,
                        @RequestBody String newFileName,
                        @PathVariable String userName) throws IOException {
        fileService.renameFile(fileName, newFileName);
    }

    @DeleteMapping("/file/{userName}")
    public void deleteFile(@RequestParam("filename") String fileName,
                           @PathVariable String userName) {
        fileService.deleteFile(fileName);
    }
}
