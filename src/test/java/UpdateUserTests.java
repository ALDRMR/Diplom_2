import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTests extends BaseTest {

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
