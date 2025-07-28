package ru.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.practicum.config.Endpoints;
import ru.practicum.models.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("Send POST request to /api/v1/orders")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(Endpoints.CREATE_GET_ORDER)
                .then();
    }

    @Step("Send GET request to /api/v1/orders")
    public ValidatableResponse getOrders() {
        return given()
                .when()
                .get(Endpoints.CREATE_GET_ORDER)
                .then();
    }

    @Step("Send PUT request to /api/v1/orders/cancel")
    public void cancelOrder(Order order) {
        given()
                .body(order)
                .when()
                .put(Endpoints.CANCEL_ORDER)
                .then();
    }
}
