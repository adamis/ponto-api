package br.com.adamis.ponto.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.adamis.ponto.entity.kripton.Usuarios;
import br.com.adamis.ponto.filter.RelogioPontoFilter;
import br.com.adamis.ponto.service.RelogioPontoAdminService;

@RestController
@RequestMapping("/relogioPontoAdmin")
public class RelogioPontoAdminResource {

	// @Autowired private RelogioPontoRepository relogioPontoRepository;

	@Autowired
	private RelogioPontoAdminService relogioPontoAdminService;

	@GetMapping("/usuarios")
	public List<Usuarios> pesquisarUsuarios(RelogioPontoFilter relogioPontoFilter) {
		return relogioPontoAdminService.buscarDistinct(relogioPontoFilter);
	}
}
