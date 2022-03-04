import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import static io.restassured.http.ContentType.JSON;

class BaseUrls {

    static String getOrdersUrl() {
        return "/orders";
    }

    static String getIngredientsUrl() {
        return "/ingredients";
    }

    static String getAuthUserUrl() {
        return "/auth/user";
    }

    static String getAuthRegisterUrl() {
        return "/auth/register";
    }

    static String getLoginUserUrl() {
        return "/auth/login";
    }

    static RequestSpecification getBaseSpec() {
        String baseUrl = "https://stellarburgers.nomoreparties.site/api/";
        return new RequestSpecBuilder()
                .setContentType(JSON)
                .setBaseUri(baseUrl)
                .build();
    }

}
