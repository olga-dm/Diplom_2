import dto.userDto.UserDto;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.OrderService;
import service.UserService;
import utils.OrderDataGeneration;
import utils.UserDataGeneration;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderTest {
    UserDto user;
    String accessToken;

    @Before
    public void before() throws InterruptedException {
        UserDto newUser = UserDataGeneration.generateNewUser();
        user = new UserDto();
        user.setEmail(newUser.getEmail());
        user.setPassword(newUser.getPassword());
        user.setName(newUser.getName());

        UserService.registerUser(newUser)
                .then().assertThat()
                .statusCode(SC_OK);

        accessToken = UserService.loginUser(user)
                .then().assertThat()
                .statusCode(SC_OK)
                .extract().path("accessToken");

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
    @DisplayName("Получение заказа авторизованного пользователя")
    public void checkGetOrderWithAuth() {
        OrderDataGeneration orderDataGeneration = OrderDataGeneration.getIngredients();
        OrderService.createOrder(orderDataGeneration, accessToken)
                .then().assertThat()
                .statusCode(SC_OK);
        OrderService.getOrderForUser(accessToken)
                .then().assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("total", notNullValue())
                .body("totalToday", notNullValue())
                .body("orders[0]._id", notNullValue())
                .body("orders[0].ingredients", notNullValue())
                .body("orders[0].createdAt", notNullValue())
                .body("orders[0].updatedAt", notNullValue())
                .body("orders[0].number", notNullValue())
                .body("orders[0].status", equalTo("done"))
                .body("orders[0].name", equalTo("Бессмертный spicy краторный бургер"));
    }

    @Test
    @DisplayName("Получение заказа неавторизованного пользователя")
    public void checkGetOrderWithoutAuth() {
        OrderDataGeneration orderDataGeneration = OrderDataGeneration.getIngredients();
        OrderService.getOrderWithoutAuth()
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
