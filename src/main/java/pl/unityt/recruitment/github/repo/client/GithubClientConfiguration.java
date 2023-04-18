package pl.unityt.recruitment.github.repo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Configuration
public class GithubClientConfiguration {

    public static final String ERROR_MESSAGE_TEMPLATE = "Github API responded with http code {%s}";

    @Value("${integration.github.api.url}")
    private String githubApiUrl;

    @Value("${integration.github.api.version.value}")
    private String githubApiVersionValue;

    @Value("${integration.github.api.version.key}")
    private String githubApiVersionKey;


    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl(githubApiUrl)
                .defaultHeader(githubApiVersionKey, githubApiVersionValue)
                .defaultStatusHandler(HttpStatusCode::is3xxRedirection, this::mapRedirection)
                .defaultStatusHandler(HttpStatusCode::isError, this::mapClientError)
                .build();
    }

    @Bean
    GithubClient postClient() {
        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient()))
                        .build();
        return httpServiceProxyFactory.createClient(GithubClient.class);
    }

    private Mono<Throwable> mapClientError(ClientResponse response) {
        if (response.statusCode().is5xxServerError()) {
            return Mono.just(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, String.format(ERROR_MESSAGE_TEMPLATE, response.statusCode())));
        }
        return Mono.just(new ResponseStatusException(response.statusCode(), String.format(ERROR_MESSAGE_TEMPLATE, response.statusCode())));
    }

    private Mono<Throwable> mapRedirection(ClientResponse response) {
        return Mono.just(new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(ERROR_MESSAGE_TEMPLATE, response.statusCode())));
    }
}