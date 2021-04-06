package suranovan.cloud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
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
import suranovan.cloud.repository.role.IRoleRepository;
import suranovan.cloud.service.FileServiceAdminImpl;
import suranovan.cloud.service.FileServiceImpl;

import java.io.IOException;
import java.security.Principal;
import java.util.*;


@RestController
@RequestMapping("/cloud")
@PreAuthorize("isAuthenticated()")
public class Controller {

    final AuthenticationManager authenticationManager;
    final JwtUtils jwtUtils;
    final FileServiceImpl fileService;
    final FileServiceAdminImpl fileServiceAdmin;
    final IRoleRepository roleRepository;

    public Controller(AuthenticationManager authenticationManager, JwtUtils jwtUtils, FileServiceImpl fileService, FileServiceAdminImpl fileServiceAdmin, IRoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.fileService = fileService;
        this.fileServiceAdmin = fileServiceAdmin;
        this.roleRepository = roleRepository;
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginAndPassword loginRequest) throws JsonProcessingException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok("{ \"auth-token\": " + "\"" + jwt + "\" }");
    }


    @PostMapping("/logout")
    public void logout() {

    }

    private boolean getRole() {
        var usersRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return usersRole.contains(roleRepository.findByIdEquals(2));
    }

    @GetMapping(value = "/list", produces = "application/json")
    public List<CloudFile> list(@RequestParam(value = "limit", defaultValue = "5") Integer limit) {
        if (getRole()) {
            return fileServiceAdmin.listUsersFiles(10);
        } else {
            return fileService.listUsersFiles(limit);
        }
    }

    @PostMapping(value = "/file", consumes = "multipart/form-data")
    public void file(@RequestParam("filename") String fileName,
                     @RequestParam("file") MultipartFile file) throws IOException {
        if (getRole()) {
            fileServiceAdmin.addFileToCloud(fileName, file);
        } else {
            fileService.addFileToCloud(fileName, file);
        }
    }

    @GetMapping(value = "/file")
    public ResponseEntity<InputStreamResource> getFile(@RequestParam("filename") String fileName) throws IOException {
        if (getRole()) {
            return fileServiceAdmin.getFileFromCloud(fileName);
        } else {
            return fileService.getFileFromCloud(fileName);
        }
    }


    @PutMapping("/file")
    public void putFile(@RequestParam("filename") String fileName,
                        @RequestBody String newFileName) throws IOException {
        if (getRole()) {
            fileServiceAdmin.renameFile(fileName, newFileName);
        } else {
            fileService.renameFile(fileName, newFileName);
        }
    }

    @DeleteMapping("/file")
    public void deleteFile(@RequestParam("filename") String fileName) {
        if (getRole()) {
            fileServiceAdmin.deleteFile(fileName);
        } else {
            fileService.deleteFile(fileName);
        }
    }
}
