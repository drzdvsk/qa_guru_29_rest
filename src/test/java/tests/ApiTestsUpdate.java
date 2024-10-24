package tests;

import helpers.ApiHelper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import models.lombok.CreateUserLombokModel;
import models.lombok.UpdateUserLombokModel;
import org.junit.jupiter.api.Test;
import models.lombok.LoginBodyLombokModel;
import models.lombok.LoginResponseLombokModel;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.baseRequestSpec;
import static specs.BaseSpec.baseResponseSpec;


public class ApiTestsUpdate extends TestBase {

    @Test
    void unsuccessfulLoginTest() {
        Response response = step("Unsuccessful login without any data", () ->
                given(baseRequestSpec)

                        .when()
                        .post("/login")

                        .then()
                        .spec(baseResponseSpec)
                        .extract().response());
        step("Check 400 status code", () ->
                assertThat(response.statusCode()).isEqualTo(400));
    }

    @Test
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

        LoginResponseLombokModel loginResponse = response.as(LoginResponseLombokModel.class);

        step("Check response", () -> {
            assertThat(response.statusCode()).isEqualTo(200);
            assertThat(loginResponse.getToken()).isNotNull();
        });
    }

    @Test
    void checkListUsersOnPage2Test() {

        Response response = step("Check list user on second page", () ->
                given(baseRequestSpec)
                        .queryParam("page", "2")
                        .when()
                        .get("/users")
                        .then()
                        .spec(baseResponseSpec)
                        .extract().response());

        step("Check response", () -> {
            ApiHelper.checkPagination(response, 2, 6, 12, 2);
            ApiHelper.checkUserIds(response, List.of(7, 8, 9, 10, 11, 12));
            ApiHelper.checkUserEmails(response, List.of(
                    "michael.lawson@reqres.in",
                    "lindsay.ferguson@reqres.in",
                    "tobias.funke@reqres.in",
                    "byron.fields@reqres.in",
                    "george.edwards@reqres.in",
                    "rachel.howell@reqres.in"
            ));
            ApiHelper.checkSupportInfo(response,
                    "https://reqres.in/#support-heading",
                    "To keep ReqRes free, contributions towards server costs are appreciated!");
        });
    }

    @Test
    void updateUser2Test() {
        UpdateUserLombokModel updateUser = new UpdateUserLombokModel();
        updateUser.setName("morpheus");
        updateUser.setJob("zion resident");

        Response response = step("Update user", () ->
                given(baseRequestSpec)
                        .body(updateUser)
                        .when()
                        .put("/users/2")
                        .then()
                        .spec(baseResponseSpec)
                        .extract().response());

        step("Check User name and job after update", () -> {
            String name = response.path("name");
            String job = response.path("job");

            assertThat(name).isEqualTo("morpheus");
            assertThat(job).isEqualTo("zion resident");
        });
    }

    @Test
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
    void createUserTest() {

        CreateUserLombokModel createUser = new CreateUserLombokModel();
        createUser.setName("morpheus");
        createUser.setJob("leader");

        Response response = step("Create user", () ->

                given(baseRequestSpec)
                        .body(createUser)

                        .when()
                        .post("/users")

                        .then()
                        .spec(baseResponseSpec)
                        .extract().response());

        step("Check User name and job after create", () -> {
            String name = response.path("name");
            String job = response.path("job");

            assertThat(name).isEqualTo("morpheus");
            assertThat(job).isEqualTo("leader");
        });

    }
}

