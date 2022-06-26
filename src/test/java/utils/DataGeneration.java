package utils;

import com.github.javafaker.Faker;
import dto.userDto.UserDto;

public class DataGeneration {
    static Faker faker = new Faker();

    public static UserDto generateNewUser() {
        String name = faker.name().name();
        String email = faker.name().username() + "@mail.ru";
        String password = faker.internet().password();
        return new UserDto(email, password, name);
    }

    public static UserDto generateLoginUser() {
        String email = faker.name().username() + "@mail.ru";
        String password = faker.internet().password();
        return new UserDto(email, password, null);
    }
}
