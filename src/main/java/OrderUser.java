import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

class OrderUser extends BaseUrls {
    private String userToken;

    OrderUser(String userToken) {
        this.userToken = userToken;
    }

    @Step("Get user orders")
    Response getUserOrders() {
        return given()
                .spec(BaseUrls.getBaseSpec())
                .auth().oauth2(this.userToken)
                .when()
                .get(getOrdersUrl());
    }
}
