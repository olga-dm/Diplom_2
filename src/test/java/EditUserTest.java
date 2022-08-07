import dto.userDto.UserDto;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.UserService;
import utils.UserDataGeneration;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class EditUserTest {
    UserDto user;
    String accessToken;

    @Before
    public void before() throws InterruptedException {
        UserDto newUser = UserDataGeneration.generateNewUser();

        accessToken = UserService.registerUser(newUser)
                .then().assertThat()
                .statusCode(SC_OK)
                .extract().path("accessToken");

        user = UserDataGeneration.generateNewUser();

        Thread.sleep(1000);
    }

    @After
    public void after() {
        if (accessToken == null)
            return;
        UserService.deleteUser(accessToken)
                .then().assertThat()
                .statusCode(SC_ACCEPTED)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

    @Test
    @DisplayName("Проверка изменения данных пользователя с авторизацией")
    public void checkEditUserWithAuth() {
        UserService.changesUser(user, accessToken)
                .then().assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверка изменения данных пользователя с почтой которая уже используется")
    public void checkEditUserWithAuthSameEmail() {
        UserDto newUser = UserDataGeneration.generateNewUser();
        UserService.registerUser(newUser)
                .then().assertThat()
                .statusCode(SC_OK)
                .extract().path("accessToken");

        user.setEmail(newUser.getEmail());
        UserService.changesUser(user, accessToken)
                .then().assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
    }

    @Test
    @DisplayName("Проверка изменения данных пользователя без авторизации")
    public void checkEditUserWithotAuth() {
        UserService.changesUser(user, "")
                .then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
