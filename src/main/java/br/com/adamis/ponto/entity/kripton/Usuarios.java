package br.com.adamis.ponto.entity.kripton;

import java.util.Date;

import lombok.Data;

@Data
public class Usuarios implements java.io.Serializable {

	private static final long serialVersionUID = 3957306199243577683L;
	private Long id;
	private String cpfCnpj;
	private String nome;
	private String email;
	private String contato;
	private Date dataCadastro;
	private String senha;
	private String tipoAcesso;
	private String foto;
	private String situacao;
	private String token;
	private Date expiraToken;
	private String usuarioRede;
	private String root;
	private String primeiroAcesso;

}
