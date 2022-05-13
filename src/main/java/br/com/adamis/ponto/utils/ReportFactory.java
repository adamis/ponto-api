/**
 * 
 */
package br.com.adamis.ponto.utils;

import java.io.File;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import br.com.adamis.ponto.enums.ReportType;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

/**
 * @author adamis.rocha
 *
 */
public class ReportFactory {

	private JasperPrint jasperPrint;
	
	public ReportFactory(JasperPrint jasperPrint) {
		this.jasperPrint = jasperPrint;
	}
	
	public ResponseEntity<Object> print(ReportType reportType,String fileName) throws Exception {
		
		ResponseEntity<Object> response = null;
		
		switch (reportType) {
		case PDF:

			byte[] exportReportToPdf = JasperExportManager.exportReportToPdf(jasperPrint);

			response = ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
					.body(exportReportToPdf);
			
			break;

		case HTML:

			File fileTemp = new File(fileName+".html");
			
			JasperExportManager.exportReportToHtmlFile(jasperPrint,fileTemp.getAbsolutePath());

			response = ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_TYPE, MediaType.ALL_VALUE)
					.body(Files.readAllBytes(fileTemp.toPath()))
			;

			File fileFolder = new File(fileName+".html_files");
			FileUtils.deleteDirectory(fileFolder);
			fileTemp.delete();
			
			break;
			
		case CSV:

			File fileTempCSV = new File(fileName+".csv");
			JRCsvExporter exporter = new JRCsvExporter();
			
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleWriterExporterOutput(fileTempCSV));
			exporter.exportReport();

//			System.err.println(""+fileTempCSV.getAbsolutePath());
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=\"" + fileTempCSV.getName() + "\"");
			headers.add(HttpHeaders.CONTENT_TYPE, "text/csv;charset=utf-8");
			
			response = ResponseEntity.ok()						
					.headers(headers)						
					.body(Files.readAllBytes(fileTempCSV.toPath()))						
					;
			
			fileTempCSV.delete();
			break;
			
		case XLSX:

			File fileTempXLSX = new File(fileName+".xlsx");
			JRXlsxExporter exporterXLSX = new JRXlsxExporter();
			
			exporterXLSX.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporterXLSX.setExporterOutput(new SimpleOutputStreamExporterOutput(fileTempXLSX));
			exporterXLSX.exportReport();

//			System.err.println(""+fileTempXLSX.getAbsolutePath());
			
			HttpHeaders headersXLSX = new HttpHeaders();
			headersXLSX.add("Content-Disposition", "attachment; filename=\"" + fileTempXLSX.getName() + "\"");
			headersXLSX.add(HttpHeaders.CONTENT_TYPE, "text/csv;charset=utf-8");
			
			response = ResponseEntity.ok()						
					.headers(headersXLSX)						
					.body(Files.readAllBytes(fileTempXLSX.toPath()))						
					;
			
			fileTempXLSX.delete();
			
			break;
			
		case DOCX:

			File fileTempDOCX = new File(fileName+".docx");
			JRDocxExporter exporterDOCS = new JRDocxExporter();
			
			exporterDOCS.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporterDOCS.setExporterOutput(new SimpleOutputStreamExporterOutput(fileTempDOCX));
			exporterDOCS.exportReport();

//			System.err.println(""+fileTempDOCX.getAbsolutePath());
			
			HttpHeaders headersDOCX = new HttpHeaders();
			headersDOCX.add("Content-Disposition", "attachment; filename=\"" + fileTempDOCX.getName() + "\"");
			headersDOCX.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			
			response = ResponseEntity.ok()						
					.headers(headersDOCX)						
					.body(Files.readAllBytes(fileTempDOCX.toPath()))						
					;
			
			fileTempDOCX.delete();
			
			break;
			
		case RTF:

			File fileTempRTF = new File(fileName+".rtf");
			JRRtfExporter exporterRTF = new JRRtfExporter();
			
			exporterRTF.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporterRTF.setExporterOutput(new SimpleWriterExporterOutput(fileTempRTF));
			exporterRTF.exportReport();

//			System.err.println(""+fileTempRTF.getAbsolutePath());
			
			HttpHeaders headersRTF = new HttpHeaders();
			headersRTF.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileTempRTF.getName() + "\"");
			headersRTF.add(HttpHeaders.CONTENT_TYPE, "text/richtext");
			
			response = ResponseEntity.ok()						
					.headers(headersRTF)						
					.body(Files.readAllBytes(fileTempRTF.toPath()))						
					;
			
			fileTempRTF.delete();
			
			break;
			
			
		default:
			response = ResponseEntity.badRequest().body("Tipo de Arquivo de Retorno não encontrado!");
			break;
		}
		
		return response;
	}
	
}
