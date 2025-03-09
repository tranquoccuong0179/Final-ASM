package com.assignment.asm.service;

import com.assignment.asm.dto.response.UserResponse;
import com.assignment.asm.mapper.UserMapper;
import com.assignment.asm.model.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReportService implements IReportService {

    @Override
    public byte[] generatePdfListUser(List<UserResponse> users) throws JRException, FileNotFoundException {
        try (InputStream reportStream = getClass().getClassLoader().getResourceAsStream("report/template-jasper.jrxml")) {
            if (reportStream == null) {
                throw new FileNotFoundException("Cannot find the report template file");
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(users);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitle", "List Users");
            parameters.put("GeneratedDate", new Date());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return outputStream.toByteArray();
        } catch (JRException | FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected error during PDF generation", e);
        }
    }

    @Override
    public byte[] generateExcelListUser(List<UserResponse> users) {
        try (InputStream reportStream = getClass().getClassLoader().getResourceAsStream("report/template-jasper.jrxml")) {
            if (reportStream == null) {
                throw new FileNotFoundException("Cannot find the report template file");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(users);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitle", "List Users");
            parameters.put("GeneratedDate", new Date());

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JRXlsxExporter jrXlsxExporter = new JRXlsxExporter();
            jrXlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            jrXlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            SimpleXlsxReportConfiguration simpleXlsxReportConfiguration = new SimpleXlsxReportConfiguration();
            simpleXlsxReportConfiguration.setOnePagePerSheet(false);
            simpleXlsxReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
            simpleXlsxReportConfiguration.setDetectCellType(true);
            simpleXlsxReportConfiguration.setCollapseRowSpan(false);
            jrXlsxExporter.setConfiguration(simpleXlsxReportConfiguration);

            jrXlsxExporter.exportReport();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo file Excel", e);
        }
    }
}
