package ru.practicum.config;

public class RestConfig {
    public static final String HOST = "https://qa-scooter.praktikum-services.ru/";
    public static final Integer CODE_CREATE_SUCCESS = 201;
    public static final String KEY_CREATE_SUCCESS = "ok";
    public static final Boolean VALUE_CREATE_SUCCESS = true;
    public static final Integer CODE_FAILURE = 400;
    public static final String KEY_FAILURE = "message";
    public static final String VALUE_CREATE_FAILURE = "Недостаточно данных для создания учетной записи";
    public static final Integer CODE_CREATE_COURIER_CONFLICT = 409;
    public static final String VALUE_CREATE_CONFLICT = "Этот логин уже используется";
    public static final Integer CODE_RESPONSE_SUCCESS = 200;
    public static final String KEY_LOGIN_SUCCESS = "id";
    public static final Integer CODE_LOGIN_NOT_FOUND = 404;
    public static final String VALUE_LOGIN_NOT_FOUND = "Учетная запись не найдена";
    public static final String VALUE_LOGIN_NOT_ALLOWED = "Недостаточно данных для входа";
    public static final String KEY_ORDER = "track";
    public static final String KEY_ORDERS_LIST = "orders";
}
