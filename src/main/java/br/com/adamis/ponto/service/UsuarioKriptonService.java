package br.com.adamis.ponto.service;

import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.adamis.ponto.entity.kripton.PersonalEntity;

@Component
public class UsuarioKriptonService {

	public PersonalEntity getUsuario(Long id) {

		RestTemplate template = new RestTemplate();
		template.getInterceptors().add(new BasicAuthorizationInterceptor("codiub", "C0D1UB"));
		return template.getForObject("http://app.codiub.com.br/kripton-api/usuarios?id={id}", PersonalEntity.class, id);
	}

}
