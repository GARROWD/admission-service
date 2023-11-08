package ru.garrowd.admissionservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "work_experiences")
public class WorkExperience {
    @Id
    @Column(name = "id", unique = true, length = 40)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "department_mame", nullable = false, length = 200)
    private String departmentName;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @ManyToOne
    @JoinColumn(name = "application_id")
    @JsonIgnore
    private Application application;
}
