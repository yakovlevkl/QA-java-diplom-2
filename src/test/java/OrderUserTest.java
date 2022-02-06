import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderUserTest {
    List<String> ingredients;
    String userPassword;
    String userMail;
    String userName;
    String userAccessToken;
    Response response;
    CreateOrder order;
    String[] id;
    CreateUser user;

    public static int randomIndex() {
        return (int) (Math.random() * 10);
    }
    @Before
    public void setUp() {
        /*
            Получение заказов конкретного пользователя:
               - авторизованный пользователь,
               - неавторизованный пользователь.
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

        // Генерим ингридиенты и заказ пользователя
        order = new CreateOrder();
        id = order.getIngredients().getBody().jsonPath().getString("data._id").split(",");
        ingredients = List.of(
                id[randomIndex()].trim(),
                id[randomIndex()].trim(),
                id[randomIndex()].trim());
        response = order.postOrder(ingredients, userAccessToken);
    }

    @Test
    @Feature("Данные по заказам авторизированного пользователя")
    @DisplayName("Данные по заказам авторизированного пользователя")
    @Description("Test for /api/orders endpoint")
    public void testGetOrderAuthUser() {
        OrderUser order = new OrderUser(userAccessToken);
        response = order.getUserOrders();
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody().jsonPath().getString("orders._id"));
    }

    @Test
    @Feature("Данные по заказам не авторизированного пользователя")
    @DisplayName("Данные по заказам не авторизированного пользователя")
    @Description("Test for /api/orders endpoint")
    public void testPostOrder() {
        OrderUser order = new OrderUser("");
        response = order.getUserOrders();
        assertEquals(401, response.getStatusCode());
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
