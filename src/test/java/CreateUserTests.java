import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateUserTests extends BaseTest {

    @Test
    @DisplayName("Создать уникального пользователя")
    public void shouldCreateUniqueUser() {
        String randomValue = getRandomString();
        String email = randomValue + "@yandex.ru";
        String password = "pass_" + randomValue;
        String name = "User_" + randomValue;

        User user = new User(email, password, name);

        accessToken = apiUserClient.createUser(user)
                .statusCode(200)
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    public void shouldNotCreateDuplicateUser() {
        String randomValue = getRandomString();
        String email = randomValue + "@yandex.ru";
        String password = "pass_" + randomValue;
        String name = "User_" + randomValue;

        User user = new User(email, password, name);

        accessToken = apiUserClient.createUser(user)
                .statusCode(200)
                .extract()
                .path("accessToken");

        apiUserClient.createUser(user)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создать пользователя и не заполнить одно из обязательных полей (email)")
    public void shouldNotCreateUserWithoutRequiredField() {
        String randomValue = getRandomString();
        String password = "pass_" + randomValue;
        String name = "User_" + randomValue;

        User userWithoutEmail = new User(null, password, name);

        apiUserClient.createUser(userWithoutEmail)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
