package br.com.adamis.ponto.inputs;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RelogioPontoAprovacaoInput {

	@NotNull
	private String aprovado;
	
	private String aprovadoObs;
	
}
