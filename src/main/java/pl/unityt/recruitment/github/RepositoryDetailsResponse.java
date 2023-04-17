package pl.unityt.recruitment.github;

import pl.unityt.recruitment.github.client.RepositoryDetails;

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
