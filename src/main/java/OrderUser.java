import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderUser extends BaseUrl {
    String userToken;

    public OrderUser(String userToken) {
        this.userToken = userToken;
    }

    @Step("Get user orders")
    public Response getUserOrders() {
        return given()
                .spec(BaseUrl.getBaseSpec())
                .auth().oauth2(this.userToken)
                .when()
                .get("/orders/");
    }
}
