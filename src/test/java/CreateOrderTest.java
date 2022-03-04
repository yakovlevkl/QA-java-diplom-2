import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreateOrderTest extends BeforeTests {

    private List<String> ingredients;
    private CreateOrder order;
    private String[] id;


    @Before
    public void setUp_() {
        /*
            Создание заказа:
               - с авторизацией,
               - без авторизации,
               - с ингредиентами,
               - без ингредиентов,
               - с неверным хешем ингредиентов.
        */

        // Генерим ингридиенты для заказа
        order = new CreateOrder();
        id = order.getIngredients().getBody().jsonPath().getString("data._id").split(",");
    }

    @Test
    @Feature("Создание заказа с валидными ингридиентами с авторизацией")
    @DisplayName("Создание заказа с валидными ингридиентами с авторизацией")
    @Description("Test for /orders/ endpoint")
    public void testCreateOrderValidIngredientsAuthUser() {
        ingredients = List.of(id[4].trim(), id[3].trim(), id[2].trim());
        response = order.postOrder(ingredients, accessToken);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Feature("Создание заказа с не валидными ингридиентами с авторизацией")
    @DisplayName("Создание заказа с не валидными ингридиентами с авторизацией")
    @Description("Test for /orders/ endpoint")
    public void testCreateOrderNotValidIngredientsAuthUser() {
        ingredients = List.of(id[4], id[3], id[2]);
        response = order.postOrder(ingredients, accessToken);
        assertEquals(500, response.getStatusCode());
    }

    @Test
    @Feature("Создание заказа без ингридиентов с авторизацией")
    @DisplayName("Создание заказа без ингридиентов с авторизацией")
    @Description("Test for /orders/ endpoint")
    public void testCreateOrderEmptyIngredientsAuthUser() {
        response = order.postOrder(ingredients, accessToken);
        assertEquals(400, response.getStatusCode());
        assertEquals("Ingredient ids must be provided",
                response.getBody().jsonPath().getString("message"));
    }

    @Test
    @Feature("Создание заказа с валидными ингридиентами без авторизации")
    @DisplayName("Создание заказа с валидными ингридиентами без авторизации")
    @Description("Test for /orders/ endpoint")
    public void testCreateOrderValidIngredientsNotAuthUser() {
        ingredients = List.of(id[4].trim(), id[3].trim(), id[2].trim());
        response = order.postOrder(ingredients, "");
        assertEquals(200, response.getStatusCode());
    }
}
