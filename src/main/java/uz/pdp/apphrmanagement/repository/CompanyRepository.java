package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagement.entity.Company;

public interface CompanyRepository extends JpaRepository<Company,Integer> {
}
