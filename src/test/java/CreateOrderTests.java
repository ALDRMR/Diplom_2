import model.Order;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTests extends BaseTest {

    private List<String> validIngredients;

    @BeforeEach
    public void setUp() {
        validIngredients = apiIngredientsClient.getIngredients()
                .statusCode(200)
                .extract()
                .path("data._id");
    }

    private String createTestUser() {
        String randomValue = getRandomString();
        User user = new User(
                randomValue + "@yandex.ru",
                "pass_" + randomValue,
                "User_" + randomValue);

        return apiUserClient.createUser(user)
                .statusCode(200)
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void shouldCreateOrder() {
        accessToken = createTestUser();

        List<String> orderIngredients = List.of(validIngredients.get(0), validIngredients.get(1));
        Order order = new Order(orderIngredients);

        apiOrderClient.createOrder(order, accessToken)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void shouldCreateOrderWithoutAuth() {

        List<String> orderIngredients = List.of(validIngredients.get(0), validIngredients.get(1));
        Order order = new Order(orderIngredients);

        apiOrderClient.createOrder(order, null)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void shouldNotCreateOrderWithoutIngredients() {
        accessToken = createTestUser();

        Order orderWithoutIngredients = new Order(new ArrayList<>());

        apiOrderClient.createOrder(orderWithoutIngredients, accessToken)
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void shouldNotCreateOrderWithInvalidIngredientHash() {
        accessToken = createTestUser();

        List<String> invalidHashList = List.of("invalid");
        Order order = new Order(invalidHashList);

        apiOrderClient.createOrder(order, accessToken)
                .statusCode(500);
    }

}
