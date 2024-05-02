package it.corso.service;

import java.util.List;

import it.corso.dto.UtenteCorsoIscrizioneDto;
import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteRegistrazioneDto;
import it.corso.dto.UtenteUpdateDto;
import it.corso.model.Utente;

public interface UtenteService {
	
	void registerUtente(UtenteRegistrazioneDto utente);
	
	boolean existsUtente(String email);
	
	void deleteUtenteByEmail(String email);
	
	Utente getUtenteByEmail(String email);
	
	UtenteDto getUtenteDtoByEmail(String email);
	
	void updateUtente(UtenteUpdateDto utenteUpdateDto);
	
	List<UtenteDto> getAllUtenteDto();
	
	boolean loginUtente(UtenteLoginRequestDto utenteLoginRequestDto);
	
	void signUpForCourse(UtenteCorsoIscrizioneDto utenteCorsoIscrizioneDto);
	
}
