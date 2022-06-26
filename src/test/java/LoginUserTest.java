import dto.userDto.UserDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.UserService;
import utils.DataGeneration;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    UserDto user;
    String accessToken;

    @Before
    public void before() {
        UserDto newUser = DataGeneration.generateNewUser();
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
    public void checkShouldLogin() {
        UserService.loginUser(user)
                .then().assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void checkShouldNotLoginWithoutPassword() {
        user.setPassword(null);
        UserService.loginUser(user)
                .then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void checkShouldNotLoginWithoutEmail() {
        user.setEmail(null);
        UserService.loginUser(user)
                .then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
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
