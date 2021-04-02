package suranovan.cloud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import suranovan.cloud.model.Request.LoginAndPassword;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithUserDetails("user1")
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    LoginAndPassword loginAndPassword = new LoginAndPassword("user1", "123");
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void authenticateUser() throws Exception {
        mockMvc.perform(post("/cloud/login")
                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"login\": \"user1\", " +
//                        "\"password\": \"123\"}"))
                .content(objectMapper.writeValueAsString(loginAndPassword)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void logout() throws Exception {
        mockMvc.perform(post("/cloud/logout"))
                .andExpect(status().isOk());
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/cloud/list"))
                //.queryParam("limit", "3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void fileNullValueAndReturn400() throws Exception {
        mockMvc.perform(post("/cloud/file")
                    .queryParam("filename", "newFileName")
                    .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void deleteFile() throws Exception {
        mockMvc.perform(delete("/cloud/file")
        .queryParam("filename", "someName"))
                .andExpect(status().is4xxClientError());
    }
}