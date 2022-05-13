package br.com.adamis.ponto.inputs;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class RelogioPontoInput {

	private Long id;

	@NotNull
	private Integer horas;

	@NotNull
	private Integer minutos;

	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date dia;

	private String obs;

	@NotNull
	private String aprovado;

	@NotNull
	private Integer usuario;
	
	
	private String aprovadoObs;
	
}
