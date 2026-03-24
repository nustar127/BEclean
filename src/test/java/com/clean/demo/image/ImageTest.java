package com.clean.demo.image;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clean.demo.entity.Image;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class ImageTest {
    @Autowired
    private MockMvc mockMvc;

    //@Autowired
    private ObjectMapper objectMapper  = new ObjectMapper();

    @Test
    //@WithMockUser(roles = "ADMIN")
    void createService() throws Exception {
        Image image = new Image();
        image.setAlt("1");
        image.setFilename("url");

        mockMvc.perform(post("uploads/image")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(image)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("url"));
    } 

    @Test
    void deleteImage() throws Exception {
        mockMvc.perform(delete("/uploads/images/" + 6)).andExpect(status().isOk());
    }
}
