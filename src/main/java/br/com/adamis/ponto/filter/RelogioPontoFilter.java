package br.com.adamis.ponto.filter;

import java.util.Date;

import lombok.Data;

@Data
public class RelogioPontoFilter {

	private Integer id;
	private Integer horas;
	private Date dia;
	private String obs;
	private String aprovado;
	private Long usuario;
}
