package ru.geekbrains.backend;

import io.restassured.path.json.JsonPath;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import static io.restassured.RestAssured.given;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsNull;

public class ImageTests extends BaseTest {

    private final String PATH_TO_IMAGE = "src/main/resources/test_image.jpg";
    private final String PATH_TO_IMAGE_TWO = "src/main/resources/test _image2.gif";
    static String encodedFile;
    String uploadedImageId, uploadedDeleteImageId;

    @BeforeEach
    void beforeTest() {
        byte[] Array = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(Array);
    }

    @Test
    void uploadFileTest() {
        uploadedImageId =given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .expect()
                .body("success", CoreMatchers.is(true))
                .body("data.id", CoreMatchers.is(IsNull.notNullValue()))
                .when()
                .post("https://api.imgur.org/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
        //System.out.println(uploadedImageId);
    }

    @Test
    void setUploadFileImageTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .expect()
                .statusCode(200)
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
    void setUploadFileImageTestTwo() {
        uploadedImageId = given()
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
        String uploadedId=     json.getString("data.id");
        uploadedImageId=     json.getString("data.deletehash");

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
