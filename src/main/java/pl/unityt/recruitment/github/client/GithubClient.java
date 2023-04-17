package pl.unityt.recruitment.github.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(contentType = "application/json", accept = "application/json")
public interface GithubClient {

    @GetExchange("/repos/{owner}/{repository-name}")
    RepositoryDetails getRepositoryDetails(@PathVariable("owner") String ownerUsername,
                                           @PathVariable("repository-name") String repositoryName);
}
