package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagement.entity.Employee;
import uz.pdp.apphrmanagement.entity.Role;
import uz.pdp.apphrmanagement.entity.enums.RoleEnum;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleEnum(RoleEnum roleEnum);
}
