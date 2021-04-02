package suranovan.cloud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import suranovan.cloud.config.jwt.JwtUtils;
import suranovan.cloud.model.Request.LoginAndPassword;
import suranovan.cloud.model.response.CloudFile;
import suranovan.cloud.service.FileServiceImpl;

import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/cloud")
@PreAuthorize("isAuthenticated()")
public class Controller {

    final AuthenticationManager authenticationManager;
    final JwtUtils jwtUtils;
    final FileServiceImpl fileService;

    public Controller(AuthenticationManager authenticationManager, JwtUtils jwtUtils, FileServiceImpl fileService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.fileService = fileService;
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginAndPassword loginRequest) throws JsonProcessingException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(jwt);
    }


    @PostMapping("/logout")
    public void logout() {

    }

    @GetMapping(value = "/list", produces = "application/json")
    public List<CloudFile> list(@RequestParam(value = "limit", defaultValue = "5") Integer limit) {
        return fileService.listUsersFiles(limit);
    }

    @PostMapping(value = "/file", consumes = "multipart/form-data")
    public void file(@RequestParam("filename") String fileName,
                     @RequestParam("file") MultipartFile file) throws IOException {
        fileService.addFileToCloud(fileName, file);
    }

    @GetMapping(value = "/file")
    public ResponseEntity<InputStreamResource> getFile(@RequestParam("filename") String fileName) throws IOException {
        return fileService.getFileFromCloud(fileName);
    }


    @PutMapping("/file")
    public void putFile(@RequestParam("filename") String fileName,
                        @RequestBody String newFileName) throws IOException {
        fileService.renameFile(fileName, newFileName);
    }

    @DeleteMapping("/file")
    public void deleteFile(@RequestParam("filename") String fileName) {
        fileService.deleteFile(fileName);
    }
}
