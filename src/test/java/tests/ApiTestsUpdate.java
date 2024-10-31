package tests;

import io.restassured.response.Response;
import models.lombok.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.baseRequestSpec;
import static specs.BaseSpec.baseResponseSpec;


public class ApiTestsUpdate extends TestBase {

    @Test
    @Tag("api")
    void unsuccessfulLoginTest() {
        Response responseModel = step("Unsuccessful login without any data", () ->
                given(baseRequestSpec)

                        .when()
                        .post("/login")

                        .then()
                        .spec(baseResponseSpec)
                        .extract().response());
        step("Check 400 status code", () ->
                assertThat(responseModel.statusCode()).isEqualTo(400));
    }

    @Test
    @Tag("api")
    void successfulLoginWithSpecsTest() {
        LoginBodyLombokModel authData = new LoginBodyLombokModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        Response response = step("Login request", () ->
                given(baseRequestSpec)
                        .body(authData)

                        .when()
                        .post("/login")

                        .then()
                        .spec(baseResponseSpec)
                        .extract().response());

        LoginResponseLombokModel responseModel = response.as(LoginResponseLombokModel.class);

        step("Check response", () -> {
            assertThat(response.statusCode()).isEqualTo(200);
            assertThat(responseModel.getToken()).isNotNull();
        });
    }

    @Test
    @Tag("api")
    void checkListUsersOnPage2Test() {

        UserListResponseModel responseModel = step("Check list user on second page", () ->
                given(baseRequestSpec)
                        .queryParam("page", "2")
                        .when()
                        .get("/users")
                        .then()
                        .spec(baseResponseSpec)
                        .extract().as(UserListResponseModel.class));

        step("Check response", () -> {
            assertThat(responseModel.getPage()).isEqualTo(2);
            assertThat(responseModel.getPer_page()).isEqualTo(6);
            assertThat(responseModel.getTotal()).isEqualTo(12);
            assertThat(responseModel.getSupport().getUrl())
                    .isEqualTo("https://reqres.in/#support-heading");
            assertThat(responseModel.getSupport().getText())
                    .isEqualTo("To keep ReqRes free, contributions towards server costs are appreciated!");

            List<String> expectedEmails = List.of(
                    "michael.lawson@reqres.in",
                    "lindsay.ferguson@reqres.in",
                    "tobias.funke@reqres.in",
                    "byron.fields@reqres.in",
                    "george.edwards@reqres.in",
                    "rachel.howell@reqres.in"
            );
            List<String> actualEmail = responseModel.getData().stream()
                    .map(UserListResponseModel.UserData::getEmail)
                    .toList();

            assertThat(actualEmail).containsAnyElementsOf(expectedEmails);

        });
    }

    @Test
    @Tag("api")
    void updateUser2Test() {
        UpdateUserLombokModel updateUser = new UpdateUserLombokModel();
        updateUser.setName("morpheus");
        updateUser.setJob("zion resident");


        UpdateUserResponseModel responseModel = step("Update user", () ->
                given(baseRequestSpec)
                        .body(updateUser)
                        .when()
                        .put("/users/2")
                        .then()
                        .spec(baseResponseSpec)
                        .extract().as(UpdateUserResponseModel.class));

        step("Check User name and job after update", () -> {
            assertThat(responseModel.getName()).isEqualTo("morpheus");
            assertThat(responseModel.getJob()).isEqualTo("zion resident");
        });
    }

    @Test
    @Tag("api")
    void deleteUser3Test() {

        Response response = step("Delete User", () ->
                given(baseRequestSpec)

                        .when()
                        .delete("/users/3")

                        .then()
                        .spec(baseResponseSpec)
                        .extract().response());
        step("Check status code after delete user", () -> {
            assertThat(response.statusCode()).isEqualTo(204);
        });
    }

    @Test
    @Tag("api")
    void createUserTest() {

        CreateUserLombokModel createUser = new CreateUserLombokModel();
        createUser.setName("morpheus");
        createUser.setJob("leader");

        CreateUserResponseModel responseModel = step("Create user", () ->

                given(baseRequestSpec)
                        .body(createUser)

                        .when()
                        .post("/users")

                        .then()
                        .spec(baseResponseSpec)
                        .extract().as(CreateUserResponseModel.class));

        step("Check User name and job after create", () -> {
            assertThat(responseModel.getName()).isEqualTo("morpheus");
            assertThat(responseModel.getJob()).isEqualTo("leader");
        });

    }
}

