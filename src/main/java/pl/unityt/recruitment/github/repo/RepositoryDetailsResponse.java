package pl.unityt.recruitment.github.repo;

import pl.unityt.recruitment.github.repo.client.RepositoryDetails;

import java.time.Instant;

public record RepositoryDetailsResponse(
        String fullName,
        String description,
        String cloneUrl,
        int stars,
        Instant createdAt
) {

    public static RepositoryDetailsResponse fromGithubResponse(RepositoryDetails details) {
        return new RepositoryDetailsResponse(
                details.fullName(),
                details.description(),
                details.cloneUrl(),
                details.stars(),
                details.createdAt()
        );
    }
}
