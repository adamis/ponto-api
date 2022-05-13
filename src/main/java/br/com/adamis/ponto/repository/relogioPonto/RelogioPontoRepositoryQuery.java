package br.com.adamis.ponto.repository.relogioPonto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.adamis.ponto.entity.RelogioPonto;
import br.com.adamis.ponto.filter.RelogioPontoFilter;

public interface RelogioPontoRepositoryQuery {

	public Page<RelogioPonto> filtrar(RelogioPontoFilter relogioPontoFilter, Pageable pageable);
	
	public List<RelogioPonto> filtrar(RelogioPontoFilter relogioPontoFilter);

	public List<RelogioPonto> filtrarTotal(RelogioPontoFilter relogioPontoFilter, Pageable pageable);

	public List<Integer> filtrarDistinct(RelogioPontoFilter relogioPontoFilter);
}
