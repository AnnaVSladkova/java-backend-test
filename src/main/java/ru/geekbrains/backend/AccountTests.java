package ru.geekbrains.backend;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;

public class AccountTests extends BaseTest {

    @Test
    void getAccountInfoTest() {
        given()
                .headers("Authorization", token)
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .then()
                .statusCode(200);
    }

    @Test
    void getAccountInfoWithLoggingTest() {
        given()
                .headers("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void getAccountInfoWithAssertionsInGivenTest() {
        given()
                .headers("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .statusCode(200)
                .body("data.url", IsEqual.equalTo(username))
                .body("success", IsEqual.equalTo(true))
                .body("status", IsEqual.equalTo(200))
                .contentType("application/json")
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek();
    }

    @Test
    void getAccountInfoWithAssertionsAfterTest() {
        Response response = given()
                .headers("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek();
        MatcherAssert.assertThat(response.jsonPath().get("data.url"), IsEqual.equalTo(username));
    }
}
