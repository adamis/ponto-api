package br.com.adamis.ponto.dtos;

import java.util.Date;

import lombok.Data;

@Data
public class RelHoraExtraDTO {

	private Long id;
	private Integer horas;
	private Integer minutos;
	private Date dia;
	private String obs;
	private Integer usuario;
	private String usuarioNome;	
	private String aprovado;
	private String aprovadoObs;
	private String total;
}
