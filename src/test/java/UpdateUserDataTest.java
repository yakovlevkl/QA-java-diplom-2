import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UpdateUserDataTest extends BeforeTests {

        /*
            Изменение данных пользователя:
               - с авторизацией,
               - без авторизации,
        */

    @Test
    @Feature("Изменение данных авторизированного пользователя")
    @DisplayName("Изменение данных авторизированного пользователя")
    @Description("Test for /auth/user endpoint")
    public void testChangedDataAuthorizedUser() {
        UserData user = new UserData(
                accessToken, newUserMail, newUserPassword, newUserName);
        response = user.reformUserData();

        // Проврка успешного изменения данных пользователя
        assertEquals(200, response.getStatusCode());

        response = user.getUserData();
        // Проверка измененного email
        assertEquals(newUserMail, response.getBody().jsonPath().getString("user.email"));
        // Проверка измененного name
        assertEquals(newUserName, response.getBody().jsonPath().getString("user.name"));
    }

    @Test
    @Feature("Изменение данных не авторизированного пользователя")
    @DisplayName("Изменение данных не авторизированного пользователя")
    @Description("Test for /auth/user endpoint")
    public void testChangedDataNotAuthorizedUser() {
        UserData user = new UserData(
                "", newUserMail, newUserPassword, newUserName);
        response = user.reformUserData();

        // Проврка кода отказа изменения данных пользователя
        assertEquals(401, response.getStatusCode());

        // Проверка сообщения
        response = user.getUserData();
        assertEquals("You should be authorised",
                response.getBody().jsonPath().getString("message"));
    }

}
