package service;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import utils.OrderDataGeneration;

import static io.restassured.RestAssured.given;
import static service.RestAssured.getBaseSpec;

public class OrderService {
    private static final String ORDER_PATH = "/api/orders";

    @Step
    public static Response createOrder(OrderDataGeneration order, String token) {
        return given()
                .spec(getBaseSpec())
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body(order)
                .post(ORDER_PATH);
    }

    @Step
    public static Response createWithoutAuth(OrderDataGeneration order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step
    public static Response getOrderForUser(String token) {
        return given()
                .spec(getBaseSpec())
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .when()
                .get(ORDER_PATH);
    }

    @Step
    public static Response getOrderWithoutAuth() {
        return given()
                .spec(getBaseSpec())
                .contentType(ContentType.JSON)
                .when()
                .get(ORDER_PATH);
    }
}
