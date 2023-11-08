package ru.garrowd.admissionservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.garrowd.admissionservice.dto.ApplicationPayload;
import ru.garrowd.admissionservice.models.Application;
import ru.garrowd.admissionservice.services.ApplicationService;
import ru.garrowd.admissionservice.services.validators.ValidationService;
import ru.garrowd.admissionservice.utils.Token;
import ru.garrowd.admissionservice.utils.enums.JwtClaims;

@PreAuthorize("hasAnyRole('EMPLOYEE', 'ENROLLEE')")
@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
@Slf4j
public class Controller {
    private final ApplicationService applicationService;
    private final ModelMapper modelMapper;

    private final ValidationService validationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Application> getAllApplications(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            JwtAuthenticationToken token) {
        // TODO Нужно поменять с ObjectId а String
        return applicationService.getAllByUserIdOrderByDate(Token.get(token, JwtClaims.SUB), PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Application getApplication(
            @PathVariable(name = "id") String id) {
        return applicationService.getById(id);
    }

    @GetMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public Application createApplications(
            @RequestBody ApplicationPayload.Create applicationPayload, JwtAuthenticationToken token) {
        validationService.validate(applicationPayload);

        // TODO Вообще в контроллере может быть так "много" функционала?
        Application application = modelMapper.map(applicationPayload, Application.class);
        application.setUserId(Token.get(token, JwtClaims.SUB)); // TODO Нужно поменять с ObjectId на String
        application.setFullName(Token.get(token, JwtClaims.FULL_NAME));
        application.setPhoneNumber(Token.get(token, JwtClaims.PHONE_NUMBER));
        application.setEmail(Token.get(token, JwtClaims.EMAIL));
        application.setDepartment(Token.get(token, JwtClaims.DEPARTMENT));
        application.setPosition(Token.get(token, JwtClaims.POSITION));

        return applicationService.create(application);
    }
}