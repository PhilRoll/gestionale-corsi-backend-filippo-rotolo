package it.corso.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import it.corso.dto.UtenteCorsoIscrizioneDto;
import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteLoginResponseDto;
import it.corso.dto.UtenteRegistrazioneDto;
import it.corso.dto.UtenteUpdateDto;
import it.corso.model.Utente;
import it.corso.service.BlackList;
import it.corso.service.UtenteService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Path("/utente")
public class UtenteController {

	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private BlackList blackList;
	
	
	@POST
	@Path("/registrazione")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUtente(@Valid @RequestBody UtenteRegistrazioneDto utenteRegistrazioneDto){
		try {
			if(!Pattern.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}", utenteRegistrazioneDto.getPassword())){
				return Response.status(Response.Status.BAD_REQUEST).build();
				
			}
			
			if(utenteService.existsUtente(utenteRegistrazioneDto.getEmail())){
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			
			utenteService.registerUtente(utenteRegistrazioneDto);
			return Response.status(Response.Status.OK).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	
	@DELETE
	@Path("/elimina/{email}")
	public Response deleteUtente(@PathParam("email") String email){
		try {
			utenteService.deleteUtenteByEmail(email);
			return Response.status(Response.Status.OK).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	
	
	@GET
	@Path("/infoutente")
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectUtenteByEmail(@QueryParam("email") String email){
		
		try {
			if (email != null && !email.isEmpty()) {
				UtenteDto utenteDto = utenteService.getUtenteDtoByEmail(email);
				if (utenteDto != null) {
					return Response.status(Response.Status.OK).entity(utenteDto).build();
				}
			}
	
			return Response.status(Response.Status.BAD_REQUEST).build();
	
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
	}
	
	
	@PUT
	@Path("/aggiorna")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUtente(@Valid @RequestBody UtenteUpdateDto utente) {
		try {
			utenteService.updateUtente(utente);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}
	
	
	@GET
	@Path("/tutti_gli_utenti")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUtenti(){
		try {
			return Response.status(Response.Status.OK).entity(utenteService.getAllUtenteDto()).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginUtente(@RequestBody UtenteLoginRequestDto utenteLoginRequestDto){
		
		try {
			if(utenteService.loginUtente(utenteLoginRequestDto)){
				return Response.ok(issueToken(utenteLoginRequestDto.getEmail())).build();
			}
			
			return Response.status(Response.Status.BAD_REQUEST).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
	}
	
	
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logoutUtente(ContainerRequestContext containerRequestContext){
		try {
			String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
			String token = authorizationHeader.substring("Bearer".length()).trim();
			blackList.invalidateToken(token);
			return Response.status(Response.Status.OK).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	
	@POST
	@Path("iscrizione")
	@Produces(MediaType.APPLICATION_JSON)
	public Response signUpForCourse(UtenteCorsoIscrizioneDto utenteCorsoIscrizioneDto) {
		try {
			utenteService.signUpForCourse(utenteCorsoIscrizioneDto);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	
	
	
	private UtenteLoginResponseDto issueToken(String email) {
		// Definiamo una chiave segreta (array di byte) necessario poi per la crittografia HMAC
		byte[] secretKey = "ciaooooooo235465t4g4t4htbvt4h5yng5y123435443423234".getBytes();
		// Creiamo una chiave per l'algoritmo HMAC
		Key key = Keys.hmacShaKeyFor(secretKey);
		// Otteniamo le informazioni dell'utente dal servizio
		Utente infoUtente = utenteService.getUtenteByEmail(email);
		// Creiamo un insieme di informazioni da includere nel token (claims) [ coppia <"nome", valore> ]
		Map<String, Object> map = new HashMap<>();
		map.put("email", email);
		map.put("nome", infoUtente.getNome());
		map.put("cognome", infoUtente.getCognome());
		
		List<String> ruoli = new ArrayList<>();
		infoUtente.getRuoli().forEach(ruolo -> ruoli.add(ruolo.getTipologia().name())); //estrae gli enum e li mette nella lista di stringhe
		map.put("ruoli", ruoli);
		
		// Definiamo la data di creazione e il tempo di vita del token
		Instant now = Instant.now();
		Date creationDate = Date.from(now); 					//data creazione
		Date end = Date.from(now.plus(15, ChronoUnit.MINUTES)); //TTL di 15min
		
		// Creiamo il token JWT firmato con la chiave segreta
		String jwtToken = Jwts.builder()
		    .setClaims(map)                     // Impostiamo le informazioni aggiuntive (claims) nel token
		    .setIssuer("http://localhost:8080") // Indichiamo chi ha emesso il token
		    .setIssuedAt(creationDate)          // Impostiamo la data di creazione del token
		    .setExpiration(end)                 // Impostiamo la scadenza del token
		    .signWith(key)                      // Firmiamo il token con la chiave segreta
		    .compact();                         // Compattiamo il token in una stringa

		// Creiamo un oggetto UtenteLoginResponseDto per contenere il token e altre informazioni
		UtenteLoginResponseDto token = new UtenteLoginResponseDto();
		token.setToken(jwtToken);                   // Impostiamo il token JWT
		token.setTokenCreationTime(creationDate);    // Impostiamo il timestamp di creazione del token
		token.setTtl(end);                          // Impostiamo il tempo di vita del token

		// Restituiamo il token JWT e altre informazioni associate
		return token;
	}

	
	
	
	
}
