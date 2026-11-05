import apiclient.ApiUserClient;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

public class LoginUserTests {

    private ApiUserClient apiUserClient;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        apiUserClient = new ApiUserClient();
        accessToken = null;
    }

    @AfterEach
    public void tearDown() {
        if (accessToken != null) {
            apiUserClient.deleteUser(accessToken).statusCode(202);
        }
    }

    private String getRandomString() {
        return UUID.randomUUID().toString();
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void shouldLoginWithExistingUser() {
        String randomValue = getRandomString();
        String email = randomValue + "@yandex.ru";
        String password = "pass_" + randomValue;
        String name = "User_" + randomValue;

        User user = new User(email, password, name);

        accessToken = apiUserClient.createUser(user)
                .statusCode(200)
                .extract()
                .path("accessToken");

        apiUserClient.loginUser(user)
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void shouldNotLoginWithIncorrectCredentials() {
        String randomValue = getRandomString();
        String email = randomValue + "@yandex.ru";
        String password = "pass_" + randomValue;
        String name = "User_" + randomValue;

        User user = new User(email, password, name);

        accessToken = apiUserClient.createUser(user)
                .statusCode(200)
                .extract()
                .path("accessToken");

        String wrongRandomValue = getRandomString();
        User incorrectUser = new User(
                wrongRandomValue + "@yandex.ru",
                "pass_" + wrongRandomValue,
                null);

        apiUserClient.loginUser(incorrectUser)
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

}