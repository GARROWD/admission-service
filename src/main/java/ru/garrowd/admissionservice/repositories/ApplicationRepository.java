package ru.garrowd.admissionservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.garrowd.admissionservice.models.Application;
import ru.garrowd.admissionservice.utils.enums.ApplicationStatuses;

@Repository
public interface ApplicationRepository
        extends JpaRepository<Application, String> {
    Page<Application> findAllByUserIdOrderByDate(String userId, Pageable pageable);

    Page<Application> getAllByDepartmentAndFullNameIgnoreCaseStartingWithAndStatusIsOrderByDate(
            String fullName, String department, ApplicationStatuses status, Pageable pageable);

    Page<Application> findAllByFullNameIgnoreCaseStartingWithAndStatusIsOrderByDate(String fullName, ApplicationStatuses status, Pageable pageable);
}
