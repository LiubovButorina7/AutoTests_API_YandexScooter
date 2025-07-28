package ru.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import ru.practicum.config.RestConfig;
import ru.practicum.steps.OrderSteps;
import static org.hamcrest.Matchers.*;
import org.apache.http.HttpStatus;

public class GetListOrdersTest extends BaseTest {
    private final OrderSteps orderSteps = new OrderSteps();

    @Test
    @DisplayName("Get orders list")
    @Description("Test for '/api/v1/orders' endpoint")
    public void testGetListOrdersReturnsListOrders() {
        ValidatableResponse response = orderSteps.getOrders();
        checkCodeResponse(response, HttpStatus.SC_OK);
        checkBodyResponse(response, RestConfig.KEY_ORDERS_LIST);
    }

    @Step("Check code response")
    public void checkCodeResponse(ValidatableResponse response, Integer expectedCode) {
        response.statusCode(expectedCode);
    }

    @Step("Check body response")
    public void checkBodyResponse(ValidatableResponse response, String key) {
        response.body(key, hasSize(greaterThan(0)));
    }
}
