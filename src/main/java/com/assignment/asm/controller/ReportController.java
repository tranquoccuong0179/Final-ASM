package com.assignment.asm.controller;

import com.assignment.asm.dto.response.UserResponse;
import com.assignment.asm.service.IReportService;
import com.assignment.asm.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
    IReportService reportService;
    IUserService IUserService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf() {
        try {
            List<UserResponse> users = IUserService.getAllProfiles();
            byte[] content = reportService.generatePdfListUser(users);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=report_user.pdf");
            headers.setContentType(MediaType.APPLICATION_PDF);
            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new byte[0]);
        } catch (JRException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new byte[0]);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/generate-excel")
    public ResponseEntity<byte[]> generateExcel() {
        try {
            List<UserResponse> users = IUserService.getAllProfiles();
            byte[] content = reportService.generateExcelListUser(users);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=report_user.xlsx");
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
        } catch (JRException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
        }
    }
}
