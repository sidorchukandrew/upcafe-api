package upcafe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upcafe.dto.feedback.BugDTO;
import upcafe.dto.feedback.FeatureDTO;
import upcafe.dto.users.UserDTO;
import upcafe.entity.feedback.Bug;
import upcafe.entity.feedback.FeatureRequest;
import upcafe.entity.signin.User;
import upcafe.error.MissingParameterException;
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

    private User transferToUserEntity(UserDTO user) {
        return new User.Builder(user.getEmail())
                .id(user.getId())
                .build();
    }

    public FeatureDTO saveFeatureRequest(FeatureDTO request) {

        if(request.getDescription() == null || request.getDescription().length() <= 1)
            throw new MissingParameterException("description");

        FeatureRequest savedRequest = featureRepo.save(new FeatureRequest.Builder()
                .dateReported(request.getDateReported())
                .page(request.getPage())
                .reporter(transferToUserEntity(request.getReporter()))
                .description(request.getDescription())
                .build());

        return new FeatureDTO.Builder()
                .reporter(transferToUserDTO(savedRequest.getReporter()))
                .page(savedRequest.getPage())
                .id(savedRequest.getId())
                .description(savedRequest.getDescription())
                .dateReported(savedRequest.getDateReported())
                .build();
    }

    public BugDTO saveBugReport(BugDTO bug) {

        if(bug.getActual() == null || bug.getActual().length() <= 1)
            throw new MissingParameterException("actual");

        Bug savedBug = bugRepo.save(new Bug.Builder()
                .browser(bug.getBrowser())
                .platform(bug.getPlatform())
                .actual(bug.getActual())
                .dateReported(bug.getDateReported())
                .expectation(bug.getExpectation())
                .reporter(transferToUserEntity(bug.getReporter()))
                .extraInformation(bug.getExtraInformation())
                .page(bug.getPage())
                .build());

        return new BugDTO.Builder()
                .reporter(transferToUserDTO(savedBug.getReporter()))
                .page(savedBug.getPage())
                .dateReported(savedBug.getDateReported())
                .platform(savedBug.getPlatform())
                .actual(savedBug.getActual())
                .expectation(savedBug.getExpectation())
                .extraInformation(savedBug.getExtraInformation())
                .browser(savedBug.getBrowser())
                .id(savedBug.getId())
                .build();
    }
}
