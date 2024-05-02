package it.corso.dto;

import java.util.ArrayList;
import java.util.List;

import it.corso.model.Corso;
import it.corso.model.NomeCategoria;


public class CategoriaDto {

	private int id;
	private NomeCategoria nomeCategoria;
	private List<CategoriaCorsoDto> corsi = new ArrayList<>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public NomeCategoria getNomeCategoria() {
		return nomeCategoria;
	}
	public void setNomeCategoria(NomeCategoria nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}
	public List<CategoriaCorsoDto> getCorsi() {
		return corsi;
	}
	public void setCorsi(List<CategoriaCorsoDto> corsi) {
		this.corsi = corsi;
	}
	
	
	
	
	
}
