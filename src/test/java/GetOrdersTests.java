import model.Order;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersTests extends BaseTest {

    @BeforeEach
    public void setUp() {
        String randomValue = getRandomString();
        User user = new User(
                randomValue + "@yandex.ru",
                "pass_" + randomValue,
                "User_" + randomValue);

        accessToken = apiUserClient.createUser(user)
                .statusCode(200)
                .extract()
                .path("accessToken");

        List<String> validIngredients = apiIngredientsClient.getIngredients()
                .statusCode(200)
                .extract()
                .path("data._id");

        Order order = new Order(List.of(validIngredients.get(0)));
        apiOrderClient.createOrder(order, accessToken)
                .statusCode(200);
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя: авторизованный пользователь")
    public void shouldGetOrders() {
        apiOrderClient.getUserOrders(accessToken)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя: неавторизованный пользователь")
    public void shouldNotGetOrdersWithoutAuth() {
        apiOrderClient.getUserOrders(null)
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

}
