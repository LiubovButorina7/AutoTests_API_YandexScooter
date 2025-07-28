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

public class LoginCourierTests extends BaseTest {
    private final CourierSteps courierSteps = new CourierSteps();
    private Courier courier;

    private String loginCorrectAuthorized;
    private String passwordCorrectAuthorized;

    @Before
    public void setUp() {
        courier = new Courier();
        setRequestLoginAndPassword(RandomStringUtils.randomAlphabetic(12), RandomStringUtils.randomAlphabetic(12));
        courierSteps.createCourier(courier);
        saveLoginAndPasswordAuthorized();
    }

    @Test
    @DisplayName("Login courier with existing login and password")
    @Description("Test for '/api/v1/courier/login' endpoint")
    public void testLoginCourierWithExistingLoginAndPasswordReturnsId() {
        ValidatableResponse response = courierSteps.loginCourier(courier);
        checkCodeResponse(response, HttpStatus.SC_OK);
        checkBodyResponseLoginSuccess(response, RestConfig.KEY_LOGIN_SUCCESS);
    }

    @Test
    @DisplayName("Login courier with existing login and not existing password")
    @Description("Test for '/api/v1/courier/login' endpoint")
    public void testLoginCourierWithExistingLoginAndNotExistingPasswordReturnsFailure() {
        setRequestLoginAndPassword(loginCorrectAuthorized, RandomStringUtils.randomAlphabetic(12));
        ValidatableResponse response = courierSteps.loginCourier(courier);
        checkCodeResponse(response, HttpStatus.SC_NOT_FOUND);
        checkBodyResponse(response, RestConfig.KEY_FAILURE, RestConfig.VALUE_LOGIN_NOT_FOUND);
    }

    @Test
    @DisplayName("Login courier with not existing login and existing password")
    @Description("Test for '/api/v1/courier/login' endpoint")
    public void testLoginCourierWithNotExistingLoginAndExistingPasswordReturnsFailure() {
        setRequestLoginAndPassword(RandomStringUtils.randomAlphabetic(12), passwordCorrectAuthorized);
        ValidatableResponse response = courierSteps.loginCourier(courier);
        checkCodeResponse(response, HttpStatus.SC_NOT_FOUND);
        checkBodyResponse(response, RestConfig.KEY_FAILURE, RestConfig.VALUE_LOGIN_NOT_FOUND);
    }

    @Test
    @DisplayName("Login courier with login and without password")
    @Description("Test for '/api/v1/courier/login' endpoint")
    public void testLoginCourierWithLoginAndWithoutPasswordReturnsFailure() throws Exception {
        setRequestLoginAndPassword(loginCorrectAuthorized, null);
        ValidatableResponse response = courierSteps.loginCourier(courier);
        checkCodeResponse(response, HttpStatus.SC_BAD_REQUEST);
        checkBodyResponse(response, RestConfig.KEY_FAILURE, RestConfig.VALUE_LOGIN_NOT_ALLOWED);
    }

    @Test
    @DisplayName("Login courier without login and with password")
    @Description("Test for '/api/v1/courier/login' endpoint")
    public void testLoginCourierWithoutLoginAndWithPasswordReturnsFailure() {
        setRequestLoginAndPassword(null, passwordCorrectAuthorized);
        ValidatableResponse response = courierSteps.loginCourier(courier);
        checkCodeResponse(response, HttpStatus.SC_BAD_REQUEST);
        checkBodyResponse(response, RestConfig.KEY_FAILURE, RestConfig.VALUE_LOGIN_NOT_ALLOWED);
    }

    @Step("Set courier login and password")
    public void setRequestLoginAndPassword(String login, String password) {
        courier.setLogin(login);
        courier.setPassword(password);
    }

    @Step("Save authorized login and password")
    public void saveLoginAndPasswordAuthorized() {
        loginCorrectAuthorized = courier.getLogin();
        passwordCorrectAuthorized = courier.getPassword();
    }

    @Step("Check code response")
    public void checkCodeResponse(ValidatableResponse response, Integer expectedCode) {
        response.statusCode(expectedCode);
    }

    @Step("Check body response login successful")
    public void checkBodyResponseLoginSuccess(ValidatableResponse response, String key) {
        response.body(key, notNullValue());
    }

    @Step("Check body response")
    public void checkBodyResponse(ValidatableResponse response, String key, Object value) {
        response.body(key, is(value));
    }

    @Step("Get id from authorized courier")
    public Integer getResponseCourierId() {
        return courierSteps.loginCourier(courier).extract().body().path("id");
    }

    @Step("Set courier id for delete request")
    public void setRequestCourierId(Integer id) {
        courier.setId(id);
    }

    @After
    public void tearDown() {
        setRequestLoginAndPassword(loginCorrectAuthorized, passwordCorrectAuthorized);
        Integer id = getResponseCourierId();
        setRequestCourierId(id);
        courierSteps.deleteCourier(courier);
    }
}