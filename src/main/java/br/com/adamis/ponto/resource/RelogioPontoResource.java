package br.com.adamis.ponto.resource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.adamis.ponto.entity.RelogioPonto;
import br.com.adamis.ponto.exceptionhandler.PersonalExceptionHandler.Erro;
import br.com.adamis.ponto.filter.RelogioPontoFilter;
import br.com.adamis.ponto.inputs.RelogioPontoAprovacaoInput;
import br.com.adamis.ponto.inputs.RelogioPontoInput;
import br.com.adamis.ponto.repository.RelogioPontoRepository;
import br.com.adamis.ponto.service.RelogioPontoService;

@RestController
@RequestMapping("/relogioPonto")
public class RelogioPontoResource {

	@Autowired
	private RelogioPontoRepository relogioPontoRepository;

	@Autowired
	private RelogioPontoService relogioPontoService;

	@PostMapping
	public ResponseEntity<Object> criar(@Valid @RequestBody RelogioPontoInput relogioPontoInput,
			HttpServletResponse response) {
		RelogioPonto relogioPontoSalva;
		try {
			relogioPontoSalva = relogioPontoService.save(relogioPontoInput);
			return ResponseEntity.status(HttpStatus.CREATED).body(relogioPontoSalva);
		} catch (Exception e) {
			// e.printStackTrace();
			String mensagemUsuario = e.getMessage();
			String mensagemDesenvolvedor = e.getMessage();
			List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
		}

	}

	@GetMapping("/{codigo}")
	public ResponseEntity<RelogioPonto> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<RelogioPonto> relogioPonto = relogioPontoRepository.findById(codigo);
		return relogioPonto != null ? ResponseEntity.ok(relogioPonto.get()) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		relogioPontoRepository.deleteById(codigo);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<RelogioPonto> atualizar(@PathVariable Long codigo,
			@Valid @RequestBody RelogioPonto relogioPonto) {
		RelogioPonto relogioPontoSalva = relogioPontoService.atualizar(codigo, relogioPonto);
		return ResponseEntity.ok(relogioPontoSalva);
	}
	
	@PutMapping("/aprovacao/{codigo}")
	public ResponseEntity<RelogioPonto> atualizarAprovacao(@PathVariable Long codigo,
			@Valid @RequestBody RelogioPontoAprovacaoInput relogioPontoAprovacaoInput) {
		RelogioPonto relogioPontoSalva = relogioPontoService.atualizarAprovacao(codigo, relogioPontoAprovacaoInput);
		return ResponseEntity.ok(relogioPontoSalva);
	}

	@GetMapping
	public List<RelogioPonto> pesquisar(RelogioPontoFilter relogioPontoFilter, Pageable pageable) {
		return relogioPontoRepository.filtrarTotal(relogioPontoFilter, pageable);
	}
	
	@GetMapping("/page")
	public Page<RelogioPonto> pesquisarPage(RelogioPontoFilter relogioPontoFilter, Pageable pageable) {
		return relogioPontoRepository.filtrar(relogioPontoFilter, pageable);
	}
}
