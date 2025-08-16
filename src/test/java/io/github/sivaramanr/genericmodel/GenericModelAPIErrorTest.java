package io.github.sivaramanr.genericmodel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class GenericModelAPIErrorTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void testCreateWithoutRequiredHeadersAndGetBadRequestResponse() {
        webClient.post()
                .uri("/genericmodel")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                {
                    "genericType": "PRODUCT_CATEGORY",
                    "name": "Electronics",
                    "valueType": "STRING",
                    "sortOrder": 10,
                    "value": "Electronics",
                    "status": "ACTIVE",
                    "description": "Product category for electronics"
                }
                """)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.error")
                .isEqualTo("Missing Request Header");

        webClient.post()
                .uri("/genericmodel")
                .header("tenantId", "1000")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                {
                    "genericType": "PRODUCT_CATEGORY",
                    "name": "Electronics",
                    "valueType": "STRING",
                    "sortOrder": 10,
                    "value": "Electronics",
                    "status": "ACTIVE",
                    "description": "Product category for electronics"
                }
                """)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.error")
                .isEqualTo("Missing Request Header");
    }

    @Test
    void testCreateWithoutRequiredAttributeInPayLoadAndGetBadRequestResponse() {
        webClient.post()
            .uri("/genericmodel")
            .header("tenantId", "1000")
            .header("username", "testuser1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""
            {
                "name": "Electronics",
                "valueType": "STRING",
                "sortOrder": 10,
                "value": "Electronics",
                "status": "ACTIVE",
                "description": "Product category for electronics"
            }
            """)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody()
            .jsonPath("$.error")
            .isEqualTo("Validation Failed");

        webClient.post()
            .uri("/genericmodel")
            .header("tenantId", "1000")
            .header("username", "testuser1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""
            {
                "genericType": "PRODUCT_CATEGORY",
                "valueType": "STRING",
                "sortOrder": 10,
                "value": "Electronics",
                "status": "ACTIVE",
                "description": "Product category for electronics"
            }
            """)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody()
            .jsonPath("$.error")
            .isEqualTo("Validation Failed");
    }

}
