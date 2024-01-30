package nl.helvar.servicetickets.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
    public static long assertCreatedIdAndStatusIsOk(MvcResult result) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        long createdId = jsonNode.get("id").asLong();

        assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());

        return createdId;
    }

    public static void assertCreatedIdAndLocation(MvcResult result, String url) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        long createdId = jsonNode.get("id").asLong();
        assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());
        assertThat(result.getResponse().getHeader("Location"), matchesPattern("^.*" + url + "/" + createdId));
    }

    public static void assertIsEngineerResponse(MvcResult result) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        assertEquals(jsonNode.get("engineerResponse").asBoolean(), true);
    }
}
