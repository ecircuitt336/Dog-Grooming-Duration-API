package com.example.dog_grooming_duration_api.repositories;

import com.example.dog_grooming_duration_api.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * This class represents the database-access interface for Appointment objects.
 * It gives the application a way to save, find, and query appointments.
 */
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    // This is a derived query, meaning Spring reads the method name and works out the query automatically.
    // "Find all appointments where actualDurationMinutes is not null."
    List<Appointment> findByActualDurationMinutesIsNotNull();

    // Custom JPQL query.
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

    // Return only the data wanted as defined in CompletedAppointmentProjection.
    List<CompletedAppointmentProjection> findCompletedAppointmentsForMl();
}
