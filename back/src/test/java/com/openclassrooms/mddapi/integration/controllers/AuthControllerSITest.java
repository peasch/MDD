package com.openclassrooms.mddapi.integration.controllers;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(SpringExtension.class)
class AuthControllerSITest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void loginFailureTest() throws Exception {
        // crée un utilisateur admin
        String email = "yoga@studio.com";
        String password = "test!123";
        String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginTest() throws Exception {
        // crée un utilisateur admin
        String email = "bob@bob.com";
        String password = "bob";
        String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void registerTest() throws Exception {
        // crée un utilisateur admin
        String email = "john@john.fr";
        String password = "test!1234";
        String name = "Doe";
        String requestBody = "{" +
                "\"email\": \"" + email + "\"," +
                "\"password\": \"" + password + "\"," +
                "\"lastName\": \"" + name + "\"" +
                "}";


        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        String requestBody2 = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody2)).andReturn();

        String content = result.getResponse().getContentAsString();
        String token = JsonPath.read(content, "$.token");



        MvcResult resultMe = mockMvc.perform(get("/api/auth/me" ).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn();
        String content2 = resultMe.getResponse().getContentAsString();
        int userId = JsonPath.read(content2, "$.id");

        mockMvc.perform(delete("/api/user/" + userId).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
