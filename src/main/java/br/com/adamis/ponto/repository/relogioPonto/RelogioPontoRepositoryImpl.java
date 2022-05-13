package br.com.adamis.ponto.repository.relogioPonto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import br.com.adamis.ponto.entity.RelogioPonto;
import br.com.adamis.ponto.entity.RelogioPonto_;
import br.com.adamis.ponto.filter.RelogioPontoFilter;

public class RelogioPontoRepositoryImpl implements RelogioPontoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<RelogioPonto> filtrar(RelogioPontoFilter relogioPontoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<RelogioPonto> criteria = builder.createQuery(RelogioPonto.class);
		Root<RelogioPonto> root = criteria.from(RelogioPonto.class);

		List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

		Predicate[] predicates = criarRestricoes(relogioPontoFilter, builder, root);
		criteria.where(predicates).orderBy(orders);

		TypedQuery<RelogioPonto> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(relogioPontoFilter));
	}
	
	@Override
	public List<RelogioPonto> filtrar(RelogioPontoFilter relogioPontoFilter){
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<RelogioPonto> criteria = builder.createQuery(RelogioPonto.class);
		Root<RelogioPonto> root = criteria.from(RelogioPonto.class);

		Predicate[] predicates = criarRestricoes(relogioPontoFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<RelogioPonto> query = manager.createQuery(criteria);		

		return query.getResultList();
	}

	@Override
	public List<Integer> filtrarDistinct(RelogioPontoFilter relogioPontoFilter) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Integer> criteria = builder.createQuery(Integer.class);
		Root<RelogioPonto> root = criteria.from(RelogioPonto.class);

		criteria.select(root.get(RelogioPonto_.USUARIO)).distinct(true);

		Predicate[] predicates = criarRestricoes(relogioPontoFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<Integer> query = manager.createQuery(criteria);

		return query.getResultList();
	}

	@Override
	public List<RelogioPonto> filtrarTotal(RelogioPontoFilter relogioPontoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<RelogioPonto> criteria = builder.createQuery(RelogioPonto.class);
		Root<RelogioPonto> root = criteria.from(RelogioPonto.class);

		List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

		Predicate[] predicates = criarRestricoes(relogioPontoFilter, builder, root);
		criteria.where(predicates).orderBy(orders);

		TypedQuery<RelogioPonto> query = manager.createQuery(criteria);
		//adicionarRestricoesDePaginacao(query, pageable);

		return query.getResultList();
	}

	private Predicate[] criarRestricoes(RelogioPontoFilter relogioPontoFilter, CriteriaBuilder builder,
			Root<RelogioPonto> root) {
		List<Predicate> predicates = new ArrayList<>();

		// ID
		if (relogioPontoFilter.getId() != null) {
			predicates.add(builder.equal(root.get(RelogioPonto_.ID), relogioPontoFilter.getId()));
		}
		// HORAS
		if (relogioPontoFilter.getHoras() != null) {
			predicates.add(builder.equal(root.get(RelogioPonto_.HORAS), relogioPontoFilter.getHoras()));
		}
		// DIA
		if (relogioPontoFilter.getDia() != null) {

			Calendar c = Calendar.getInstance();
			c.setTime(relogioPontoFilter.getDia());
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			Date ini = c.getTime();

			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MILLISECOND, 0);
			Date fim = c.getTime();

			System.err.println("ini> " + ini.toString());
			System.err.println("fim> " + fim.toString());

			predicates.add(builder.greaterThanOrEqualTo(root.get(RelogioPonto_.DIA), ini));
			predicates.add(builder.lessThanOrEqualTo(root.get(RelogioPonto_.DIA), fim));
		}

		// OBS
		if (StringUtils.hasLength(relogioPontoFilter.getObs())) {
			predicates.add(builder.equal(root.get(RelogioPonto_.OBS), relogioPontoFilter.getObs()));
		}

		// USUARIO
		if (relogioPontoFilter.getUsuario() != null) {
			predicates.add(builder.equal(root.get(RelogioPonto_.USUARIO), relogioPontoFilter.getUsuario()));
		}

		if (relogioPontoFilter.getAprovado() != null) {
			predicates.add(builder.equal(root.get(RelogioPonto_.APROVADO), relogioPontoFilter.getAprovado()));
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

	private Long total(RelogioPontoFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<RelogioPonto> root = criteria.from(RelogioPonto.class);

		Predicate[] predicates = criarRestricoes(filter, builder, root);
		criteria.where(predicates);

		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

}
