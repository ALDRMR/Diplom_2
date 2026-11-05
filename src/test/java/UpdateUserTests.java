import apiclient.ApiUserClient;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTests {

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
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void shouldUpdateUserData() {
        String randomValue = getRandomString();
        User originalUser = new User(
                randomValue + "@yandex.ru",
                "pass_" + randomValue,
                "User_" + randomValue);

        accessToken = apiUserClient.createUser(originalUser)
                .statusCode(200)
                .extract()
                .path("accessToken");

        String newRandomValue = getRandomString();
        User updatedUser = new User(
                newRandomValue + "@yandex.ru",
                "pass_" + randomValue,
                "NewName_" + newRandomValue);

        apiUserClient.updateUser(updatedUser, accessToken)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(updatedUser.getEmail()))
                .body("user.name", equalTo(updatedUser.getName()));

        apiUserClient.loginUser(updatedUser)
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void shouldNotUpdateUserDataWithoutAuth() {
        String randomValue = getRandomString();
        User originalUser = new User(
                randomValue + "@yandex.ru",
                "pass_" + randomValue,
                "User_" + randomValue);

        accessToken = apiUserClient.createUser(originalUser)
                .statusCode(200)
                .extract()
                .path("accessToken");

        String newRandomValue = getRandomString();
        User updatedUser = new User(
                newRandomValue + "@yandex.ru",
                "pass_" + randomValue,
                "NewName_" + newRandomValue);

        apiUserClient.updateUser(updatedUser, null)
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

}
