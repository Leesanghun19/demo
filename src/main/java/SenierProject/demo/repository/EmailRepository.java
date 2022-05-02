package SenierProject.demo.repository;

import SenierProject.demo.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email,Long> {
    Optional<Email> findByEmail(String email);
}