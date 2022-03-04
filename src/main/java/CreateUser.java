import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;
import static io.restassured.RestAssured.given;

public class CreateUser extends BaseUrls {
    private String userPassword;
    private String userEmail;
    private String userLogin;
    private String accessToken;
    private String refreshToken;

    CreateUser(String password, String login, String email) {
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
                .spec(BaseUrls.getBaseSpec())
                .and()
                .body(json.toString())
                .when()
                .post(getAuthRegisterUrl());

        if (response.getStatusCode() == 200) {
            this.accessToken = response.getBody().jsonPath().getString("accessToken");
            this.refreshToken = response.getBody().jsonPath().getString("refreshToken");
        } else {
            this.accessToken = null;
            this.refreshToken = null;
        }

        return response;
    }

    @Step("Get access token")
    public String getAccessToken() {
        if (this.accessToken != null) {
            return this.accessToken.split(" ")[1];
        }
        return null;
    }

    @Step("Get refresh token")
    public String getRefreshToken() {
        return this.refreshToken;
    }

    @Step("Delete user")
    void delete() {
        Allure.attachment("User access token: ", String.valueOf(getAccessToken()));
        if (getAccessToken() != null) {
            given()
                    .spec(BaseUrls.getBaseSpec())
                    .auth().oauth2(getAccessToken())
                    .when()
                    .delete(getAuthUserUrl())
                    .then()
                    .statusCode(202);
        }
    }

}
