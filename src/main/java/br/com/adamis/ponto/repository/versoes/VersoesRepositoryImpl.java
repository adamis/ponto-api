package br.com.adamis.ponto.repository.versoes;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.StringUtils;

import br.com.adamis.ponto.entity.Versoes;
import br.com.adamis.ponto.entity.Versoes_;
import br.com.adamis.ponto.filter.VersoesFilter;

public class VersoesRepositoryImpl implements VersoesRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Versoes> filtrar(VersoesFilter versoesFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Versoes> criteria = builder.createQuery(Versoes.class);
		Root<Versoes> root = criteria.from(Versoes.class);

		List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

		Predicate[] predicates = criarRestricoes(versoesFilter, builder, root);
		criteria.where(predicates).orderBy(orders);

		TypedQuery<Versoes> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(versoesFilter));
	}

	private Predicate[] criarRestricoes(VersoesFilter versoesFilter, CriteriaBuilder builder, Root<Versoes> root) {
		List<Predicate> predicates = new ArrayList<>();

		// ID
		if (versoesFilter.getId() != null) {
			predicates.add(builder.equal(root.get(Versoes_.ID), versoesFilter.getId()));
		}
		// VERSAO
		if (StringUtils.hasLength(versoesFilter.getVersao())) {
			predicates.add(builder.like(builder.lower(root.get(Versoes_.VERSAO)),
					"%" + versoesFilter.getVersao().toLowerCase() + "%"));
		}

		// ID
		if (versoesFilter.getId() != null) {
			predicates.add(builder.equal(root.get(Versoes_.ID), versoesFilter.getId()));
		}
		// VERSAO
		if (StringUtils.hasLength(versoesFilter.getVersao())) {
			predicates.add(builder.like(builder.lower(root.get(Versoes_.VERSAO)),
					"%" + versoesFilter.getVersao().toLowerCase() + "%"));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}

	private Long total(VersoesFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Versoes> root = criteria.from(Versoes.class);

		Predicate[] predicates = criarRestricoes(filter, builder, root);
		criteria.where(predicates);

		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

}
