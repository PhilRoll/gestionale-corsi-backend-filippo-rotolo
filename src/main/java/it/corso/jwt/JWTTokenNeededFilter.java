package it.corso.jwt;

import java.io.IOException;
import java.security.Key;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.corso.service.BlackList;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@JWTTokenNeeded
public class JWTTokenNeededFilter implements ContainerRequestFilter{

	//iniezione delle dipendenze con context anziche autowired
	@Context
	private ResourceInfo resourceInfo; // Informazioni sulle risorse
	
	@Autowired
	private BlackList blackList;	// Lista nera dei token
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		// Controlla se è presente l'annotazione @Secured è presente a livello di metodi (se no, a livello di classe)
		Secured annotationRole = resourceInfo.getResourceMethod().getAnnotation(Secured.class);
		if(annotationRole==null){
			annotationRole = resourceInfo.getResourceClass().getAnnotation(Secured.class);
		}
		
		// Estrae l'autorizzazione dall'header della richiesta
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		
		
		if(authorizationHeader==null || !authorizationHeader.startsWith("Bearer ")){ //se è vuota o non inizia per "Bearer"
			throw new NotAuthorizedException("Authorization header must be provided");
		}
		
		//sottostringa del token pulita
		String token = authorizationHeader.substring("Bearer".length()).trim(); 
		
		// Controlla se il token è presente nella lista nera
		if(blackList.isTokenRevoked(token)){
			throw new NotAuthorizedException("Blacklisted token");
		}
		
		try {
			// Otteniamo la chiave segreta per la decodifica del token (UGUALE A QUELLA DI UTENTE CONTROLLER)
			byte[] secretKey = "ciaooooooo235465t4g4t4htbvt4h5yng5y123435443423234".getBytes();
			// Creiamo una chiave HMAC utilizzando la chiave segreta
			Key key = Keys.hmacShaKeyFor(secretKey);
			
			// Effettuiamo il parsing del token JWT per verificare la sua validità e ottenere le informazioni (claims)
			Jws<Claims> claims = Jwts.parserBuilder()  // Creiamo un parser per il token JWT
			    .setSigningKey(key)                    // Impostiamo la chiave segreta per la verifica della firma del token
			    .build()                               // Costruiamo il parser
			    .parseClaimsJws(token);                // Effettuiamo il parsing del token JWT e otteniamo i claims
			
			// Estraiamo le informazioni (claims) dal corpo del token
			Claims body = claims.getBody();
			List<String> rolesToken = body.get("ruoli", List.class);
			
			// Verifichiamo se l'utente possiede il ruolo richiesto
			Boolean hasRole = rolesToken.contains(annotationRole.role());
			
			// Se l'utente non ha il ruolo richiesto, restituiamo una risposta di errore UNAUTHORIZED
			if(!hasRole){
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			}
		
			
		} catch (Exception e) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
		
		
	}

}
