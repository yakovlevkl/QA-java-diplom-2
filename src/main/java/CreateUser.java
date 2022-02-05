import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class CreateUser extends BaseUrl {
    String userName;
    String userEmail;
    String userLogin;

    public CreateUser(String name, String login, String email) {

        this.userName = name;
        this.userLogin = login;
        this.userEmail = email;
    }

    public CreateUser() {
        this.userEmail = "Naruto@email.org";
        this.userName = "12345";
        this.userLogin = "Uchiha";
    }

    public JSONObject getJson(){
        return new JSONObject()
                .put("email", this.userEmail)
                .put("password", this.userName)
                .put("name", this.userLogin);
    }

    @Step("Create order")
    public Response getResponse() {
        JSONObject json = getJson();
        Allure.attachment("Команда на создание пользователя: ", String.valueOf(json));

        // отправляем запрос на логин курьера возвращаем true или false
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json.toString())
                .when()
                .post(getBaseUrl() +  "/auth/register");
    }
}
