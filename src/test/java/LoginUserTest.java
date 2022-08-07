import dto.userDto.UserDto;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.UserService;
import utils.UserDataGeneration;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    UserDto user;
    String accessToken;

    @Before
    public void before() throws InterruptedException {
        UserDto newUser = UserDataGeneration.generateNewUser();
        user = new UserDto();
        user.setEmail(newUser.getEmail());
        user.setPassword(newUser.getPassword());

        accessToken = UserService.registerUser(newUser)
                .then().assertThat()
                .statusCode(SC_OK)
                .extract().path("accessToken");
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
    @DisplayName("Проверка авторизация с существующим логином и паролем")
    public void checkShouldLogin() {
        UserService.loginUser(user)
                .then().assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверка авторизация без ввода пароля")
    public void checkShouldNotLoginWithoutPassword() {
        user.setPassword(null);
        UserService.loginUser(user)
                .then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверка авторизация без ввода email")
    public void checkShouldNotLoginWithoutEmail() {
        user.setEmail(null);
        UserService.loginUser(user)
                .then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверка авторизация без ввода email и пароля")
    public void checkShouldNotLoginWithoutEmailAndPassword() {
        user.setEmail(null);
        user.setPassword(null);
        UserService.loginUser(user)
                .then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
