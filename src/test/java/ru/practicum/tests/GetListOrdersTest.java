package ru.practicum.tests;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import ru.practicum.config.RestConfig;
import ru.practicum.steps.OrderSteps;
import static org.hamcrest.Matchers.*;

public class GetListOrdersTest extends BaseTest {
    private final OrderSteps orderSteps = new OrderSteps();

    @Test
    public void testGetListOrdersReturnsListOrders() {
        ValidatableResponse response = orderSteps.getOrders();
        checkCodeResponse(response, RestConfig.CODE_RESPONSE_SUCCESS);
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
