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
class GenericModelAPITest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void testCreateAndGetGenericModel() {
        // --- Create ---
        String location = webClient.post()
                .uri("/genericmodel")
                .header("tenantId", "1000")
                .header("username", "testuser1")
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
                .expectStatus().isCreated()
                .expectHeader().valueMatches("Location", ".*/genericmodel/.*")
                .returnResult(Void.class)
                .getResponseHeaders()
                .getLocation()
                .toString();

        webClient.post()
                .uri(location+"/name")
                .header("tenantId", "1000")
                .header("username", "testuser1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                {
                    "locale": "de-DE",
                    "text": "Produkte"
                }
                """)
                .exchange()
                .expectStatus().isCreated();

        webClient.put()
                .uri(location+"/name/de-DE")
                .header("tenantId", "1000")
                .header("username", "testuser1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                {
                    "text": "Elektronik"
                }
                """)
                .exchange()
                .expectStatus().isNoContent();

        // --- Get ---
        webClient.get()
                .uri(location)
                .header("tenantId", "1000")
                .header("Accept-Language", "de-DE")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.genericType").isEqualTo("PRODUCT_CATEGORY")
                .jsonPath("$.name").isEqualTo("Elektronik")
                .jsonPath("$.value").isEqualTo("Electronics")
                .jsonPath("$.status").isEqualTo("ACTIVE")
                .jsonPath("$.sortOrder").isEqualTo(10)
                .jsonPath("$.createdBy").isEqualTo("testuser1")
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.createdAt").isNotEmpty()
                .jsonPath("$.updatedAt").isNotEmpty();
    }

    @Test
    void testCreateAndGetAllGenericModel() {
        // --- Create Electronics ---
        webClient.post()
                .uri("/genericmodel")
                .header("tenantId", "1000")
                .header("username", "testuser1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
        {
            "genericType": "CATEGORY",
            "name": "Electronics",
            "valueType": "STRING",
            "sortOrder": 10,
            "value": "Electronics",
            "status": "ACTIVE",
            "description": "Product category for electronics"
        }
        """)
                .exchange()
                .expectStatus().isCreated();

        // --- Create Furniture ---
        webClient.post()
                .uri("/genericmodel")
                .header("tenantId", "1000")
                .header("username", "testuser1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
        {
            "genericType": "CATEGORY",
            "name": "Furniture",
            "valueType": "STRING",
            "sortOrder": 10,
            "value": "Furniture",
            "status": "ACTIVE",
            "description": "Product category for electronics"
        }
        """)
                .exchange()
                .expectStatus().isCreated();

        // --- Get all PRODUCT_CATEGORY ---
        webClient.get()
                .uri("/genericmodel?genericType=CATEGORY")
                .header("tenantId", "1000")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                // Validate array size
                .jsonPath("$.content.length()").isEqualTo(2)
                // Validate first element
                .jsonPath("$.content[0].genericType").isEqualTo("CATEGORY")
                .jsonPath("$.content[0].name").isEqualTo("Electronics")
                .jsonPath("$.content[0].value").isEqualTo("Electronics")
                .jsonPath("$.content[0].status").isEqualTo("ACTIVE")
                .jsonPath("$.content[0].createdBy").isEqualTo("testuser1")
                .jsonPath("$.content[0].id").isNotEmpty()
                .jsonPath("$.content[0].createdAt").isNotEmpty()
                // Validate second element
                .jsonPath("$.content[1].name").isEqualTo("Furniture")
                .jsonPath("$.content[1].value").isEqualTo("Furniture")
                // Validate page metadata
                .jsonPath("$.page.size").isEqualTo(10)
                .jsonPath("$.page.number").isEqualTo(0)
                .jsonPath("$.page.totalElements").isEqualTo(2)
                .jsonPath("$.page.totalPages").isEqualTo(1);
    }

    @Test
    void testCreateAndUpdateGenericModel() {
        // --- Create ---
        String location = webClient.post()
                .uri("/genericmodel")
                .header("tenantId", "1000")
                .header("username", "testuser1")
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
                .expectStatus().isCreated()
                .expectHeader().valueMatches("Location", ".*/genericmodel/.*")
                .returnResult(Void.class)
                .getResponseHeaders()
                .getLocation()
                .toString();

        // --- Get ---
        webClient.put()
                .uri(location)
                .header("tenantId", "1000")
                .header("username", "testuser1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                {
                    "genericType": "PRODUCT_CATEGORY",
                    "name": "Electronics",
                    "valueType": "STRING",
                    "sortOrder": 10,
                    "value": "ELECT",
                    "status": "ACTIVE",
                    "description": "Product category for electronics"
                }
                """)
                .exchange()
                .expectStatus().isNoContent();

        webClient.get()
                .uri(location)
                .header("tenantId", "1000")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.genericType").isEqualTo("PRODUCT_CATEGORY")
                .jsonPath("$.name").isEqualTo("Electronics")
                .jsonPath("$.value").isEqualTo("ELECT")
                .jsonPath("$.status").isEqualTo("ACTIVE")
                .jsonPath("$.sortOrder").isEqualTo(10)
                .jsonPath("$.createdBy").isEqualTo("testuser1")
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.createdAt").isNotEmpty()
                .jsonPath("$.updatedAt").isNotEmpty();
    }

    @Test
    void testCreateAndDeleteGenericModel() {
        // --- Create ---
        String location = webClient.post()
                .uri("/genericmodel")
                .header("tenantId", "1000")
                .header("username", "testuser1")
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
                .expectStatus().isCreated()
                .expectHeader().valueMatches("Location", ".*/genericmodel/.*")
                .returnResult(Void.class)
                .getResponseHeaders()
                .getLocation()
                .toString();

        // --- Get ---
        webClient.delete()
                .uri(location)
                .header("tenantId", "1000")
                .header("username", "testuser1")
                .exchange()
                .expectStatus().isNoContent();

        webClient.get()
                .uri(location)
                .header("tenantId", "1000")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateAndGetGenericModelTranslations() {
        // --- Create ---
        String location = webClient.post()
                .uri("/genericmodel")
                .header("tenantId", "1000")
                .header("username", "testuser1")
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
                .expectStatus().isCreated()
                .expectHeader().valueMatches("Location", ".*/genericmodel/.*")
                .returnResult(Void.class)
                .getResponseHeaders()
                .getLocation()
                .toString();

        webClient.post()
                .uri(location+"/name")
                .header("tenantId", "1000")
                .header("username", "testuser1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                {
                    "locale": "de-DE",
                    "text": "Elektronik"
                }
                """)
                .exchange()
                .expectStatus().isCreated();

        webClient.post()
                .uri(location+"/name")
                .header("tenantId", "1000")
                .header("username", "testuser1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                {
                    "locale": "fr-FR",
                    "text": "Électronique"
                }
                """)
                .exchange()
                .expectStatus().isCreated();

        webClient.post()
                .uri(location+"/name")
                .header("tenantId", "1000")
                .header("username", "testuser1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                {
                    "locale": "en-US",
                    "text": "Electronics"
                }
                """)
                .exchange()
                .expectStatus().isCreated();

        webClient.delete()
                .uri(location+"/name/en-US")
                .header("tenantId", "1000")
                .exchange()
                .expectStatus().isNoContent();

        // --- Get ---
        webClient.get()
                .uri(location+"/name")
                .header("tenantId", "1000")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                // Verify that the response contains translations for de-DE and fr-FR
                .jsonPath("$.content.length()").isEqualTo(2)
                .jsonPath("$.content[0].locale").isEqualTo("de-DE")
                .jsonPath("$.content[0].text").isEqualTo("Elektronik")
                .jsonPath("$.content[1].locale").isEqualTo("fr-FR")
                .jsonPath("$.content[1].text").isEqualTo("Électronique")
                .jsonPath("$.content[0].createdAt").isNotEmpty()
                .jsonPath("$.content[0].updatedAt").isNotEmpty()
                .jsonPath("$.content[1].createdAt").isNotEmpty()
                .jsonPath("$.content[1].updatedAt").isNotEmpty()
                .jsonPath("$.page.size").isEqualTo(10)
                .jsonPath("$.page.number").isEqualTo(0)
                .jsonPath("$.page.totalElements").isEqualTo(2)
                .jsonPath("$.page.totalPages").isEqualTo(1);
    }

}
