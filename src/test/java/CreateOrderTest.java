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

public class CreateOrderTest {

    List<String> ingredients;
    String userPassword;
    String userMail;
    String userName;
    String userAccessToken;
    Response response;
    CreateOrder order;
    String[] id;
    CreateUser user;

    @Before
    public void setUp() {
        /*
            Создание заказа:
               - с авторизацией,
               - без авторизации,
               - с ингредиентами,
               - без ингредиентов,
               - с неверным хешем ингредиентов.
        */
        Faker faker = new Faker();
        userName = faker.name().firstName(); // Emory
        userPassword = faker.name().lastName(); // Barton
        userMail = userName + "." + userPassword + "@ya.ru"; // Emory.Barton@ya.ru

        // Создаем пользователя, запоминаем токен доступа.
        user = new CreateUser(userPassword, userName ,userMail);
        response = user.getResponse();
        userAccessToken = user.getAccessToken();

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
        response = order.postOrder(ingredients, userAccessToken);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Feature("Создание заказа с не валидными ингридиентами с авторизацией")
    @DisplayName("Создание заказа с не валидными ингридиентами с авторизацией")
    @Description("Test for /orders/ endpoint")
    public void testCreateOrderNotValidIngredientsAuthUser() {
        ingredients = List.of(id[4], id[3], id[2]);
        response = order.postOrder(ingredients, userAccessToken);
        assertEquals(500, response.getStatusCode());
    }

    @Test
    @Feature("Создание заказа без ингридиентов с авторизацией")
    @DisplayName("Создание заказа без ингридиентов с авторизацией")
    @Description("Test for /orders/ endpoint")
    public void testCreateOrderEmptyIngredientsAuthUser() {
        response = order.postOrder(ingredients, userAccessToken);
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

    @After
    public void rollBck() {
        Allure.attachment("Answer status code: ", String.valueOf(response.getStatusCode()));
        Allure.attachment("Answer body: ", String.valueOf(response.getBody().prettyPrint()));
        if (user != null) {
            user.delete();
        }
    }
}
