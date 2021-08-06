package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagement.entity.Employee;
import uz.pdp.apphrmanagement.entity.Role;
import uz.pdp.apphrmanagement.entity.enums.RoleEnum;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    boolean existsByEmail(String email);

    Optional<Employee> findByEmailAndEmailCode(String email, String emailCode);

    Optional<Employee> findByEmail(String email);
}
