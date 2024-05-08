package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.CategoriaDao;
import it.corso.dao.CorsoDao;
import it.corso.dto.CorsoDto;
import it.corso.dto.UtenteDto;
import it.corso.model.Categoria;
import it.corso.model.Corso;
import it.corso.model.Utente;


@Service
public class CorsoServiceImpl implements CorsoService {

	@Autowired
	CorsoDao corsoDao;
	
	@Autowired
	CategoriaDao categoriaDao;
	
	private ModelMapper mapper = new ModelMapper();
	
	
	@Override
	public CorsoDto getCorso(int id) {
		Optional<Corso> corsoDb = corsoDao.findById(id);
		if(corsoDb.isPresent()){
			Corso corso = corsoDb.get();
			return mapper.map(corso,CorsoDto.class);
		}
		
		return null;
	}
	
	
	
	@Override
	 public List<CorsoDto> getAllCorsi() {
	  
	  List<Corso> listaCorsi =  (List<Corso>) corsoDao.findAll();
	  List<CorsoDto> listaCorsiDto = new ArrayList<>();
	  listaCorsi.forEach(c -> listaCorsiDto.add(mapper.map(c, CorsoDto.class)));
	  
	  return listaCorsiDto;
	 }

	

	@Override
	public boolean createCorso(CorsoDto corsoDto) {
		Optional<Categoria> categoriaDb = categoriaDao.findById(corsoDto.getIdCategoria());
		if(categoriaDb.isPresent()) {
			Categoria categoria = categoriaDb.get();
			
			Corso corso = new Corso();
			corso.setNomeCorso(corsoDto.getNomeCorso());
			corso.setDescrizioneBreve(corsoDto.getDescrizioneBreve());
			corso.setDescrizioneCompleta(corsoDto.getDescrizioneCompleta());
			corso.setDurata(corsoDto.getDurata());
			corso.setCategoria(categoria);
			
			corsoDao.save(corso);
			return true;
		}
		return false;
	}


	@Override
	public void deleteCorso(int id) {
		Optional<Corso> corsoDb = corsoDao.findById(id);
		if(corsoDb.isPresent()){
			corsoDao.delete(corsoDb.get());
		}
		
	}



	
	
	
	
	
	
	
	
	
	
	
}
