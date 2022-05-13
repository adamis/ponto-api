package br.com.adamis.ponto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.adamis.ponto.entity.Versoes;
import br.com.adamis.ponto.repository.versoes.VersoesRepositoryQuery;

public interface VersoesRepository extends JpaRepository<Versoes, Long>, VersoesRepositoryQuery {

	@Query(nativeQuery = true, value = "SELECT id, versao FROM versoes WHERE id = ( SELECT max( id ) FROM versoes )	")
	public Versoes filtrarMax();

}
