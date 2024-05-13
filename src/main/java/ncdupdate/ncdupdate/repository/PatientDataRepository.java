package ncdupdate.ncdupdate.repository;

import ncdupdate.ncdupdate.model.PatientData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface PatientDataRepository extends JpaRepository<PatientData, Long> {
    //Using JPA Entity
   // @Query(value="SELECT * FROM integrator_client_intake s WHERE s.client_intake_id=?1 AND encounter_date=?2",nativeQuery = true)
  Optional<PatientData> findByIntegratorIdAndEncounterDate (int integratorId, java.sql.Date encounterDate);


}
