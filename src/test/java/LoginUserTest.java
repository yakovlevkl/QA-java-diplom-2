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

public class LoginUserTest {
         /*
            Логин пользователя:
               - логин под существующим пользователем,
               - логин с неверным логином и паролем.
         */

    String userPassword;
    String userMail;
    String userName;
    Response response;

    @Before
    public void setUp() {
        Faker faker = new Faker();
        userName = faker.name().firstName(); // Emory
        userPassword = faker.name().lastName(); // Barton
        userMail = userName + "." + userPassword + "@ya.ru"; // Emory.Barton@ya.ru
        CreateUser user = new CreateUser(userPassword, userName, userMail);
        user.getResponse();
    }

    @Test
    @Feature("Авторизация пользователя")
    @DisplayName("Control return response code and body")
    @Description("Test for /auth/login endpoint")
    public void testLoginUser() {
        LoginUser user = new LoginUser(userMail, userPassword);
        response = user.getResponse();
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Feature("Авторизация пользователя с неверным логином и паролем")
    @DisplayName("Control return response code and body")
    @Description("Test for /auth/login endpoint")
    public void testLoginIncorrectUser() {
        userMail = "userMail";
        userPassword = "userPassword";
        LoginUser user = new LoginUser(userMail, userPassword);
        response = user.getResponse();
        assertEquals(401, response.getStatusCode());
        assertEquals("email or password are incorrect",
                response.getBody().jsonPath().getString("message"));
        System.out.println(response);
    }

    @After
    public void rollBck(){
        Allure.attachment("Answer status code: ", String.valueOf(response.getStatusCode()));
        Allure.attachment("Answer body: ", String.valueOf(response.getBody().prettyPrint()));
    }
}
