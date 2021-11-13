package ru.geekbrains.backend.tests;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.javaback.Endpoints;
import ru.geekbrains.javaback.dto.ImageResponse;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static ru.geekbrains.backend.tests.ImageUploadTests.requestSpecificationWithAuthWithBase64;

public class FavoriteAnImageTest extends BaseTest{

    private final String PATH_TO_IMAGE = "src/main/resources/test_image.jpg";
    private final String PATH_TO_IMAGE_TWO = "src/main/resources/test _image2.gif";
    static String encodedFile;
    String uploadedImageId, uploadedDeleteImageId;
    MultiPartSpecification base64MultiPartSpec;
    MultiPartSpecification multiPartSpecWithFile;
    RequestSpecification requestSpecificationWithAuthAndMultuPartImage;

    @BeforeEach
    void setUp(){
        byte[] Array = getFileContent(PATH_TO_IMAGE_TWO);
        encodedFile = Base64.getEncoder().encodeToString(Array);
        base64MultiPartSpec = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();
        multiPartSpecWithFile  = new MultiPartSpecBuilder(new File( PATH_TO_IMAGE_TWO))
                .controlName("image")
                .build();
         requestSpecificationWithAuthAndMultuPartImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("title", "Picture")
                .addFormParam("type", "gif")
                .addMultiPart(multiPartSpecWithFile)
                .build();
        requestSpecificationWithAuthWithBase64 = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(base64MultiPartSpec)
                .build();
        uploadedImageId =given(requestSpecificationWithAuthWithBase64,positiveResponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .body()
                .as(ImageResponse.class)
                .getData().getDeletehash();
    }

    @Test
    void setFavoriteAnImageTest() {
        given()
                .headers("Authorization", token)
                //.multiPart("imageHash", uploadedId)
                .expect()
                .body("success", CoreMatchers.is(true))
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}/favorite", uploadedImageId)
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

    private byte[] getFileContent(String path) {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(path));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}


