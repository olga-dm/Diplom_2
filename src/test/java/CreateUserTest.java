import dto.userDto.UserDto;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.UserService;
import utils.UserDataGeneration;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    UserDto user;
    String accessToken;

    @Before
    public void before() throws InterruptedException {
        user = UserDataGeneration.generateNewUser();
        Thread.sleep(1000);
    }

    @After
    public void tearDown() {
        if (accessToken == null)
            return;
        accessToken = UserService.loginUser(user)
                .then().assertThat()
                .statusCode(SC_OK)
                .extract().path("accessToken");
        UserService.deleteUser(accessToken)
                .then().assertThat()
                .statusCode(SC_ACCEPTED)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

    @Test
    @DisplayName("Проверка cоздания пользователя")
    public void checkCreatedUser() {
        UserService.registerUser(user).then().assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Проверка невозможности создания пользователя с теми же регистрационными данными")
    public void checkShouldNotCreatedSameUser() {
        UserService.registerUser(user).then().assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));

        UserService.registerUser(user).then().assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Проверка невозможности создания пользователя без email")
    public void checkShouldNotCreatedWithoutEmail() {
        user.setEmail(null);
        UserService.registerUser(user).then().assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка невозможности создания пользователя без password")
    public void checkShouldNotCreatedWithoutPassword() {
        user.setPassword(null);
        UserService.registerUser(user).then().assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка невозможности создания пользователя без name")
    public void checkShouldNotCreatedWithoutName() {
        user.setName(null);
        UserService.registerUser(user).then().assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
