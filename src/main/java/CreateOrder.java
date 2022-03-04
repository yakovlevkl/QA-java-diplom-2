import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;
import java.util.List;

import static io.restassured.RestAssured.given;

public class CreateOrder extends BaseUrls {

    private JSONObject getJson(List<String> ingredients){
        return new JSONObject()
                .put("ingredients",
                        ingredients);
    }

    @Step("Create order")
    Response postOrder(List<String> ingredients, String userToken) {
        JSONObject json = getJson(ingredients);
        Allure.attachment("New order data: ", String.valueOf(json));
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(userToken)
                .and()
                .body(json.toString())
                .when()
                .post(getOrdersUrl());
    }

    @Step("Get ingredients")
    public Response getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(getIngredientsUrl());
    }
}
