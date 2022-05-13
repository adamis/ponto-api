package br.com.adamis.ponto.repository.versoes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.adamis.ponto.entity.Versoes;
import br.com.adamis.ponto.filter.VersoesFilter;

public interface VersoesRepositoryQuery {

	public Page<Versoes> filtrar(VersoesFilter versoesFilter, Pageable pageable);

}
