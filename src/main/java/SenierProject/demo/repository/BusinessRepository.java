package SenierProject.demo.repository;

import SenierProject.demo.domain.Business;
import SenierProject.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business,Long> {
    Optional<Business> findByEmail(String email);
}
