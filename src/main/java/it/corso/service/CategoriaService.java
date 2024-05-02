package it.corso.service;

import java.util.List;

import it.corso.dto.CategoriaDto;

public interface CategoriaService {
	
	List<CategoriaDto> getAllCategories();
	CategoriaDto getCategory(int id);
	void createCategory(CategoriaDto categoriaCreateDto);
	void updateCategory(CategoriaDto categoriaUpdateDto);
	void deleteCategory(int id);
	List<CategoriaDto> getAllCategoriesContaining(String nomeCategoria);
}
