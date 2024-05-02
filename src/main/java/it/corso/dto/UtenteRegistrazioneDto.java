package it.corso.dto;

import jakarta.validation.constraints.Pattern;


public class UtenteRegistrazioneDto {
	
	private int id;
	@Pattern(regexp="[a-zA-Z\\s']{1,50}", message="nome non corretto")
	private String nome;
	@Pattern(regexp="[a-zA-Z\\s']{1,50}", message="cognome non corretto")
	private String cognome;
	@Pattern(regexp = "[A-Za-z0-9\\.\\+_-]+@[A-Za-z0-9\\._-]+\\.[A-Za-z]{2,24}", message="email non valida")
	private String email;
	private String password;
	
	
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
