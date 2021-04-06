package suranovan.cloud.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import suranovan.cloud.model.response.CloudFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileServiceAdminImpl {

    @Value("${cloud.upload.path}")
    private final String uploadPath;

    public FileServiceAdminImpl(@Value("cloud.upload.path") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public List<CloudFile> listUsersFiles(int limit) {
        List<CloudFile> userFileList = new ArrayList<>();
        File path = new File(uploadPath);
        Stream.of(path.listFiles()).limit(limit)
                .forEach(dir -> Stream.of(dir.listFiles())
                        .forEach(file -> userFileList.add(
                                new CloudFile((dir.getName() + "-" + file.getName()), (int) file.length()))));
        return userFileList;
    }
}
