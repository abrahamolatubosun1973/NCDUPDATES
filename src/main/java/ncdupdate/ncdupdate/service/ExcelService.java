package ncdupdate.ncdupdate.service;


import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ExcelService {
    @Autowired
    PatientDataRepository patientDataRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Integer> processCsvFile(MultipartFile file) throws Exception{
        Map<String, Integer> response = new HashMap<>();

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVReader csvReader = new CSVReader(reader);
            // Skip header row (optional)
            csvReader.readNext();

            int totalRowsFromCsv = 0;
            int totalRowsFromDatabase = 0;
            int totalRecordsUpdated = 0;

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                totalRowsFromCsv++;

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
                Date encounterDate = formatter.parse(encounterDateString);
                java.sql.Date encounterSqlDate = new java.sql.Date(encounterDate.getTime());

                if (encounterSqlDate != null) {
                    Optional<PatientData> patientDataOptional = patientDataRepository.findByIntegratorIdAndPatientIdentifierAndEncounterDate(integratorId,patientIdentifier, encounterSqlDate);
                    if (patientDataOptional.isPresent()) {
                        totalRowsFromDatabase++;
                        PatientData patientData = patientDataOptional.get();
                        patientData.setNcdBpUpper(ncdBpUpper);
                        patientData.setNcdBpLower(ncdBpLower);
                        patientData.setNcdRbs(ncdRbs);
                        patientData.setBmiWeight(bmiWeight);
                        patientData.setBmiHeight(bmiHeight);
                        patientData.setBmiValue(bmiValue);
                        patientData.setBmiRemark(bmiRemark);
                        patientData.setEncounterDate(encounterSqlDate);
                        patientDataRepository.save(patientData);
                        totalRecordsUpdated++;
                    }
                }
            }

            response.put("totalRowsFromCsv", totalRowsFromCsv);
            response.put("totalRowsFromDatabase", totalRowsFromDatabase);
            response.put("totalRecordsUpdated", totalRecordsUpdated);

            log.info("Response: {}", response);

        } catch (Exception e) {
            log.error("An error occurred while processing the CSV file: {}", e.getMessage(), e);
        }

        return response;
    }



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



}