package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@DisplayName("AuthResource Tests")
class AuthResourceTest {

    @Nested
    @DisplayName("Login Endpoint")
    class LoginEndpointTest {

        @Test
        @DisplayName("Should successfully login with valid credentials")
        void shouldLoginSuccessfullyWithValidCredentials() {
            // Arrange
            String requestBody = """
                {
                    "username":"user1",
                    "password":"password1"
                }
                """;

            // Act & Assert
            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/auth/login")
                    .then()
                    .statusCode(200)
                    .body("message", equalTo("Login successful"));
        }

        @ParameterizedTest
        @CsvSource({
                "user1,wrongpassword",
                "wronguser,password1",
                "wronguser,wrongpassword"
        })
        @DisplayName("Should return 401 for invalid credentials")
        void shouldReturnUnauthorizedForInvalidCredentials(String username, String password) {
            // Arrange
            String requestBody = String.format("""
                {
                    "username":"%s",
                    "password":"%s"
                }
                """, username, password);

            // Act & Assert
            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/auth/login")
                    .then()
                    .statusCode(401)
                    .body("message", equalTo("Invalid credentials"));
        }

        @Test
        @DisplayName("Should return 400 for missing password")
        void shouldReturnBadRequestForMissingPassword() {
            // Arrange
            String requestBody = """
                {
                    "username":"user1"
                }
                """;

            // Act & Assert
            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/auth/login")
                    .then()
                    .statusCode(400)
                    .body("message", equalTo("Username and password are required"));
        }

        @Test
        @DisplayName("Should return 400 for missing username")
        void shouldReturnBadRequestForMissingUsername() {
            // Arrange
            String requestBody = """
                {
                    "password":"password1"
                }
                """;

            // Act & Assert
            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/auth/login")
                    .then()
                    .statusCode(400)
                    .body("message", equalTo("Username and password are required"));
        }

        @Test
        @DisplayName("Should return 400 for empty request body")
        void shouldReturnBadRequestForEmptyRequestBody() {
            // Arrange
            String requestBody = "{}";

            // Act & Assert
            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/auth/login")
                    .then()
                    .statusCode(400)
                    .body("message", equalTo("Username and password are required"));
        }

        @Test
        @DisplayName("Should return 400 for null request body")
        void shouldReturnBadRequestForNullRequestBody() {
            // Act & Assert
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .post("/auth/login")
                    .then()
                    .statusCode(400)
                    .body("message", equalTo("Invalid request body"));
        }
    }
}