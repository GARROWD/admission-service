package ru.garrowd.admissionservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.garrowd.admissionservice.utils.enums.ApplicationStatuses;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applications")
public class Application {
    @Id
    @Column(name = "id", unique = true, length = 40)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "user_id", nullable = false, length = 40)
    private String userId;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(name = "phone_number", nullable = false, length = 200)
    private String phoneNumber;

    @Column(name = "email", nullable = false, length = 200)
    private String email;

    @Column(name = "supervisor_full_name", nullable = false, length = 200)
    private String supervisorFullName;

    @Column(name = "direction", nullable = false, length = 200)
    private String direction;

    @Column(name = "department", nullable = false, length = 200)
    private String department;

    @Column(name = "position", nullable = false, length = 200)
    private String position;

    @Column(name = "letter", nullable = false, length = 2000)
    private String letter;

    @Column(name = "achievements", nullable = false, length = 2000)
    private String achievements;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 200)
    private ApplicationStatuses status;

    @OneToMany(mappedBy = "application", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<WorkExperience> workExperiences = new ArrayList<>();
}
