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
    Response response;

    @Before
    public void setUp() {
    }

    @Test
    @Feature("Авторизация пользователя")
    @DisplayName("Авторизация пользователя")
    @Description("Test for /auth/login endpoint")
    public void testLoginUser() {
        userMail = "Laureen.Leannon@ya.ru";
        userPassword = "Leannon";
        LoginUser user = new LoginUser(userMail, userPassword);
        response = user.getResponseLogin();
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Feature("Авторизация пользователя с неверным логином и паролем")
    @DisplayName("Авторизация пользователя с неверным логином и паролем")
    @Description("Test for /auth/login endpoint")
    public void testLoginIncorrectUser() {
        userMail = "userMail";
        userPassword = "userPassword";
        LoginUser user = new LoginUser(userMail, userPassword);
        response = user.getResponseLogin();
        assertEquals(401, response.getStatusCode());
        assertEquals("email or password are incorrect",
                response.getBody().jsonPath().getString("message"));
    }

    @After
    public void rollBck(){
        Allure.attachment("Answer status code: ", String.valueOf(response.getStatusCode()));
        Allure.attachment("Answer body: ", String.valueOf(response.getBody().prettyPrint()));
    }
}
