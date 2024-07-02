package ncdupdate.ncdupdate.controller;

import ncdupdate.ncdupdate.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ExcelUploadController {
@Autowired
private ExcelService excelService;

@PostMapping("/upload")
//@RequestParam(value = "files") MultipartFile[] files
// @PostMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String,Integer>> uploadExcelFile(@RequestParam(value = "file") MultipartFile file){
    try{
           Map<String,Integer> response = excelService.processCsvFile(file);
            return ResponseEntity.ok(response);
        }catch (Exception e){
        log.error("An error occurred while processing the CSV file: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
