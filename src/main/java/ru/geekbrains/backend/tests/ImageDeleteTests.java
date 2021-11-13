package ru.geekbrains.backend.tests;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.javaback.Endpoints;
import ru.geekbrains.javaback.dto.ImageResponse;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;


public class ImageDeleteTests extends BaseTest{
    private final String PATH_TO_IMAGE="src/main/resources/test_image.jpg";
    //static String encodedFile;
    String uploadedImageId;
    //MultiPartSpecification base64MultiPartSpec;
    //MultiPartSpecification multiPartSpecWithFile;
    //RequestSpecification requestSpecificationWithAuthWithBase64;

    //@BeforeEach
//    void setUp(){
//        byte[] Array = getFileContent(PATH_TO_IMAGE);
//        encodedFile = Base64.getEncoder().encodeToString(Array);
//        base64MultiPartSpec = new MultiPartSpecBuilder(encodedFile)
//                .controlName("image")
//                .build();
//        multiPartSpecWithFile  = new MultiPartSpecBuilder(new File("src/main/resources/test_image.jpg"))
//                .controlName("image")
//                .build();
//        requestSpecificationWithAuthWithBase64 = new RequestSpecBuilder()
//                .addHeader("Authorization", token)
//                .addMultiPart(base64MultiPartSpec)
//                .build();
//
//        uploadedImageId =given(requestSpecificationWithAuthWithBase64,positiveResponseSpecification)
//                .post(Endpoints.UPLOAD_IMAGE)
//                .prettyPeek()
//                .then()
//                .extract()
//                .response()
//                .body()
//                .as(ImageResponse.class)
//                .getData().getDeletehash();
//    }

    @BeforeEach
    void setUp(){
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
    void deleteTest() {//requestWithAuth
        given(requestSpecificationWithAuthWithBase64,positiveResponseSpecification)
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", username, uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

   }

