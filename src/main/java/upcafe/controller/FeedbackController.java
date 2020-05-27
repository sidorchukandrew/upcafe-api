package upcafe.controller;

import org.springframework.web.bind.annotation.*;

@RestController("/api/v1")
public class FeedbackController {

    @GetMapping(path = "/features")
    public void getFeatureRequests() {

    }

    @GetMapping(path = "/features/{id}")
    public void getFeatureRequestById(@PathVariable(name = "id") int id) {

    }

    @PostMapping(path = "/features")
    public void saveNewFeatureRequest() {

    }

    @PutMapping(path = "/features/{id}")
    public void updateFeatureRequestById(@PathVariable(name = "id") int id) {

    }

    @GetMapping(path = "/bugs")
    public void getBugs() {

    }

    @GetMapping(path = "/bugs/{id}")
    public void getBugById(@PathVariable(name = "id") int id) {

    }

    @PostMapping(path = "/bugs")
    public void saveNewBugReport() {

    }

    @PutMapping(path = "/bugs/{id}")
    public void updateBugById(@PathVariable(name = "id") int id) {

    }

}
