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

public class UpdateUserDataTest {

    String newUserName;
    String newUserMail;
    String newUserPassword;
    String userPassword;
    String userMail;
    String userName;
    String userAccessToken;
    Response response;
    CreateUser user;

    @Before
    public void setUp() {
        /*
            Изменение данных пользователя:
               - с авторизацией,
               - без авторизации,
        */
        // Сгенерим фейковые данный пользователя
        Faker faker = new Faker();
        userName = faker.name().firstName(); // Emory
        userPassword = faker.name().lastName(); // Barton
        userMail = userName + "." + userPassword + "@ya.ru"; // Emory.Barton@ya.ru

        // Создаем пользователя, запоминаем токен доступа.
        user = new CreateUser(userPassword, userName ,userMail);
        response = user.getResponse();
        userAccessToken = user.getAccessToken();
        newUserName = "new_test_qa";
        newUserMail = "new_test_qa@ya.com";
        newUserPassword = "test_qa";
    }

    @Test
    @Feature("Изменение данных авторизированного пользователя")
    @DisplayName("Изменение данных авторизированного пользователя")
    @Description("Test for /auth/user endpoint")
    public void testChangedDataAuthorizedUser() {
        UserData user = new UserData(
                userAccessToken, newUserMail, newUserPassword, newUserName);
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

    @After
    public void rollBck() {
        Allure.attachment("Answer status code: ", String.valueOf(response.getStatusCode()));
        Allure.attachment("Answer body: ", String.valueOf(response.getBody().prettyPrint()));
        if (user != null) {
            user.delete();
        }
    }
}
