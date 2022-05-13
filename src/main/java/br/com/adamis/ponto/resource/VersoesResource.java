package br.com.adamis.ponto.resource;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.adamis.ponto.entity.Versoes;
import br.com.adamis.ponto.filter.VersoesFilter;
import br.com.adamis.ponto.repository.VersoesRepository;

@RestController
@RequestMapping("/versoes")
public class VersoesResource {

	@Autowired
	private VersoesRepository versoesRepository;

	@GetMapping("/{codigo}")
	public ResponseEntity<Versoes> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Versoes> versoes = versoesRepository.findById(codigo);
		return versoes != null ? ResponseEntity.ok(versoes.get()) : ResponseEntity.notFound().build();
	}

	@GetMapping
	public Page<Versoes> pesquisar(VersoesFilter versoesFilter, Pageable pageable) {
		return versoesRepository.filtrar(versoesFilter, pageable);
	}

	@GetMapping("/max")
	public Versoes pesquisarmax() {
		return versoesRepository.filtrarMax();
	}
}
