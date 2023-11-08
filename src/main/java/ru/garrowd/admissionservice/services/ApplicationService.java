package ru.garrowd.admissionservice.services;

import com.university.userservice.grpc.user.UserGrpcServiceGrpc;
import com.university.userservice.grpc.user.UserRequest;
import com.university.userservice.grpc.user.UserRequestWithDir;
import com.university.userservice.grpc.user.WorkExperiences;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.garrowd.admissionservice.models.Application;
import ru.garrowd.admissionservice.models.WorkExperience;
import ru.garrowd.admissionservice.repositories.ApplicationRepository;
import ru.garrowd.admissionservice.services.validators.ValidationService;
import ru.garrowd.admissionservice.utils.enums.ApplicationStatuses;
import ru.garrowd.admissionservice.utils.enums.ExceptionMessages;
import ru.garrowd.admissionservice.utils.exceptions.ApplicationStatusException;
import ru.garrowd.admissionservice.utils.exceptions.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ApplicationService {
    private final ExceptionMessagesService exceptionMessages;
    private final ApplicationRepository applicationRepository;

    private final ValidationService validationService;
    private final ModelMapper modelMapper;

    private final UserGrpcServiceGrpc.UserGrpcServiceBlockingStub stub;

    public Application getById(String id)
            throws NotFoundException {
        Optional<Application> foundApplication = applicationRepository.findById(id);

        return foundApplication.orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.APPLICATION_NOT_FOUND, id.toString())));
    }

    public void existsById(String id)
            throws NotFoundException {
        applicationRepository.findById(id).orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.APPLICATION_NOT_FOUND, id.toString())));
    }

    public Page<Application> getAllByUserIdOrderByDate(String userId, Pageable pageable) {
        return applicationRepository.findAllByUserIdOrderByDate(userId, pageable);
    }

    public Page<Application> getAllByDepartmentAndFullName(String fullName, String department, Pageable pageable) {
        return applicationRepository.getAllByDepartmentAndFullNameIgnoreCaseStartingWithAndStatusIsOrderByDate(fullName,
                                                                                                               department,
                                                                                                               ApplicationStatuses.PENDING,
                                                                                                               pageable);
    }

    public Page<Application> getAllByFullNameOrderByDate(String fullName, Pageable pageable) {
        return applicationRepository.findAllByFullNameIgnoreCaseStartingWithAndStatusIsOrderByDate(fullName,
                                                                                                   ApplicationStatuses.WAITING,
                                                                                                   pageable);
    }

    @Transactional
    public void approveApplicationBySupervisor(String department, String id) {
        processApplicationStatusChange(department, id, ApplicationStatuses.WAITING);
    }

    @Transactional
    public void rejectApplicationBySupervisor(String department, String id) {
        stub.setEmployeeStatus(UserRequest.newBuilder().setUserId(processApplicationStatusChange(department, id, ApplicationStatuses.REJECTED_BY_SUPERVISOR)).build());
    }

    private String processApplicationStatusChange(
            String department, String id, ApplicationStatuses newStatus) {
        Application application = getById(id);

        if(! department.equals(application.getDepartment())) {
            throw new ApplicationStatusException(
                    exceptionMessages.getMessage(ExceptionMessages.APPLICATION_AND_SUPERVISOR_DEPARTMENT_NOT_EQUALS));
        }

        if(application.getStatus().equals(ApplicationStatuses.PENDING)) {
            application.setStatus(newStatus);
            applicationRepository.save(application);

            log.info("Application with ID {} is updated with status {} by supervisor", application.getId(), newStatus.toString());

            return application.getUserId();
        }
        else {
            throw new ApplicationStatusException(
                    exceptionMessages.getMessage(ExceptionMessages.APPLICATION_NOT_PENDING));
        }
    }

    public void approveApplicationByAdmission(String id) {
        stub.setStudentStatus(UserRequest.newBuilder().setUserId(processApplicationStatusChange(id, ApplicationStatuses.APPROVED)).build());
    }

    @Transactional
    public void rejectApplicationByAdmission(String id) {
        stub.setEmployeeStatus(UserRequest.newBuilder().setUserId(processApplicationStatusChange(id, ApplicationStatuses.REJECTED_BY_ADMISSION)).build());
    }

    private String processApplicationStatusChange(String id, ApplicationStatuses newStatus) {
        Application application = getById(id);

        if(application.getStatus().equals(ApplicationStatuses.WAITING)) {
            application.setStatus(newStatus);
            applicationRepository.save(application);

            log.info("Application with ID {} is updated with status {} by admission committee", application.getId(), newStatus.toString());

            return application.getUserId();
        }
        else {
            throw new ApplicationStatusException(
                    exceptionMessages.getMessage(ExceptionMessages.APPLICATION_NOT_WAITING));
        }
    }

    @Transactional
    public Application create(Application application) {
        application.setDate(LocalDateTime.now());
        application.setStatus(ApplicationStatuses.PENDING);

        // TODO Желательно проверить есть ли такой direction вообще по gRpc
        //  И вообще реализация странная, нет?

        WorkExperiences workExperiences = stub.getWorkExperiencesById(UserRequest.newBuilder().setUserId(application.getUserId()).build());

        if (workExperiences != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            List<ru.garrowd.admissionservice.models.WorkExperience> workExperienceList =
                    workExperiences.getWorkExperiencesList().stream().map(
                            workExperience -> WorkExperience
                                    .builder().departmentName(workExperience.getDepartmentName())
                                    .startDate(LocalDate.parse(workExperience.getStartDate(), formatter))
                                    .endDate(LocalDate.parse(workExperience.getEndDate(), formatter))
                                    .description(workExperience.getDescription())
                                    .application(application)
                                    .build()).toList();

            application.setWorkExperiences(workExperienceList);
        }

        applicationRepository.save(application);

        stub.setEnrolleeStatus(UserRequestWithDir.newBuilder().setUserId(application.getUserId()).setDirectionTitle(application.getDirection()).build());

        log.info("Application with ID {} is created", application.getId());

        return application;
    }

    // TODO Как правильно делается редактирование?
    @Transactional
    public Application update(Application unsavedApplication, String id) {
        Application application = Application.builder().build();
        modelMapper.map(getById(id), application);
        modelMapper.map(unsavedApplication, application);
        validationService.validate(application);
        applicationRepository.save(application);

        log.info("Application with ID {} is updated", application.getId());

        return application;
    }

    @Transactional
    public void updateWithoutChecks(Application application) {
        existsById(application.getId());
        applicationRepository.save(application);

        log.info("Application with ID {} is updated", application.getId());
    }
}
