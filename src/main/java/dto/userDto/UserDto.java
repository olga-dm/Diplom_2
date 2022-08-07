package dto.userDto;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonCreator;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;

public class UserDto {

    private String email;
    private String password;
    private String name;

    @JsonCreator
    public UserDto(@JsonProperty(value = "email", required = true) String email,
                   @JsonProperty(value = "password", required = false) String password,
                   @JsonProperty(value = "name", required = true) String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }


    public UserDto() {
    }

    public UserDto setEmailAndNameOnly(String email, String name) {
        this.email = email;
        this.name = name;
        return this;
    }

    public UserDto setEmailAndPasswordOnly(String email, String password) {
        this.email = email;
        this.password = password;
        return this;
    }

    public UserDto setWithNameAndPasswordOnly(String name, String password) {
        this.name = name;
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
