package ru.geekbrains.backend.tests;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import static io.restassured.RestAssured.given;
import org.hamcrest.CoreMatchers;
import ru.geekbrains.javaback.Endpoints;
import ru.geekbrains.javaback.dto.ImageResponse;

public class ImageUploadTests extends BaseTest {

    static String encodedFile;
    String uploadedImageId, uploadedDeleteImageId;

    @BeforeEach
    void beforeTest() {

    }

    @Test
    void uploadFileTest() {
        uploadedImageId =given(requestSpecificationWithAuthWithBase64,positiveResponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
        System.out.println(uploadedImageId);
    }

    @Test
    void setUploadFileImageTest() {
        uploadedImageId = given(requestSpecificationWithAuthAndMultuPartImage)
                 .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(ImageResponse.class)
                .getData().getDeletehash();

    }

    @Test
    void setUploadFileImageTestTwo() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE_TWO))
                .expect()
                .statusCode(200)
                .body("data.type",CoreMatchers.is("image/gif"))
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
    void uploadWithMultiPart(){
        uploadedImageId= given(requestSpecificationWithAuthAndMultuPartImage)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @Test
    void setFavoriteAnImageTest() {
        JsonPath json=  given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE_TWO))
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath();
        String uploadedId= json.getString("data.id");
        uploadedImageId= json.getString("data.deletehash");

       given()
                .headers("Authorization", token)
                //.multiPart("imageHash", uploadedId)
                .expect()
                .body("success", CoreMatchers.is(true))
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}/favorite", uploadedId)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath();

    }

      @AfterEach
    void tearDown() {
        if(uploadedImageId!=null)
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", username, uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    private byte[] getFileContent() {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}
