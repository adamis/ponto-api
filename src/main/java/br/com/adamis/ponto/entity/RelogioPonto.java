package br.com.adamis.ponto.entity;
// Generated 2 de mar. de 2021 00:28:54 by Hibernate Tools 4.3.5.Final

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "relogio_ponto")
@Data
public class RelogioPonto {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "horas")
	private Integer horas;

	@Column(name = "minutos")
	private Integer minutos;

	// @JsonFormat(timezone = "GMT-03:00")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dia", length = 19)
	private Date dia;

	@Column(name = "obs", length = 9999)
	private String obs;

	@Column(name = "usuario")
	private Integer usuario;

	@Column(name = "aprovado", length = 2)
	private String aprovado;

	@Column(name = "aprovado_obs", length = 9999)
	private String aprovadoObs;

}
