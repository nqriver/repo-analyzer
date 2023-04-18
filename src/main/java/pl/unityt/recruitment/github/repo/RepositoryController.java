package pl.unityt.recruitment.github.repo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryController {

    private final RepositoryFacade repositoryFacade;

    public RepositoryController(RepositoryFacade repositoryFacade) {
        this.repositoryFacade = repositoryFacade;
    }

    @GetMapping("/repositories/{owner}/{repository-name}")
    ResponseEntity<RepositoryDetailsResponse> getRepositoryDetails(@PathVariable("owner") String owner,
                                                                   @PathVariable("repository-name") String repositoryName) {
        return ResponseEntity.ok(repositoryFacade.getDetails(owner, repositoryName));
    }
}
