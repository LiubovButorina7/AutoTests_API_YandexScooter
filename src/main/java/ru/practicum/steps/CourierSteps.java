package ru.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.practicum.models.Courier;

import static io.restassured.RestAssured.given;

public class CourierSteps {
    @Step("Send POST request to /api/v1/courier")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                    .body(courier)
                    .when()
                    .post("/api/v1/courier")
                    .then();
    }
    @Step("Send POST request to /api/v1/courier/login")
    public ValidatableResponse loginCourier(Courier courier) {
        return given()
                    .body(courier)
                    .when()
                    .post("/api/v1/courier/login")
                    .then();
    }
    @Step("Send DELETE request to /api/v1/courier/:id")
    public void deleteCourier(Courier courier) {
        given()
            .pathParams("id", courier.getId())
            .when()
            .delete("/api/v1/courier/{id}")
            .then();
    }
}
