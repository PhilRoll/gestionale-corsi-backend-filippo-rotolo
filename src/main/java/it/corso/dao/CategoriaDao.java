package it.corso.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.corso.dto.CategoriaDto;
import it.corso.model.Categoria;
import it.corso.model.NomeCategoria;

public interface CategoriaDao extends CrudRepository<Categoria, Integer>{
	
	List<Categoria> findByNomeCategoria(NomeCategoria nomeCategoria);
	
	@Query
	(value = "SELECT * FROM categoria WHERE Nome_Categoria=:nomeCategoria", nativeQuery = true)
	List<Categoria> findByNomeCategoria(@Param("nomeCategoria") String nomeCategoria);
	
}
