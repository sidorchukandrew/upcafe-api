package upcafe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upcafe.dto.feedback.BugDTO;
import upcafe.dto.feedback.FeatureDTO;
import upcafe.dto.users.UserDTO;
import upcafe.entity.signin.User;
import upcafe.repository.feedback.BugReportRepository;
import upcafe.repository.feedback.FeatureRequestRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private BugReportRepository bugRepo;

    @Autowired
    private FeatureRequestRepository featureRepo;

    public List<BugDTO> getAllBugsReported() {
        List<BugDTO> bugsReported = new ArrayList<>();

        bugRepo.findAll().forEach(bug ->
            bugsReported.add(new BugDTO.Builder()
                    .id(bug.getId())
                    .actual(bug.getActual())
                    .expectation(bug.getExpectation())
                    .extraInformation(bug.getExtraInformation())
                    .browser(bug.getBrowser())
                    .platform(bug.getPlatform())
                    .dateReported(bug.getDateReported())
                    .page(bug.getPage())
                    .reporter(transferToUserDTO(bug.getReporter()))
                    .build())
        );

        return bugsReported;
    }

    private UserDTO transferToUserDTO(User user) {
        return new UserDTO.Builder(user.getEmail())
                .id(user.getId())
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .build();
    }

    public List<FeatureDTO> getAllFeatureRequests() {
        List<FeatureDTO> featuresRequested = new ArrayList<>();

        featureRepo.findAll().forEach(request ->
           featuresRequested.add(new FeatureDTO.Builder()
                   .dateReported(request.getDateReported())
                   .description(request.getDescription())
                   .id(request.getId())
                   .page(request.getPage())
                   .reporter(transferToUserDTO(request.getReporter()))
                   .build())
        );

        return featuresRequested;
    }
}
