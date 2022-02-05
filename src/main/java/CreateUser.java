import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class CreateUser extends BaseUrl {
    String userPassword;
    String userEmail;
    String userLogin;

    public CreateUser(String password, String login, String email) {
        this.userPassword = password;
        this.userLogin = login;
        this.userEmail = email;
    }

    public JSONObject getJson(){
        return new JSONObject()
                .put("email", this.userEmail)
                .put("password", this.userPassword)
                .put("name", this.userLogin);
    }

    @Step("Create user")
    public Response getResponse() {
        JSONObject json = getJson();
        Allure.attachment("New user data: ", String.valueOf(json));

        // отправляем запрос на создание пользователя
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json.toString())
                .when()
                .post(getBaseUrl() +  "/auth/register");
    }
}
