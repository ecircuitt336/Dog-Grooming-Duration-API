package com.example.dog_grooming_duration_api.repositories;

import com.example.dog_grooming_duration_api.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByActualDurationMinutesIsNotNull();

    @Query("""
            SELECT
                b.code AS breed,
                a.weightKg AS weightKg,
                a.coatLength AS coatLength,
                a.coatTexture AS coatTexture,
                a.coatStructure AS coatStructure,
                a.mattingSeverity AS mattingSeverity,
                a.behaviour AS behaviour,
                a.service AS service,
                a.groomerExperienceYears AS groomerExperienceYears,
                a.actualDurationMinutes AS actualDurationMinutes
            FROM Appointment a
            JOIN a.breed b
            WHERE a.actualDurationMinutes IS NOT NULL
            """)
    List<CompletedAppointmentProjection> findCompletedAppointmentsForMl();
}
