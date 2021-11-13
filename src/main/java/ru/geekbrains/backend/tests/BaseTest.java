package ru.geekbrains.backend.tests;

//import javafx.application.Application;
//import javafx.stage.Stage;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.io.FileUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeAll;

import java.util.Base64;
import java.util.Properties;

public abstract class BaseTest {

    public static String PATH_TO_IMAGE = "src/main/resources/test_image.jpg";
    public static String PATH_TO_IMAGE_TWO = "src/main/resources/test _image2.gif";

    static ResponseSpecification positiveResponseSpecification;
    static RequestSpecification requestSpecificationWithAuth;

    static Properties properties = new Properties();

    static String token;
    static String basicToken;
    static String username;
    static String imageHash;
    static MultiPartSpecification base64MultiPartSpec;
    static MultiPartSpecification multiPartSpecWithFile;


    static RequestSpecification requestSpecificationWithAuthAndMultuPartImage;
    static RequestSpecification requestSpecificationWithAuthWithBase64;
    @BeforeAll
    static void beforeAll() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        getProperties();
        token = properties.getProperty("token");
        basicToken = properties.getProperty("basicToken");
        username = properties.getProperty("username");
        imageHash = properties.getProperty("imageHash");

        positiveResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", IsEqual.equalTo(200))
                .expectBody("success", CoreMatchers.is(true))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        requestSpecificationWithAuth = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .build();
        multiPartSpecWithFile  = new MultiPartSpecBuilder(new File("src/main/resources/test_image.jpg"))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultuPartImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("title","Picture")
                .addFormParam("type","gif")
                .addMultiPart(multiPartSpecWithFile)
                .build();
        byte[] Array = getFileContent();
        String encodedFile = Base64.getEncoder().encodeToString(Array);
        base64MultiPartSpec = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();
        requestSpecificationWithAuthWithBase64 = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(base64MultiPartSpec)
                .build();


    }
    private static byte[] getFileContent() {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
    private static void getProperties() {
        try (InputStream output = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

