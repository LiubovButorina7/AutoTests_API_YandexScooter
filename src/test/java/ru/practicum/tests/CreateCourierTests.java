package ru.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.config.RestConfig;
import ru.practicum.models.Courier;
import ru.practicum.steps.CourierSteps;
import static org.hamcrest.Matchers.*;
import org.apache.http.HttpStatus;

public class CreateCourierTests extends BaseTest {
    private final CourierSteps courierSteps = new CourierSteps();
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courier = new Courier();
    }

    @Test
    @DisplayName("Create courier with login and password")
    @Description("Test for '/api/v1/courier' endpoint")
    public void testCreateCourierWithLoginAndPasswordReturnsSuccess() {
        setRequestLoginAndPassword(RandomStringUtils.randomAlphabetic(12), RandomStringUtils.randomAlphabetic(12));
        ValidatableResponse response = courierSteps.createCourier(courier);
        checkCodeResponse(response, HttpStatus.SC_CREATED);
        checkBodyResponse(response, RestConfig.KEY_CREATE_SUCCESS, RestConfig.VALUE_CREATE_SUCCESS);
    }

    @Test
    @DisplayName("Create courier without login and with password")
    @Description("Test for '/api/v1/courier' endpoint")
    public void testCreateCourierWithoutLoginAndWithPasswordReturnsFailure() {
        setRequestLoginAndPassword(null, RandomStringUtils.randomAlphabetic(12));
        ValidatableResponse response = courierSteps.createCourier(courier);
        checkCodeResponse(response, HttpStatus.SC_BAD_REQUEST);
        checkBodyResponse(response, RestConfig.KEY_FAILURE, RestConfig.VALUE_CREATE_FAILURE);
    }

    @Test
    @DisplayName("Create сourier with login and without password")
    @Description("Test for '/api/v1/courier' endpoint")
    public void testCreateCourierWithLoginAndWithoutPasswordReturnsFailure() {
        setRequestLoginAndPassword(RandomStringUtils.randomAlphabetic(12),null);
        ValidatableResponse response = courierSteps.createCourier(courier);
        checkCodeResponse(response, HttpStatus.SC_BAD_REQUEST);
        checkBodyResponse(response, RestConfig.KEY_FAILURE, RestConfig.VALUE_CREATE_FAILURE);
    }

    @Test
    @DisplayName("Create сourier with already existing login")
    @Description("Test for '/api/v1/courier' endpoint")
    public void testCreateCourierWithAlreadyExistingLoginReturnsFailure() {
        setRequestLoginAndPassword(RandomStringUtils.randomAlphabetic(12), RandomStringUtils.randomAlphabetic(12));
        courierSteps.createCourier(courier);
        ValidatableResponse response = courierSteps.createCourier(courier);
        checkCodeResponse(response, HttpStatus.SC_CONFLICT);
        checkBodyResponse(response, RestConfig.KEY_FAILURE, RestConfig.VALUE_CREATE_CONFLICT);
    }

    @Step("Set courier login and password")
    public void setRequestLoginAndPassword(String login, String password) {
        courier.setLogin(login);
        courier.setPassword(password);
    }

    @Step("Get id from authorized courier")
    public void getResponseCourierId() {
        try {
            courierId = courierSteps.loginCourier(courier).extract().body().path("id");
        } catch (Exception e) {
            System.out.println("Ошибка при парсинге JSON: " + e.getMessage());
        }
    }

    @Step("Set courier id for delete request")
    public void setRequestCourierId() {
        courier.setId(courierId);
    }

    @Step("Check code response")
    public void checkCodeResponse(ValidatableResponse response, Integer expectedCode) {
        response.statusCode(expectedCode);
    }

    @Step("Check body response")
    public void checkBodyResponse(ValidatableResponse response, String key, Object value) {
        response.body(key, is(value));
    }

    @After
    public void tearDown() {
        getResponseCourierId();
        if (courierId != null) {
            setRequestCourierId();
            courierSteps.deleteCourier(courier);
        }
    }
}
