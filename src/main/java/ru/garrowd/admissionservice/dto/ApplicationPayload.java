package ru.garrowd.admissionservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.garrowd.admissionservice.models.WorkExperience;
import ru.garrowd.admissionservice.utils.enums.ApplicationStatuses;

import java.time.LocalDateTime;
import java.util.List;

public class ApplicationPayload {
    @Schema(name = "ApplicationPayloadCreate")
    @Data
    public static class Create {
        @Size(max = 200, message = "application.error.supervisorFullNameLength")
        @NotNull(message = "application.error.nullField")
        private String supervisorFullName;

        @Size(max = 200, message = "application.error.directionLength")
        @NotNull(message = "application.error.nullField")
        private String direction;

        @NotNull(message = "application.error.nullField")
        private String letter;

        @NotNull(message = "application.error.nullField")
        private String achievements;
    }

    @Data
    public static class Update {

    }

    @Schema(name = "ApplicationPayloadRequest")
    @Data
    public static class Request {
        private String id;

        private LocalDateTime date;

        private String userId;

        private String fullName;

        private String phoneNumber;

        private String email;

        private String supervisorFullName;

        private String direction;

        private String department;

        private String position;

        private String letter;

        private String achievements;

        @Enumerated(EnumType.STRING)
        private ApplicationStatuses status;

        /*private List<WorkExperience> workExperiences;*/
    }
}

