package ncdupdate.ncdupdate.controller;

import ncdupdate.ncdupdate.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ExcelUploadController {
@Autowired
private ExcelService excelService;

@PostMapping("/upload")
//@RequestParam(value = "files") MultipartFile[] files
// @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadExcelFile(@RequestParam(value = "file") MultipartFile file){
    try{
           excelService.processCsvFile(file);
            return "File uploaded successfully!";
        }catch (Exception e){
        return  "Error uploading files: "+e.getMessage();
        }
    }
}
