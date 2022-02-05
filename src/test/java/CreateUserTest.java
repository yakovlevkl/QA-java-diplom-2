import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
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

    @Before
    public void setUp() {
        Faker faker = new Faker();
        userName = faker.name().firstName(); // Emory
        userPassword = faker.name().lastName(); // Barton
        userMail = userName + "." + userPassword + "@ya.ru"; // Emory.Barton@ya.ru
    }

    @Test
    @Feature("Создание пользователя")
    @DisplayName("Control return response code and body")
    @Description("Test for /auth/register endpoint")
    public void testCreateUniqueUser() {
        CreateUser user = new CreateUser(userName ,userPassword, userMail);
        assertEquals(200, user.getResponse().getStatusCode());
        assertEquals("User already exists",
                user.getResponse().getBody().jsonPath().getString("message"));
        Allure.attachment("Server answer: ",
                String.valueOf(user.getResponse().getBody().prettyPrint()));
    }

    @Test
    @Feature("Создание существующего пользователя")
    @DisplayName("Control return response code and body")
    @Description("Test for /auth/register endpoint")
    public void testCreateExistingUser() {
        userName = "Paul";
        userPassword = "Larson";
        userMail = "Paul.Larson@ya.ru";
        CreateUser user = new CreateUser(userName ,userPassword, userMail);
        assertEquals(403, user.getResponse().getStatusCode());
        assertEquals("User already exists",
                user.getResponse().getBody().jsonPath().getString("message"));
        Allure.attachment("Server answer: ",
                String.valueOf(user.getResponse().getBody().prettyPrint()));
    }

    @Test
    @Feature("Создание пользователя без email")
    @DisplayName("Control return response code and body")
    @Description("Test for /auth/register endpoint")
    public void testCreateUserEmptyEmail() {
        userMail = "";
        CreateUser user = new CreateUser(userName ,userPassword, userMail);
        assertEquals(403, user.getResponse().getStatusCode());
        assertEquals("Email, password and name are required fields",
                user.getResponse().getBody().jsonPath().getString("message"));
        Allure.attachment("Server answer: ",
                String.valueOf(user.getResponse().getBody().prettyPrint()));
    }

    @After
    public void rollBck(){

    }
}
