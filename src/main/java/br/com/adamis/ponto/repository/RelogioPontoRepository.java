package br.com.adamis.ponto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.adamis.ponto.entity.RelogioPonto;
import br.com.adamis.ponto.repository.relogioPonto.RelogioPontoRepositoryQuery;

public interface RelogioPontoRepository extends JpaRepository<RelogioPonto, Long>, RelogioPontoRepositoryQuery {

}
