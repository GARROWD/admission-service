package ru.garrowd.admissionservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.garrowd.admissionservice.models.Application;
import ru.garrowd.admissionservice.services.ApplicationService;
import ru.garrowd.admissionservice.utils.Token;
import ru.garrowd.admissionservice.utils.enums.JwtClaims;


@PreAuthorize("hasRole('SUPERVISOR')")

@RestController
@RequestMapping("/supervisor/applications")
@RequiredArgsConstructor
@Slf4j
public class SupervisorController {
    private final ApplicationService applicationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Application> getAllApplications(
            @RequestParam(value = "fullName", defaultValue = "") String fullName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            JwtAuthenticationToken token) {
        return applicationService.getAllByDepartmentAndFullName(fullName, Token.get(token, JwtClaims.DEPARTMENT), PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Application getApplication(
            @PathVariable(name = "id") String id) {
        return applicationService.getById(id);
    }

    @PutMapping("/{id}/approve")
    @ResponseStatus(HttpStatus.OK)
    public void approveApplicationBySupervisor(@PathVariable(name = "id") String id, JwtAuthenticationToken token) {
        applicationService.approveApplicationBySupervisor(Token.get(token, JwtClaims.DEPARTMENT), id);
    }

    @PutMapping("/{id}/reject")
    @ResponseStatus(HttpStatus.OK)
    public void rejectApplicationBySupervisor(@PathVariable(name = "id") String id, JwtAuthenticationToken token) {
        applicationService.rejectApplicationBySupervisor(Token.get(token, JwtClaims.DEPARTMENT), id);
    }
}
