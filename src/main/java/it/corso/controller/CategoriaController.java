package it.corso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import it.corso.dto.CategoriaDto;
import it.corso.service.CategoriaService;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/categoria")
public class CategoriaController {
	
	
	@Autowired
	private CategoriaService categoriaService;
	
	
	@POST
	@Path("/crea")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCategory(@RequestBody CategoriaDto categoriaDto){
		try {
			categoriaService.createCategory(categoriaDto);
			return Response.status(Response.Status.OK).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	
	
	@PUT
	@Path("/aggiorna")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCategory(@RequestBody CategoriaDto categoriaDto) {
		try {
			categoriaService.updateCategory(categoriaDto);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}
	
	
	
	@DELETE
	@Path("/elimina/{id}")
	public Response deleteCategory(@PathParam("id") int id){
		try {
			categoriaService.deleteCategory(id);
			return Response.status(Response.Status.OK).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	
	
	@GET
	@Path("/info/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectCategory(@PathParam("id") int id){
		try {
			CategoriaDto categoriaDto = categoriaService.getCategory(id);
			if (categoriaDto != null) {
				return Response.status(Response.Status.OK).entity(categoriaDto).build();
			}
	
			return Response.status(Response.Status.NOT_FOUND).build();
	
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
	}
	
	

	
	
	@GET
	@Path("/categorie")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCategories(){
		try {
			List<CategoriaDto> listaCategorie = categoriaService.getAllCategories();
			if(!listaCategorie.isEmpty()) {
				return Response.status(Response.Status.OK).entity(listaCategorie).build();
			}
			return Response.status(Response.Status.NOT_FOUND).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	
	
	@GET
	@Path("/categorie/{nomeCategoria}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCategoriesContaining(@PathParam("nomeCategoria") String nomeCategoria){
		try {
			List<CategoriaDto> listaCategorie = categoriaService.getAllCategoriesContaining(nomeCategoria);
			if(!listaCategorie.isEmpty()) {
				return Response.status(Response.Status.OK).entity(listaCategorie).build();
			}
			
			return Response.status(Response.Status.NOT_FOUND).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	

	
	
}
