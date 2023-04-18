package pl.unityt.recruitment.github.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.unityt.recruitment.github.repo.client.GithubClient;
import pl.unityt.recruitment.github.repo.client.RepositoryDetails;

@Service
public class RepositoryFacade {

    private static final Logger LOG = LoggerFactory.getLogger(RepositoryFacade.class.getName());


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
