import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;


public class BeforeTests {
    String userName;
    String userPassword;
    String userEmail;
    String accessToken;
    CreateUser user;
    Response response;
    String newUserName;
    String newUserMail;
    String newUserPassword;

    @Before
    public void setUp() {

        Faker faker = new Faker();
        userName = faker.name().firstName(); // Emory
        userPassword = faker.name().lastName(); // Barton
        userEmail = userName + "." + userPassword + "@ya.ru"; // Emory.Barton@ya.ru

        // Создаем пользователя, запоминаем токен доступа.
        user = new CreateUser(userPassword, userName, userEmail);
        response = user.getResponse();
        accessToken = user.getAccessToken();

        //новый пользователь
        newUserName = "new_test_qa_";
        newUserMail = "new_test_qa_@ya.com";
        newUserPassword = "test_qa_";
    }

    @After
    public void rollBck(){
        Allure.attachment("Answer status code: ", String.valueOf(response.getStatusCode()));
        Allure.attachment("Answer body: ", String.valueOf(response.getBody().prettyPrint()));
        user.delete();
    }
}


