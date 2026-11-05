package apiclient;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Order;

import static io.restassured.RestAssured.given;

public class ApiOrderClient {

    private static final String ORDERS_PATH = "/api/orders";
    private static final String ORDERS_ALL_PATH = "/api/orders/all";

    @Step("Создание нового заказа POST " + ORDERS_PATH)
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return given()
                .spec(ApiTestsConfig.getSpecification())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS_PATH)
                .then()
                .spec(ApiTestsConfig.getResponseSpecification());
    }

    @Step("Получить все заказы GET " + ORDERS_ALL_PATH)
    public ValidatableResponse getAllOrders() {
        return given()
                .spec(ApiTestsConfig.getSpecification())
                .when()
                .get(ORDERS_ALL_PATH)
                .then()
                .spec(ApiTestsConfig.getResponseSpecification());
    }

    @Step("Получить заказы конкретного пользователя GET " + ORDERS_PATH)
    public ValidatableResponse getUserOrders(String accessToken) {
        return given()
                .spec(ApiTestsConfig.getSpecification())
                .header("Authorization", accessToken)
                .when()
                .get(ORDERS_PATH)
                .then()
                .spec(ApiTestsConfig.getResponseSpecification());
    }

}
