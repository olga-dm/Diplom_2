package service;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static service.RestAssured.getBaseSpec;

public class IngredientsService {
    private static final String INGR_PATH = "/api/ingredients";

    @Step
    public static Response getIngredients(){
        return given()
                .spec(getBaseSpec())
                .contentType(ContentType.JSON)
                .get(INGR_PATH);
    }
}
