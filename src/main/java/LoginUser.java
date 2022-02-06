import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class LoginUser extends BaseUrl {
    String userEmail;
    String userPassword;

    public LoginUser(String email, String password) {
        this.userEmail = email;
        this.userPassword = password;
    }

    public JSONObject getJson(){
        return new JSONObject()
                .put("email", this.userEmail)
                .put("password", this.userPassword);
    }

    @Step("Login user")
    public Response getResponseLogin() {
        JSONObject json = getJson();
        Allure.attachment("User login data: ", String.valueOf(json));

        // отправляем запрос на логин пользователя
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json.toString())
                .when()
                .post(getBaseUrl() +  "/auth/login");
    }

}
