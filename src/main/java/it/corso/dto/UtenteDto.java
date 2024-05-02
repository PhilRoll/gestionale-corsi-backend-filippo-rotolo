package it.corso.dto;

import java.util.List;

import jakarta.validation.constraints.Pattern;

public class UtenteDto {
	
	private int id;
	@Pattern(regexp="[a-zA-Z\\s']{1,50}", message="nome non corretto")
	private String nome;
	@Pattern(regexp="[a-zA-Z\\s']{1,50}", message="cognome non corretto")
	private String cognome;
	@Pattern(regexp = "[A-Za-z0-9\\.\\+_-]+@[A-Za-z0-9\\._-]+\\.[A-Za-z]{2,24}", message="email non valida")
	private String email;
	private List<UtenteRuoloDto> ruoli;
	private List<UtenteCorsoDto> corsi;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<UtenteRuoloDto> getRuoli() {
		return ruoli;
	}
	public void setRuoli(List<UtenteRuoloDto> ruoli) {
		this.ruoli = ruoli;
	}
	public List<UtenteCorsoDto> getCorsi() {
		return corsi;
	}
	public void setCorsi(List<UtenteCorsoDto> corsi) {
		this.corsi = corsi;
	}
	
	
	
	
}
