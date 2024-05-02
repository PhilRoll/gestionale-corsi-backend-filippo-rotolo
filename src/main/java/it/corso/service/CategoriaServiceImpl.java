package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.EnumUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.CategoriaDao;
import it.corso.dto.CategoriaDto;
import it.corso.model.Categoria;
import it.corso.model.NomeCategoria;

@Service
public class CategoriaServiceImpl implements CategoriaService {

	
	@Autowired
	CategoriaDao categoriaDao;
	
	private ModelMapper mapper = new ModelMapper();

	
	@Override
	public List<CategoriaDto> getAllCategories() {
		List<Categoria> listaCategorie = (List<Categoria>) categoriaDao.findAll();
		List<CategoriaDto> listaCategoriaDto = new ArrayList<>();
		
		listaCategorie.forEach(c -> listaCategoriaDto.add(mapper.map(c, CategoriaDto.class)));
		
		return listaCategoriaDto;
	}
	
	
	@Override
	public List<CategoriaDto> getAllCategoriesContaining(String nomeCategoria) {
		List<CategoriaDto> listaCategoriaDto = new ArrayList<>();
		//verifica che la stringa è effettivamente un elemento dell'enum:
		if(EnumUtils.isValidEnum(NomeCategoria.class, nomeCategoria)) {
			//List<Categoria> listaCategorie = categoriaDao.findByNomeCategoria(NomeCategoria.valueOf(nomeCategoria));
			List<Categoria> listaCategorie = categoriaDao.findByNomeCategoria(nomeCategoria);
			if(!listaCategorie.isEmpty()) {
				listaCategorie.forEach(c -> listaCategoriaDto.add(mapper.map(c, CategoriaDto.class)));
			}
		}
		return listaCategoriaDto;
	}
	
	

	@Override
	public CategoriaDto getCategory(int id) {
		Optional<Categoria> categoriaDb = categoriaDao.findById(id);
		if(categoriaDb.isPresent()){
			Categoria categoria = categoriaDb.get();
			return mapper.map(categoria, CategoriaDto.class);
		}
		return null;
	}

	
	
	@Override
	public void createCategory(CategoriaDto categoriaDto) {
		Categoria categoria = new Categoria();
		categoria.setNomeCategoria(categoriaDto.getNomeCategoria());
		categoriaDao.save(categoria);
	}


	
	
	@Override
	public void updateCategory(CategoriaDto categoriaDto) {
		Optional<Categoria> categoriaDb = categoriaDao.findById(categoriaDto.getId());
		if(categoriaDb.isPresent()){
			Categoria categoria = categoriaDb.get();
			categoria.setNomeCategoria(categoriaDto.getNomeCategoria());
		}
	}

	
	
	
	@Override
	public void deleteCategory(int id) {
		Optional<Categoria> categoriaDb = categoriaDao.findById(id);
		if(categoriaDb.isPresent()){
			categoriaDao.delete(categoriaDb.get());
		}
	}
	
	
	
	


	
	

}
