import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateUserTest {
        /*
        Создание пользователя:
            - создать уникального пользователя;
            - создать пользователя, который уже зарегистрирован;
            - создать пользователя и не заполнить одно из обязательных полей.
        */

    String userName;
    String userPassword;
    String userMail;
    Response response;
    CreateUser user;

    @Before
    public void setUp() {
        Faker faker = new Faker();
        userName = faker.name().firstName(); // Emory
        userPassword = faker.name().lastName(); // Barton
        userMail = userName + "." + userPassword + "@ya.ru"; // Emory.Barton@ya.ru

    }

    @Test
    @Feature("Создание пользователя")
    @DisplayName("Создание пользователя")
    @Description("Test for /auth/register endpoint")
    public void testCreateUniqueUser() {
        user = new CreateUser(userPassword, userName ,userMail);
        response = user.getResponse();
        assertEquals(200, response.getStatusCode());
        user.delete();
    }

    @Test
    @Feature("Создание существующего пользователя")
    @DisplayName("Создание существующего пользователя")
    @Description("Test for /auth/register endpoint")
    public void testCreateExistingUser() {
        userName = "Paul";
        userPassword = "Larson";
        userMail = "Paul.Larson@ya.ru";
        user = new CreateUser(userName ,userPassword, userMail);
        response = user.getResponse();
        assertEquals(403, response.getStatusCode());
        assertEquals("User already exists",
                response.getBody().jsonPath().getString("message"));
    }

    @Test
    @Feature("Создание пользователя без email")
    @DisplayName("Создание пользователя без email")
    @Description("Test for /auth/register endpoint")
    public void testCreateUserEmptyEmail() {
        userMail = "";
        user = new CreateUser(userName ,userPassword, userMail);
        response = user.getResponse();
        assertEquals(403, response.getStatusCode());
        assertEquals("Email, password and name are required fields",
                response.getBody().jsonPath().getString("message"));
    }

    @After
    public void rollBck(){
        Allure.attachment("Answer status code: ", String.valueOf(response.getStatusCode()));
        Allure.attachment("Answer body: ", String.valueOf(response.getBody().prettyPrint()));
    }
}
