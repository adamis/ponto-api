package br.com.adamis.ponto.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.adamis.ponto.entity.Versoes;
import br.com.adamis.ponto.repository.VersoesRepository;

@Service
public class VersoesService {

	@Autowired
	private VersoesRepository versoesRepository;

	public Versoes atualizar(Long codigo, Versoes versoes) {
		Versoes versoesSalva = buscarPeloCodigo(codigo);

		BeanUtils.copyProperties(versoes, versoesSalva, "id");
		return versoesRepository.save(versoesSalva);
	}

	public Versoes buscarPeloCodigo(Long codigo) {
		Optional<Versoes> versoesSalva = versoesRepository.findById(codigo);
		if (versoesSalva == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return versoesSalva.get();
	}
}
