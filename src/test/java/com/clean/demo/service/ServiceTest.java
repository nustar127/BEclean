package com.clean.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.clean.demo.entity.Image;
import com.clean.demo.entity.Service;

import com.clean.demo.repository.ImageRepository;

@SpringBootTest
@AutoConfigureMockMvc
class ServiceTest {

    @Autowired
    private MockMvc mockMvc;

    // @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ImageRepository imageRepository;

    @Test
    // @WithMockUser(roles = "ADMIN")
    void createService() throws Exception {
        Service service = new Service();

        mockMvc.perform(post("/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(service)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("2"));

    }

    @Test
    void updateService() throws Exception {

        Service service = new Service();
        service.setName("lololo");
        Iterable<Image> allImages = imageRepository.findAll();
        List<Image> images = new ArrayList<>();
        for (Image img : allImages) {
            images.add(img);
        }

        service.setImages(images);
        service.setFeaturedImage(images.get(0));

        mockMvc.perform(put("/services/" + 18)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(service)))
                .andDo(result -> {
                    System.out.println("RESPONSE: " + result.getResponse().getContentAsString());
                    if (result.getResolvedException() != null) {
                        result.getResolvedException().printStackTrace();
                    }
                })
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("lololo"));

    }

    @Test
    // @WithMockUser(roles = { "USER" })
    public void givenUserRole_whenAccessUserEndpoint_thenOk() throws Exception {
        mockMvc.perform(get("/services/" + 2))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteService() throws Exception {
        mockMvc.perform(delete("/services/" + 19)).andExpect(status().isOk());
    }

}