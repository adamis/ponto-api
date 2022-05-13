package br.com.adamis.ponto.resource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.adamis.ponto.dtos.RelHoraExtraDTO;
import br.com.adamis.ponto.enums.ReportType;
import br.com.adamis.ponto.filter.RelogioPontoFilter;
import br.com.adamis.ponto.service.RelogioPontoService;
import br.com.adamis.ponto.utils.ReportFactory;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RestController
@RequestMapping("/print")
public class ReportsResource {


	@Autowired
	ResourceLoader resourceLoader;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private RelogioPontoService relogioPontoService;

	//private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


	@RequestMapping(value = "/horasExtras", method = RequestMethod.POST)
	public ResponseEntity<Object> despesas(//OK
			//@RequestBody 
			RelogioPontoFilter relogioPontoFilter			
			) {

		Connection conn = null;	
		ResponseEntity<Object> response = null;

		try {
			conn = dataSource.getConnection();

			List<RelHoraExtraDTO> findRelDespesas = relogioPontoService.filterRelatorio(relogioPontoFilter);
			//response = ResponseEntity.ok(findRelDespesas);

			
			String nomefilter = "";

			if(nomefilter.length() == 0) {
				nomefilter = "Filtro: Sem Filtro";
			}

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("REPORT_CONNECTION",conn);



			//parameters.put("sessao","");
			//parameters.put("nomefilter", nomefilter );

			JRDataSource jrds = new JRBeanCollectionDataSource(findRelDespesas);

			//File file = new ClassPathResource("reports/RelDespesas.jasper").getFile();

			// Fetching the .jrxml file from the resources folder.
			final InputStream stream = new ClassPathResource("reports/RelHorasExtras.jrxml").getInputStream();		 
			// Compile the Jasper report from .jrxml to .japser
			final JasperReport report = JasperCompileManager.compileReport(stream);


			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters,jrds);
			ReportFactory reportFactory = new ReportFactory(jasperPrint);			
			response = reportFactory.print(ReportType.PDF, "Relatorio");

			 
		} catch (Exception e) {
			response = ResponseEntity.badRequest().body("Erro Interno!");
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {	
				response = ResponseEntity.badRequest().body("Erro Interno!");
				e.printStackTrace();
			}
		}


		return response;


	}




}
