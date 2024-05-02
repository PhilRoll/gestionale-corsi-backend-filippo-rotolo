package it.corso.dao;

import org.springframework.data.repository.CrudRepository;

import it.corso.model.Corso;
import java.util.List;



public interface CorsoDao extends CrudRepository<Corso, Integer>{

	Corso findByNomeCorso(String nomeCorso);
	
}
