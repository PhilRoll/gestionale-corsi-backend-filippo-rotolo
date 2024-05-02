package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.CorsoDao;
import it.corso.dao.RuoloDao;
import it.corso.dao.UtenteDao;
import it.corso.dto.UtenteCorsoIscrizioneDto;
import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteRegistrazioneDto;
import it.corso.dto.UtenteUpdateDto;
import it.corso.model.Corso;
import it.corso.model.Ruolo;
import it.corso.model.Utente;

@Service
public class UtenteServiceImpl implements UtenteService {
	
	@Autowired
	private UtenteDao utenteDao;
	@Autowired
	private RuoloDao ruoloDao;
	@Autowired
	private CorsoDao corsoDao;
	
	private ModelMapper mapper = new ModelMapper();

	@Override
	public void registerUtente(UtenteRegistrazioneDto utenteRegistrazioneDto) {
		
		Utente utente = new Utente();
		utente.setNome(utenteRegistrazioneDto.getNome());
		utente.setCognome(utenteRegistrazioneDto.getCognome());
		utente.setEmail(utenteRegistrazioneDto.getEmail());
		String sha256hex = DigestUtils.sha256Hex(utenteRegistrazioneDto.getPassword());
		utente.setPassword(sha256hex);
		utenteDao.save(utente);
	}
	
	
	@Override
	public void deleteUtenteByEmail(String email) {
		Utente utente = utenteDao.findByEmail(email);
		Optional<Utente> utenteOptional = utenteDao.findById(utente.getId());
	  	if(utenteOptional.isPresent()){
			utenteDao.delete(utenteOptional.get());
		}
			
	}
	
	
	@Override
	public boolean existsUtente(String email) {
		return utenteDao.existsByEmail(email);
	}

	
	@Override
	public Utente getUtenteByEmail(String email) {
		return utenteDao.findByEmail(email);
	}
	
	
	//utilizzo del ModelMapper per mappare l'utente con UtenteDto
	@Override
	public UtenteDto getUtenteDtoByEmail(String email) {
		Utente utente = utenteDao.findByEmail(email);
		return mapper.map(utente, UtenteDto.class);
	}


	@Override
	public void updateUtente(UtenteUpdateDto utenteUpdateDto) {
		Utente utente = utenteDao.findByEmail(utenteUpdateDto.getEmail());
		if(utente!=null) {
			// nuova lista ruoli
			List<Ruolo> ruoliUtente = new ArrayList<>();
			// cerco sul db il ruolo con questo id
			Optional<Ruolo> ruoloDb = ruoloDao.findById(utenteUpdateDto.getIdRuolo());
			if(ruoloDb.isPresent()){
				Ruolo ruolo = ruoloDb.get();
				// Imposta l'id del ruolo con quello fornito nell'oggetto UtenteUpdateDto
				ruolo.setId(utenteUpdateDto.getIdRuolo());
				// Aggiungi il ruolo alla lista dei ruoli dell'utente
				ruoliUtente.add(ruolo);	
			}
			
			// Aggiorna i dettagli dell'utente
	        utente.setNome(utenteUpdateDto.getNome());
	        utente.setCognome(utenteUpdateDto.getCognome());
	        utente.setEmail(utenteUpdateDto.getEmail());
	        utente.setRuoli(ruoliUtente); 
	        //salvataggio sul db
			utenteDao.save(utente);
		}

	}


	@Override
	public List<UtenteDto> getAllUtenteDto() {
		List<Utente> listaUtenti = (List<Utente>) utenteDao.findAll();
		List<UtenteDto> listaUtentiDto = new ArrayList<>();
		
		listaUtenti.forEach(u -> listaUtentiDto.add(mapper.map(u, UtenteDto.class)));
		
		return listaUtentiDto;
	}



	@Override
	public boolean loginUtente(UtenteLoginRequestDto utenteLoginRequestDto) {

		Utente utente = new Utente();
		utente.setEmail(utenteLoginRequestDto.getEmail());
		//fingiamo che la password sia "ciao":
		//getPassword di utenteLoginRequestDto mi recupera questa password e la setto su utente tramite il metodo 
		//setPassword
		utente.setPassword(utenteLoginRequestDto.getPassword());
		
		System.out.println(utente.getPassword());
		//tramite il getPassword di utente recupero la password e la passo al metodo che me la hasha
		String passwordHash = DigestUtils.sha256Hex(utente.getPassword());
		
		Utente credenzialiUtente = utenteDao.findByEmailAndPassword(utente.getEmail(), passwordHash);
		
		// operatore ternario dato dalla condizione? espressione a  se è vero espressione b se è falso
		return credenzialiUtente != null ? true : false;
	}


	@Override
	public void signUpForCourse(UtenteCorsoIscrizioneDto utenteCorsoIscrizioneDto) {
		Utente utente = utenteDao.findByEmail(utenteCorsoIscrizioneDto.getEmail());
		Corso corso = corsoDao.findByNomeCorso(utenteCorsoIscrizioneDto.getNomeCorso());
		if(utente!=null && corso!=null){
			List<Corso> listaCorsi = utente.getCorsi();
			
			listaCorsi.add(corso);
			utente.setCorsi(listaCorsi);
			
			utenteDao.save(utente);
		}
		
	}







}
