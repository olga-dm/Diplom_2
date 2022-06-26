package service;

import dto.userDto.UserDto;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static service.RestAssured.getBaseSpec;

public class UserService {
    private static final String USER_PATH = "/api/auth/";

    @Step
    public static Response registerUser(UserDto userDto) {
        return given()
                .spec(getBaseSpec())
                .contentType(ContentType.JSON)
                .body(userDto)
                .post(USER_PATH + "register");
    }

    @Step
    public static Response loginUser(UserDto userDto) {
        return given()
                .spec(getBaseSpec())
                .contentType(ContentType.JSON)
                .body(userDto)
                .post(USER_PATH + "login");
    }

    @Step
    public static Response deleteUser(String token) {
        return given()
                .spec(getBaseSpec())
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .delete(USER_PATH + "user");
    }

    @Step
    public static Response changesUser(UserDto userDto,String token){
        return given()
                .spec(getBaseSpec())
                .contentType(ContentType.JSON)
                .body(userDto)
                .header("authorization", token)
                .patch(USER_PATH + "user");
    }
}
