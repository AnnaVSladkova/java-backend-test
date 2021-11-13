package ru.geekbrains.backend.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import java.io.File;
import java.util.UUID;

import org.hamcrest.CoreMatchers;


public class NegativeTests extends BaseTest {
    private final String PATH_TO_IMAGE = "src/main/resources/test_image.jpg";
    private final String PATH_TO_IMAGE_TWO = "src/main/resources/test _image2.gif";
    static String encodedFile;
    String uploadedImageId, uploadedDeleteImageId;

    @Test
    void setUploadFileWithuotImageTest() {
        int code = given()
              .headers("Authorization", token)
                .expect()
                .statusCode(400)
                .body("success", CoreMatchers.is(false))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response().getStatusCode();
    }
    @Test
    void uploadFileWithoutAuthorizationTest() {
        uploadedImageId = given()
                .multiPart("image", new File(PATH_TO_IMAGE))
                .expect()
                .statusCode(400)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @Test
    void uploadFileAtherMethodTest() {
                given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .expect()
                .statusCode(405)
                .when()
                .get("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response();
    }

}
