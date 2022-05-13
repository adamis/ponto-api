package br.com.adamis.ponto.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.adamis.ponto.entity.kripton.PersonalEntity;
import br.com.adamis.ponto.entity.kripton.Usuarios;
import br.com.adamis.ponto.filter.RelogioPontoFilter;
import br.com.adamis.ponto.repository.RelogioPontoRepository;

@Service
public class RelogioPontoAdminService {

	@Autowired
	private RelogioPontoRepository relogioPontoRepository;

	@Autowired
	private UsuarioKriptonService usuarioKriptonService;

	public List<Usuarios> buscarDistinct(RelogioPontoFilter relogioPontoFilter) {

		List<Integer> filtrarDistinct = relogioPontoRepository.filtrarDistinct(relogioPontoFilter);

		List<Usuarios> list = new ArrayList<>();

		for (int i = 0; i < filtrarDistinct.size(); i++) {
			PersonalEntity usuario = usuarioKriptonService.getUsuario(Long.valueOf(filtrarDistinct.get(i) + ""));
			list.add(usuario.getContent().get(0));
		}

		return list;
	}

}
