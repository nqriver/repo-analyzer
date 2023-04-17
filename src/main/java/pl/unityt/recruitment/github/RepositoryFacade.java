package pl.unityt.recruitment.github;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.unityt.recruitment.github.client.GithubClient;
import pl.unityt.recruitment.github.client.RepositoryDetails;

@Service
public class RepositoryFacade {

    private final Logger LOG = LoggerFactory.getLogger(RepositoryFacade.class.getName());


    private final GithubClient githubClient;

    public RepositoryFacade(GithubClient githubClient) {
        this.githubClient = githubClient;
    }


    public RepositoryDetailsResponse getDetails(String owner, String repositoryName) {
        LOG.info("Fetching repository details for user {} and repository {}", owner, repositoryName);
        RepositoryDetails details = githubClient.getRepositoryDetails(owner, repositoryName);
        return RepositoryDetailsResponse.fromGithubResponse(details);
    }
}
