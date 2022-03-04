import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;
import static io.restassured.RestAssured.given;

public class LoginUser extends BaseUrls {
    private String userEmail;
    private String userPassword;

    LoginUser(String email, String password) {
        this.userEmail = email;
        this.userPassword = password;
    }

    public JSONObject getJson(){
        return new JSONObject()
                .put("email", this.userEmail)
                .put("password", this.userPassword);
    }

    @Step("Login user")
    Response getResponseLogin() {
        JSONObject json = getJson();
        Allure.attachment("User login data: ", String.valueOf(json));
        return given()
                .spec(getBaseSpec())
                .and()
                .body(json.toString())
                .when()
                .post(getLoginUserUrl());
    }
}
