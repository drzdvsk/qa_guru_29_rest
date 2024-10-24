package helpers;

import io.restassured.response.Response;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiHelper {

    public static void checkPagination(Response response, int expectedPage, int expectedPerPage, int expectedTotal, int expectedTotalPages) {
        assertThat((int) response.path("page")).isEqualTo(expectedPage);
        assertThat((int) response.path("per_page")).isEqualTo(expectedPerPage);
        assertThat((int) response.path("total")).isEqualTo(expectedTotal);
        assertThat((int) response.path("total_pages")).isEqualTo(expectedTotalPages);
    }

    public static void checkUserIds(Response response, List<Integer> expectedIds) {
        List<Integer> actualIds = response.path("data.id");
        assertThat(actualIds).containsExactlyElementsOf(expectedIds);
    }

    public static void checkUserEmails(Response response, List<String> expectedEmails) {
        List<String> actualEmails = response.path("data.email");
        assertThat(actualEmails).containsExactlyElementsOf(expectedEmails);
    }

    public static void checkSupportInfo(Response response, String expectedUrl, String expectedText) {
        assertThat((String) response.path("support.url")).isEqualTo(expectedUrl);
        assertThat((String) response.path("support.text")).isEqualTo(expectedText);
    }
}

