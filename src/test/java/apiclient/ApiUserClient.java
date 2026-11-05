package apiclient;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.User;

import static io.restassured.RestAssured.given;

public class ApiUserClient {

    private static final String CREATE_USER_PATH = "/api/auth/register";
    private static final String USER_PATH = "/api/auth/user";
    private static final String LOGIN_USER_PATH = "/api/auth/login";

    @Step("Создание пользователя POST " + CREATE_USER_PATH)
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(ApiTestsConfig.getSpecification())
                .body(user)
                .when()
                .post(CREATE_USER_PATH)
                .then()
                .spec(ApiTestsConfig.getResponseSpecification());
    }

    @Step("Удаление пользователя DELETE " + USER_PATH)
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(ApiTestsConfig.getSpecification())
                .header("Authorization", accessToken)
                .when()
                .delete(USER_PATH)
                .then()
                .spec(ApiTestsConfig.getResponseSpecification());
    }

    @Step("Логин пользователя POST " + LOGIN_USER_PATH)
    public ValidatableResponse loginUser(User user) {
        return given()
                .spec(ApiTestsConfig.getSpecification())
                .body(user)
                .when()
                .post(LOGIN_USER_PATH)
                .then()
                .spec(ApiTestsConfig.getResponseSpecification());
    }

}
