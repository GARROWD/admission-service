package ru.garrowd.admissionservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.garrowd.admissionservice.models.Application;
import ru.garrowd.admissionservice.services.ApplicationService;


@PreAuthorize("hasRole('ADMISSION')")
@RestController
@RequestMapping("/admission/applications")
@RequiredArgsConstructor
@Slf4j
public class AdmissionController {
    private final ApplicationService applicationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Application> getAllApplications(
            @RequestParam(value = "fullName", defaultValue = "") String fullName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        return applicationService.getAllByFullNameOrderByDate(fullName, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Application getApplication(
            @PathVariable(name = "id") String id) {
        return applicationService.getById(id);
    }

    @PutMapping("/{id}/approve")
    @ResponseStatus(HttpStatus.OK)
    public void approveApplicationByAdmission(@PathVariable(name = "id") String id) {
        applicationService.approveApplicationByAdmission(id);
    }

    @PutMapping("/{id}/reject")
    @ResponseStatus(HttpStatus.OK)
    public void rejectApplicationByAdmission(@PathVariable(name = "id") String id) {
        applicationService.rejectApplicationByAdmission(id);
    }
}
