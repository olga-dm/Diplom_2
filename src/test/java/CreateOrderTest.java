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
import static org.hamcrest.Matchers.*;

public class CreateOrderTest {
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
    @DisplayName("Проверка создания заказа с авторизацией c игредиентами")
    public void checkCreateOrderWithAuth() {
        OrderDataGeneration orderDataGeneration = OrderDataGeneration.getIngredients();
        OrderService.createOrder(orderDataGeneration, accessToken)
                .then().assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", is(notNullValue()))
                .body("order.name", equalTo("Бессмертный spicy краторный бургер"))
                .body("order.owner.name", equalTo(user.getName()))
                .body("order.owner.email", equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Проверка создания заказа без авторизации c игредиентами")
    public void checkCreateOrderWithoutAuth() {
        OrderDataGeneration orderDataGeneration = OrderDataGeneration.getIngredients();
        OrderService.createWithoutAuth(orderDataGeneration)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", is(notNullValue()))
                .body("name", equalTo("Бессмертный spicy краторный бургер"));
    }

    @Test
    @DisplayName("Проверка создания заказа с авторизацией без игредиентов")
    public void checkCreateOrderWithAuthWithoutIngr() {
        OrderDataGeneration orderDataGeneration = OrderDataGeneration.getEmptyIngredients();

        OrderService.createOrder(orderDataGeneration, accessToken)
                .then().assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Проверка создания заказа без авторизации без игредиентов")
    public void checkCreateOrderWithoutAuthWithoutIngr(){
        OrderDataGeneration orderDataGeneration = OrderDataGeneration.getEmptyIngredients();
        OrderService.createWithoutAuth(orderDataGeneration)
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Проверка создания заказа с авторизацией с неверным хэшем")
    public void checkCreateOrderWithAuthHashIsNotCorrect(){
        OrderDataGeneration orderDataGeneration = OrderDataGeneration.getInstanceHashIsNotCorrect();

        OrderService.createOrder(orderDataGeneration, accessToken)
                .then().assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Проверка создания заказа без авторизации с неверным хэшем")
    public void checkCreateOrderWithoutAuthHashIsNotCorrect(){
        OrderDataGeneration orderDataGeneration = OrderDataGeneration.getInstanceHashIsNotCorrect();
        OrderService.createWithoutAuth(orderDataGeneration)
                .then()
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}

