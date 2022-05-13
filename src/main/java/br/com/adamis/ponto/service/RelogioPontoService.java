package br.com.adamis.ponto.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.adamis.ponto.dtos.RelHoraExtraDTO;
import br.com.adamis.ponto.entity.RelogioPonto;
import br.com.adamis.ponto.entity.kripton.Usuarios;
import br.com.adamis.ponto.filter.RelogioPontoFilter;
import br.com.adamis.ponto.inputs.RelogioPontoAprovacaoInput;
import br.com.adamis.ponto.inputs.RelogioPontoInput;
import br.com.adamis.ponto.repository.RelogioPontoRepository;

@Service
public class RelogioPontoService {

	@Autowired
	private RelogioPontoRepository relogioPontoRepository;

	@Autowired
	private RelogioPontoAdminService relogioPontoAdminService;

	public RelogioPonto atualizar(Long codigo, RelogioPonto relogioPonto) {
		RelogioPonto relogioPontoSalva = buscarPeloCodigo(codigo);

		BeanUtils.copyProperties(relogioPonto, relogioPontoSalva, "id");
		return relogioPontoRepository.save(relogioPontoSalva);
	}

	public RelogioPonto buscarPeloCodigo(Long codigo) {
		Optional<RelogioPonto> relogioPontoSalva = relogioPontoRepository.findById(codigo);
		if (relogioPontoSalva == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return relogioPontoSalva.get();
	}

	public RelogioPonto save(RelogioPontoInput relogioPontoInput) throws Exception {

		if (!relogioPontoInput.getDia().after(new Date())) {
			RelogioPonto relogioPonto = new RelogioPonto();
			BeanUtils.copyProperties(relogioPontoInput, relogioPonto);

			return relogioPontoRepository.save(relogioPonto);

		} else {
			throw new Exception("Data Invalida!");
		}

	}

	public RelogioPonto atualizarAprovacao(Long codigo, @Valid RelogioPontoAprovacaoInput relogioPontoAprovacaoInput) {

		RelogioPonto buscarPeloCodigo = buscarPeloCodigo(codigo);

		buscarPeloCodigo.setAprovado(relogioPontoAprovacaoInput.getAprovado());
		buscarPeloCodigo.setAprovadoObs(relogioPontoAprovacaoInput.getAprovadoObs());

		return relogioPontoRepository.save(buscarPeloCodigo);		
	}

	public List<RelHoraExtraDTO> filterRelatorio(RelogioPontoFilter relogioPontoFilter) {

		List<Usuarios> buscarDistinct = relogioPontoAdminService.buscarDistinct(relogioPontoFilter);
		List<RelHoraExtraDTO> listRelDto = new ArrayList<RelHoraExtraDTO>();
		
		for (Usuarios usuarios : buscarDistinct) {
			
			relogioPontoFilter.setUsuario(usuarios.getId());
			
			List<RelogioPonto> relogioPontoList = relogioPontoRepository.filtrar(relogioPontoFilter);

			Long countHoras = 0L;
			Long countMinutos = 0L;

			for (RelogioPonto relogioPonto : relogioPontoList) {	            
				countHoras += relogioPonto.getHoras();
				countMinutos += relogioPonto.getMinutos();	            
			}

			long minutos = countMinutos % 60;
			long horas = (countMinutos - minutos) / 60;

			countHoras += horas;

			String hora = countHoras+":"+(minutos <9? "0"+minutos: minutos);            

			for (RelogioPonto relogioPonto : relogioPontoList) {
				RelHoraExtraDTO relHoraExtraDTO = new RelHoraExtraDTO();
				BeanUtils.copyProperties(relogioPonto, relHoraExtraDTO);
				relHoraExtraDTO.setTotal(hora);
				relHoraExtraDTO.setUsuarioNome(usuarios.getNome());
				if(relogioPonto.getAprovado().equals("A")) {
					relHoraExtraDTO.setAprovado("AGUARDANDO");
				}else if(relogioPonto.getAprovado().equals("S")) {
					relHoraExtraDTO.setAprovado("APROVADO");
				}else if(relogioPonto.getAprovado().equals("N")) {
					relHoraExtraDTO.setAprovado("REPROVADO");
				}
				listRelDto.add(relHoraExtraDTO);
			}

		}


		return listRelDto;
	}

}
