package ncdupdate.ncdupdate.service;


import com.opencsv.CSVReader;
import ncdupdate.ncdupdate.model.PatientData;
import ncdupdate.ncdupdate.repository.PatientDataRepository;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
public class ExcelService {
    @Autowired
    PatientDataRepository patientDataRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void processCsvFile(MultipartFile file) {
        // Use OpenCSV library for easier CSV parsing
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVReader csvReader = new CSVReader(reader);
            // Skip header row (optional)
            csvReader.readNext();

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                // Assuming patient_id is in the first column, and other fields follow
                int integratorId = Integer.parseInt(line[0]);
                String patientIdentifier = line[1].trim();
                double ncdBpUpper = Double.parseDouble(line[2]);
                double ncdBpLower = Double.parseDouble(line[3]);
                double ncdRbs = Double.parseDouble(line[4]);
                double bmiWeight = Double.parseDouble(line[5]);
                double bmiHeight = Double.parseDouble(line[6]);
                double bmiValue = Double.parseDouble(line[7]);
                String bmiRemark = line[8].trim();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String encounterDateString = line[9].trim();
                Date encounterDate = null;
                encounterDate = formatter.parse(encounterDateString);
                java.sql.Date encounterSqlDate = new java.sql.Date(encounterDate.getTime());

                if(encounterSqlDate != null){
                    // Update logic with repository call
                    //PatientData patientData =
                            findAndUpdatePatientData(integratorId, patientIdentifier, ncdBpUpper, ncdBpLower, ncdRbs, bmiWeight, bmiHeight, bmiValue, bmiRemark,encounterSqlDate);
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while processing the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Separate method for update logic with error handling
    private PatientData findAndUpdatePatientData(int integratorId, String patientIdentifier, double ncdBpUpper, double ncdBpLower, double ncdRbs,
                                                 double bmiWeight, double bmiHeight, double bmiValue, String bmiRemark, java.sql.Date encounterSqlDate) {
//        try{
//            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");
//            jdbcTemplate.update("UPDATE integrator_client_intake "+
//                    "SET ncd_bp_upper = ?, ncd_bp_lower = ?, ncd_rbs = ?, " +
//                    "bmi_weight = ?, bmi_height = ?, bmi_value = ?, bmi_remark = ? " +
//                    "WHERE integrator_id = ? AND encounter_date = ?;",
//             ncdBpUpper, ncdBpLower, ncdRbs, bmiWeight, bmiHeight, bmiValue, bmiRemark, integratorId, encounterSqlDate);
//
//        }catch (Exception e){
//            System.out.println("An error occurred while updating patient data: " + e.getMessage());
//            e.printStackTrace();
//        }

        Optional<PatientData> patientDataOptional = patientDataRepository.findByIntegratorIdAndEncounterDate(integratorId,encounterSqlDate); // Assuming unique identifier
        if (patientDataOptional.isPresent()) {
            PatientData patientData = patientDataOptional.get();
            patientData.setNcdBpUpper(ncdBpUpper);
            patientData.setNcdBpLower(ncdBpLower);
            patientData.setNcdRbs(ncdRbs);
            patientData.setBmiWeight(bmiWeight);
            patientData.setBmiHeight(bmiHeight);
            patientData.setBmiValue(bmiValue);
            patientData.setBmiRemark(bmiRemark);
            patientData.setEncounterDate(encounterSqlDate);
            return patientDataRepository.save(patientData);
        } else {
            // Handle scenario where patient data is not found (optional)
            System.out.println("Patient with ID " +integratorId+"  and "+ patientIdentifier + " and encounter date " + encounterSqlDate + " not found. Skipping update.");
            return null;
        }
    }





}