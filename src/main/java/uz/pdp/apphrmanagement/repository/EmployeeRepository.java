package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.apphrmanagement.entity.Employee;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    boolean existsByEmail(String email);

    Optional<Employee> findByEmailAndEmailCode(String email, String emailCode);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByEmailAndEnabledTrue(String email);

    @Query(nativeQuery = true, value = "select count(*)>0 from employee t\n" +
            "join employee_role er on t.id = er.employee_id\n" +
            "where role_id = 3 and email =:email")
    boolean isManager(String email);

    @Query(nativeQuery = true, value = "select count(*)>0 from employee t\n" +
            "join employee_role er on t.id = er.employee_id\n" +
            "where role_id = 4 and email =:email")
    boolean isWorker(String email);

    List<Employee> findAllByCompanyIdAndEnabledTrue(Integer company_id);

    @Query(nativeQuery = true, value = "select * from employee t\n" +
            "join company c on c.id = t.company_id\n" +
            "join employee_role er on t.id = er.employee_id\n" +
            "where er.role_id = 2 and company_id =:company_id")
    Optional<Employee> findCompanyDirector(Integer company_id);

    @Query(nativeQuery = true, value = "select count(*) > 0\n" +
            "from employee_role t\n" +
            "         join employee e on e.id = t.employee_id\n" +
            "where role_id = 2\n" +
            "  and e.email =:email")
    boolean isDirector(String email);


}
