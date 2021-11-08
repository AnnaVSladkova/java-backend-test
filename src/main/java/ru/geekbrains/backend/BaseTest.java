package ru.geekbrains.backend;

//import javafx.application.Application;
//import javafx.stage.Stage;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeAll;
import java.util.Properties;

public abstract class BaseTest {

    static Properties properties = new Properties();

    static String token;
    static String basicToken;
    static String username;
    static String imageHash;

    @BeforeAll
    static void beforeAll() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        getProperties();
        token = properties.getProperty("token");
        basicToken = properties.getProperty("basicToken");
        username = properties.getProperty("username");
        imageHash= properties.getProperty("imageHash");
    }

    private static void getProperties() {
        try (InputStream output = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


//public class BaseTest extends Application {
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) {
//
//    }
//}
