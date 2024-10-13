import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    @Test
    void unsuccessfulLoginTest() {
        given()
                .log().uri()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(415);


    }

    @Test
    void successfulLoginTest() {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";

        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post("https://reqres.in/api/login")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void checkListUsersOnPage2Test() {
        given()
                .contentType(JSON)
                .log().uri()

                .when()
                .get("https://reqres.in/api/users?page=2")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("per_page", equalTo(6))
                .body("total", equalTo(12))
                .body("total_pages", equalTo(2))
                .body("data.id", hasItems(7, 8, 9, 10, 11, 12))
                .body("data.email", hasItems(
                        "michael.lawson@reqres.in",
                        "lindsay.ferguson@reqres.in",
                        "tobias.funke@reqres.in",
                        "byron.fields@reqres.in",
                        "george.edwards@reqres.in",
                        "rachel.howell@reqres.in"
                ))
                .body("support.url", equalTo("https://reqres.in/#support-heading"))
                .body("support.text", equalTo("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    void updateUser2Test() {
        String requestBody = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"zion resident\"\n" +
                "}";

        given()
                .contentType(JSON)
                .body(requestBody)
                .log().uri()
                .log().body()

                .when()
                .put("https://reqres.in/api/users/2")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"));
    }

    @Test
    void deleteUser3Test() {
        given()
                .log().uri()
                .log().body()

                .when()
                .delete("https://reqres.in/api/users/3")

                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }

    @Test
    void createUserTest() {
        String requestBody = "{\n" +
                "    \"name\": \"morpheuss\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";

        given()
                .contentType(JSON)
                .body(requestBody)
                .log().uri()
                .log().body()

                .when()
                .post("https://reqres.in/api/users")

                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", equalTo("morpheuss"))
                .body("job", equalTo("leader"));
    }
}

