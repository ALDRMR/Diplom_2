package apiclient;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ApiIngredientsClient {

    private static final String INGREDIENTS_PATH = "/api/ingredients";

    @Step("Получение данных об ингредиентах GET " + INGREDIENTS_PATH)
    public ValidatableResponse getIngredients() {
        return given()
                .spec(ApiTestsConfig.getSpecification())
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .spec(ApiTestsConfig.getResponseSpecification());
    }

}
