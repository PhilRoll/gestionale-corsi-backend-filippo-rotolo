package it.corso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import it.corso.dto.CorsoDto;
import it.corso.jwt.JWTTokenNeeded;
import it.corso.jwt.Secured;
import it.corso.service.CorsoService;

@Path("/corso")
@Secured(role = "Admin")
@JWTTokenNeeded
public class CorsoController {

	@Autowired
	private CorsoService corsoService;
	
	
	@GET
	@Path("/corsi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCorsi() {

		try {

			List<CorsoDto> listaCorsi = corsoService.getAllCorsi();
			return Response.status(Response.Status.OK).entity(listaCorsi).build();

		} catch (Exception e) {

			return Response.status(Response.Status.BAD_REQUEST).entity("Errore caricamento utenti").build();
		}
	}
	
	
	@POST
	@Path("/crea")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCorso(CorsoDto corsoDto){
		try {
			corsoService.createCorso(corsoDto);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	
	
	@GET
	@Path("elimina/{id}")
	public Response deleteCorso(@PathParam("id") int id){
		try {
			corsoService.deleteCorso(id);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	
	

}
