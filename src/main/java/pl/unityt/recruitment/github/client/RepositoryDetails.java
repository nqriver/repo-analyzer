package pl.unityt.recruitment.github.client;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public record RepositoryDetails(
        @JsonProperty("full_name") String fullName,
        @JsonProperty("description") String description,
        @JsonProperty("clone_url") String cloneUrl,
        @JsonProperty("stargazers_count") int stars,
        @JsonProperty("created_at") Instant createdAt
) {
}
