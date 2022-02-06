import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;
import java.util.List;

import static io.restassured.RestAssured.given;

public class CreateOrder extends BaseUrl {
    List<String> ingredients;

    public CreateOrder() {
    }

    public JSONObject getJson(List<String> ingredients){
        this.ingredients = ingredients;
        return new JSONObject()
                .put("ingredients",
                        this.ingredients);
    }

    @Step("Create order")
    public Response postOrder(List<String> ingredients, String userToken) {
        JSONObject json = getJson(ingredients);
        Allure.attachment("New order data: ", String.valueOf(json));
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(userToken)
                .and()
                .body(json.toString())
                .when()
                .post(getBaseUrl() +  "/orders/");
    }

    @Step("Get ingredients")
    public Response getIngredients() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(getBaseUrl() +  "/ingredients");
    }
}
