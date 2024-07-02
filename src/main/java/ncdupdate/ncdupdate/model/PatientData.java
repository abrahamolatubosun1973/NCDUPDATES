package ncdupdate.ncdupdate.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "integrator_client_intake")
public class PatientData {
    @Id
    @Column(name = "client_intake_id")
    private int integratorId;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "patient_identifier")
    private String patientIdentifier;

    @Column(name = "ncd_bp_upper")
    private double ncdBpUpper;

    @Column(name = "ncd_bp_lower")
    private double ncdBpLower;

    @Column(name = "ncd_rbs")
    private double ncdRbs;

    @Column(name = "bmi_weight")
    private double bmiWeight;

    @Column(name = "bmi_height")
    private double bmiHeight;

    @Column(name = "bmi_value")
    private double bmiValue;

    @Column(name = "bmi_remark")
    private String bmiRemark;
    @Column(name = "encounter_date")
    private Date encounterDate;
}
