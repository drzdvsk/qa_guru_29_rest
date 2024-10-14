import org.junit.jupiter.api.BeforeAll;
import io.restassured.RestAssured;

public class TestBase {
    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }
}