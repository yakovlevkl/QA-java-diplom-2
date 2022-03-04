import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoginUserTest extends BeforeTests {
         /*
            Логин пользователя:
               - логин под существующим пользователем,
               - логин с неверным логином и паролем.
         */

    @Test
    @Feature("Авторизация пользователя")
    @DisplayName("Авторизация пользователя")
    @Description("Test for /auth/login endpoint")
    public void testLoginUser() {
        LoginUser user = new LoginUser(userEmail, userPassword);
        response = user.getResponseLogin();
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Feature("Авторизация пользователя с неверным логином и паролем")
    @DisplayName("Авторизация пользователя с неверным логином и паролем")
    @Description("Test for /auth/login endpoint")
    public void testLoginIncorrectUser() {
        LoginUser user = new LoginUser("userMail","userPassword");
        response = user.getResponseLogin();
        assertEquals(401, response.getStatusCode());
        assertEquals("email or password are incorrect",
                response.getBody().jsonPath().getString("message"));
    }

}
