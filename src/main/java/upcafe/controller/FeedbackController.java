package upcafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upcafe.dto.feedback.BugDTO;
import upcafe.dto.feedback.FeatureDTO;
import upcafe.service.FeedbackService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
@CrossOrigin(origins = "*")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping(path = "/features")
    @PreAuthorize("hasRole('ADMIN')")
    public List<FeatureDTO> getFeatureRequests() {
        return feedbackService.getAllFeatureRequests();
    }

    @GetMapping(path = "/features/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void getFeatureRequestById(@PathVariable(name = "id") int id) {

    }

    @PostMapping(path = "/features")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    public FeatureDTO saveNewFeatureRequest(@RequestBody FeatureDTO featureRequest) {
        return  feedbackService.saveFeatureRequest(featureRequest);
    }

    @PutMapping(path = "/features/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateFeatureRequestById(@PathVariable(name = "id") int id, @RequestBody FeatureDTO featureRequest) {

    }

    @GetMapping(path = "/bugs")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BugDTO> getBugs() {
        return feedbackService.getAllBugsReported();
    }

    @GetMapping(path = "/bugs/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void getBugById(@PathVariable(name = "id") int id) {

    }

    @PostMapping(path = "/bugs")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    public void saveNewBugReport(@RequestBody BugDTO bugReport) {
        System.out.println(bugReport);
    }

    @PutMapping(path = "/bugs/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateBugById(@PathVariable(name = "id") int id, @RequestBody BugDTO bugReport) {

    }

}
