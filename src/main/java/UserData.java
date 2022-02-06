import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class UserData extends BaseUrl {
    String userToken;
    String userMail;
    String userPassword;
    String userLogin;

    public UserData(String token,
                    String userMail,
                    String userPassword,
                    String userLogin) {
        this.userToken = token;
        this.userMail = userMail;
        this.userPassword = userPassword;
        this.userLogin = userLogin;
    }

    public JSONObject getJson(){
        return new JSONObject()
                .put("email", userMail)
                .put("password", userPassword)
                .put("name", userLogin);
    }

    @Step("Reform user data")
    public Response reformUserData() {
        JSONObject json = getJson();
        Allure.attachment("User data update: ", String.valueOf(json));
        return given()
                .spec(BaseUrl.getBaseSpec())
                .auth().oauth2(this.userToken)
                .and()
                .body(json.toString())
                .when()
                .patch("/auth/user");
    }

    @Step("Get user data")
    public Response getUserData() {
        return given()
                .spec(BaseUrl.getBaseSpec())
                .header("Content-type", "application/json")
                .auth().oauth2(this.userToken)
                .when()
                .patch("/auth/user");
    }
}
