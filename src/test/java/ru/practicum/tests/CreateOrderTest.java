package ru.practicum.tests;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.practicum.config.RestConfig;
import ru.practicum.models.Order;
import ru.practicum.steps.OrderSteps;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseTest {
    private final OrderSteps orderSteps = new OrderSteps();
    private Order order;
    private Integer orderTrack;

    private final String[] color;
    private final String colorName;

    public CreateOrderTest(String[] color, String colorName) {
        this.color = color;
        this.colorName = colorName;
    }

    @Parameterized.Parameters(name = "Тестовые данные - цвет: {1} ")
    public static Object[][] getTestData() {
        return new Object[][] {
                { new String[]{"BLACK"}, "BLACK" },
                { new String[] {"GREY" }, "GREY" },
                { new String[] {"BLACK", "GREY"}, "BLACK, GREY" },
                { new String[] { null }, "null" }
        };
    }

    @Before
    public void setUp() {
        order = new Order();
        order.setFirstName("Alexey");
        order.setLastName("Ivanov");
        order.setAddress("Moscow, Arbat str.");
        order.setMetroStation(2);
        order.setPhone("+7 916 567 23 12");
        order.setRentTime(5);
        order.setDeliveryDate("2025-08-02");
        order.setComment("-");
        order.setColor(color);
    }

    @Test
    public void testCreateOrderReturnsTrack() {
        ValidatableResponse response = orderSteps.createOrder(order);
        getResponseTrack(response);
        checkCodeResponse(response, RestConfig.CODE_CREATE_SUCCESS);
        checkBodyResponse(response, RestConfig.KEY_ORDER);
    }

    @Step("Check code response")
    public void checkCodeResponse(ValidatableResponse response, Integer expectedCode) {
        response.statusCode(expectedCode);
    }

    @Step("Check body response")
    public void checkBodyResponse(ValidatableResponse response, String key) {
        response.body(key, notNullValue());
    }

    @Step("Get id from created order")
    public void getResponseTrack(ValidatableResponse response) {
        orderTrack = response.extract().body().path("id");
    }

    @Step("Set track id for cancel request")
    public void setRequestTrackId() {
        order.setTrack(orderTrack);
    }

    @After
    public void tearDown() {
        if (orderTrack != null) {
            setRequestTrackId();
            orderSteps.cancelOrder(order);
        }
    }
}
