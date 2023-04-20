package pl.unityt.recruitment.github.repo;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class RepositoryController {

    private final RepositoryFacade repositoryFacade;

    public RepositoryController(RepositoryFacade repositoryFacade) {
        this.repositoryFacade = repositoryFacade;
    }

    @GetMapping("/repositories/{owner}/{repository-name}")
    ResponseEntity<RepositoryDetailsResponse> getRepositoryDetails(@PathVariable("owner") @NotBlank String owner,
                                                                   @PathVariable("repository-name") @NotBlank String repositoryName) {
        return ResponseEntity.ok(repositoryFacade.getDetails(owner, repositoryName));
    }
}
