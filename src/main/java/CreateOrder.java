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
                .spec(BaseUrl.getBaseSpec())
                .auth().oauth2(userToken)
                .and()
                .body(json.toString())
                .when()
                .post( "/orders/");
    }

    @Step("Get ingredients")
    public Response getIngredients() {
        return given()
                .spec(BaseUrl.getBaseSpec())
                .when()
                .get("/ingredients");
    }
}
