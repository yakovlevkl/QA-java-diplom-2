import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;
import static io.restassured.RestAssured.given;

public class CreateUser extends BaseUrl {
    String userPassword;
    String userEmail;
    String userLogin;
    String accessToken;
    String refreshToken;

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
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(json.toString())
                .when()
                .post(getBaseUrl() +  "/auth/register");
        this.accessToken = response.getBody().jsonPath().getString("accessToken");
        this.refreshToken = response.getBody().jsonPath().getString("refreshToken");
        return response;
    }

    @Step("Get access token")
    public String getAccessToken() {
        return this.accessToken.split(" ")[1];
    }

    @Step("Get refresh token")
    public String getRefreshToken() {
        return this.refreshToken;
    }
}
