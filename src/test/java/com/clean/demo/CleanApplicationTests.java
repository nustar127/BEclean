package com.clean.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CleanApplicationTests {
    
    @Test
    void testFindById() {
        // Создаем обычный клиент
        RestTemplate restTemplate = new RestTemplate();

        // Стучимся по реальному адресу
        String url = "http://localhost:8080/99";
        String response = restTemplate.getForObject(url, String.class);

        // Проверяем, что в ответе есть наш ID
        assertThat(response).contains("99");
        System.out.println("Ответ от сервера: " + response);
    }
}
