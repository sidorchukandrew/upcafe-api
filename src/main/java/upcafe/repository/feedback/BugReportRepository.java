package upcafe.repository.feedback;

import org.springframework.data.repository.CrudRepository;
import upcafe.entity.feedback.Bug;

public interface BugReportRepository extends CrudRepository<Bug, Integer> {
}
