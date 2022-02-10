import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderUserTest extends BeforeTests {
    private Response response;

    private static int randomIndex() {
        return (int) (Math.random() * 10);
    }

    @Before
    public void setUp_() {
        /*
            Получение заказов конкретного пользователя:
               - авторизованный пользователь,
               - неавторизованный пользователь.
        */
        // Сгенерим фейковые данный пользователя

        // Генерим ингридиенты и заказ пользователя
        CreateOrder order = new CreateOrder();
        String[] id = order.getIngredients().getBody().jsonPath().getString("data._id").split(",");
        List<String> ingredients = List.of(
                id[randomIndex()].trim(),
                id[randomIndex()].trim(),
                id[randomIndex()].trim());
        response = order.postOrder(ingredients, accessToken);
    }

    @Test
    @Feature("Данные по заказам авторизированного пользователя")
    @DisplayName("Данные по заказам авторизированного пользователя")
    @Description("Test for /api/orders endpoint")
    public void testGetOrderAuthUser() {
        OrderUser order = new OrderUser(accessToken);
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
}
