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

    @PostMapping(path = "/features")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    public FeatureDTO saveNewFeatureRequest(@RequestBody FeatureDTO featureRequest) {
        return feedbackService.saveFeatureRequest(featureRequest);
    }

    @PostMapping(path = "/bugs")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    public BugDTO saveNewBugReport(@RequestBody BugDTO bugReport) {
        return feedbackService.saveBugReport(bugReport);
    }

}
