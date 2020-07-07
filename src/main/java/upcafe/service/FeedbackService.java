package upcafe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upcafe.dto.feedback.BugDTO;
import upcafe.dto.feedback.FeatureDTO;
import upcafe.entity.feedback.Bug;
import upcafe.entity.feedback.FeatureRequest;
import upcafe.error.MissingParameterException;
import upcafe.error.NonExistentIdFoundException;
import upcafe.repository.feedback.BugReportRepository;
import upcafe.repository.feedback.FeatureRequestRepository;
import upcafe.utils.TransferUtils;

@Service
public class FeedbackService {

    @Autowired
    private BugReportRepository bugRepo;

    @Autowired
    private FeatureRequestRepository featureRepo;

    public FeatureDTO saveFeatureRequest(FeatureDTO request) {

    	if(request == null) throw new NonExistentIdFoundException("NULL", "Feature Request");
    	
        if (request.getDescription() == null || request.getDescription().length() <= 1)
            throw new MissingParameterException("description");

        FeatureRequest savedRequest = featureRepo.save(new FeatureRequest.Builder()
                .dateReported(request.getDateReported())
                .page(request.getPage())
                .reporter(TransferUtils.toUserEntity(request.getReporter()))
                .description(request.getDescription())
                .build());

        return new FeatureDTO.Builder()
                .reporter(TransferUtils.toUserDTO(savedRequest.getReporter()))
                .page(savedRequest.getPage())
                .id(savedRequest.getId())
                .description(savedRequest.getDescription())
                .dateReported(savedRequest.getDateReported())
                .build();
    }

    public BugDTO saveBugReport(BugDTO bug) {

    	if(bug == null) throw new NonExistentIdFoundException("NULL", "Bug");
    	
        if (bug.getActual() == null || bug.getActual().length() <= 1)
            throw new MissingParameterException("actual");

        Bug savedBug = bugRepo.save(new Bug.Builder()
                .browser(bug.getBrowser())
                .platform(bug.getPlatform())
                .actual(bug.getActual())
                .dateReported(bug.getDateReported())
                .expectation(bug.getExpectation())
                .reporter(TransferUtils.toUserEntity(bug.getReporter()))
                .extraInformation(bug.getExtraInformation())
                .page(bug.getPage())
                .build());

        return new BugDTO.Builder()
                .reporter(TransferUtils.toUserDTO(savedBug.getReporter()))
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
