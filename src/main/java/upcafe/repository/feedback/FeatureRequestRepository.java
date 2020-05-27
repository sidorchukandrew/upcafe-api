package upcafe.repository.feedback;

import org.springframework.data.repository.CrudRepository;
import upcafe.entity.feedback.FeatureRequest;

public interface FeatureRequestRepository extends CrudRepository<FeatureRequest, Integer> {
}
