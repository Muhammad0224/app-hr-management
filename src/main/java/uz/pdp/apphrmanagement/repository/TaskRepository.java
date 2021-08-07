package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagement.entity.Task;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByTaskCode(String taskCode);

    List<Task> findAllByCompletedAtBetweenAndEmployee_Email(Timestamp completedAt, Timestamp completedAt2, String employee_email);

    List<Task> findAllByDeadlineBeforeAndEmployee_EmailAndStatus(Date deadline, String employee_email, Integer status);

    List<Task> findAllByCreatedAtBetweenAndStatusAndEmployee_Email(Timestamp createdAt, Timestamp createdAt2, Integer status, String employee_email);
}
