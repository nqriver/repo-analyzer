package pl.unityt.recruitment.github.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.spec.internal.HttpStatus;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 7070)
@ActiveProfiles("test")
class RepositoryControllerTest {

    public static final String GITHUB_API_200_RESPONSE_PATH = "classpath:mock-responses/response200.json";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void shouldReturnRepositoryDetails() throws Exception {
        // given
        String userUnderTest = "user";
        String repoUnderTest = "repo";

        givenExistingGithubRepositoryOf(userUnderTest, repoUnderTest);


        // when
        String jsonResponse = mockMvc.perform(MockMvcRequestBuilders.get("/repositories/{user}/{repo}", userUnderTest, repoUnderTest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        assertResponseMatches(jsonResponse);
    }

    @Test
    public void shouldReturn404_whenRepositoryDoesNotExist() throws Exception {

        String userUnderTest = "user";
        String repoUnderTest = "repo";

        givenNotExistingGithubRepositoryOf(userUnderTest, repoUnderTest);


        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/repositories/{user}/{repo}", userUnderTest, repoUnderTest))

                // then
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturn501_whenRemoteGithubApiRespondsWith5xx() throws Exception {

        String userUnderTest = "user";
        String repoUnderTest = "repo";

        givenClientErrorOnGithubApiCallFor(userUnderTest, repoUnderTest);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/repositories/{user}/{repo}", userUnderTest, repoUnderTest))

                // then
                .andExpect(MockMvcResultMatchers.status().isServiceUnavailable());
    }

    @Test
    public void shouldTrigger403_whenTryingToAccessPrivateRepository() throws Exception {

        String userUnderTest = "user";
        String repoUnderTest = "repo";

        givenUnauthorizedAccessToGithubRepositoryOf(userUnderTest, repoUnderTest);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/repositories/{user}/{repo}", userUnderTest, repoUnderTest))

                // then
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void shouldTrigger400_whenRepositoryIsMovedPermanently() throws Exception {

        String userUnderTest = "user";
        String repoUnderTest = "repo";

        givenMovedRepositoryOf(userUnderTest, repoUnderTest);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/repositories/{user}/{repo}", userUnderTest, repoUnderTest))

                // then
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private void assertResponseMatches(String jsonResponse) throws JsonProcessingException {
        RepositoryDetailsResponse actualResponse = objectMapper.readValue(jsonResponse, RepositoryDetailsResponse.class);

        RepositoryDetailsResponse expectedResponse = new RepositoryDetailsResponse("nqriver/TextStats",
                "Command Line Tool created for text analysis and basic statistics generation.",
                "https://github.com/nqriver/TextStats.git",
                0,
                Instant.parse("2021-06-09T22:50:49Z"));

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    private void givenExistingGithubRepositoryOf(String userUnderTest, String repoUnderTest) {
        stubFor(get(urlEqualTo(String.format("/repos/%s/%s", userUnderTest, repoUnderTest)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json").withBody(getSample200JsonResponse())));
    }

    private void givenNotExistingGithubRepositoryOf(String userUnderTest, String repoUnderTest) {
        givenResponseOfStatusOnGetRepository(userUnderTest, repoUnderTest, HttpStatus.NOT_FOUND);
    }

    private void givenClientErrorOnGithubApiCallFor(String userUnderTest, String repoUnderTest) {
        givenResponseOfStatusOnGetRepository(userUnderTest, repoUnderTest, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void givenUnauthorizedAccessToGithubRepositoryOf(String userUnderTest, String repoUnderTest) {
        givenResponseOfStatusOnGetRepository(userUnderTest, repoUnderTest, HttpStatus.FORBIDDEN);
    }

    private void givenMovedRepositoryOf(String userUnderTest, String repoUnderTest) {
        givenResponseOfStatusOnGetRepository(userUnderTest, repoUnderTest, HttpStatus.MOVED_PERMANENTLY);
    }

    private void givenResponseOfStatusOnGetRepository(String user, String repository, int httpStatus) {
        stubFor(get(urlEqualTo(String.format("/repos/%s/%s", user, repository)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json").withStatus(httpStatus)));
    }


    private String getSample200JsonResponse() {
        try {
            return resourceLoader.getResource(GITHUB_API_200_RESPONSE_PATH).getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}