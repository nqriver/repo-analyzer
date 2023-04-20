package pl.unityt.recruitment.github.repo.client;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "integration.github.api")
@ConfigurationPropertiesScan
@Validated
record GithubIntegrationProperties(
        @NotBlank(message = "Github api url cannot be empty") String url,
        VersionProperties version) {

    String versionKey() {
        return version().key();
    }

    String versionValue() {
        return version().value();
    }

    static record VersionProperties(@NotBlank(message = "Github api version key cannot be empty") String key,
                                    @NotBlank (message = "Github api version value cannot be empty") String value) {
    }
}