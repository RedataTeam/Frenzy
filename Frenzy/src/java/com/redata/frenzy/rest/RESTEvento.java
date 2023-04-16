
package com.redata.frenzy.rest;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.redata.frenzy.core.ControllerEvento;
import com.redata.frenzy.model.Evento;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("evento")
public class RESTEvento {
    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosEvento") @DefaultValue("") String datosEvento) {
        String out = null;
        Gson gson = new Gson();
        Evento evento = null;
        ControllerEvento cu = new ControllerEvento();
        System.out.println(datosEvento);

        try {
            evento = gson.fromJson(datosEvento, Evento.class);
            if (evento.getIdEvento()== 0) {
                cu.insert(evento);
            } else {
                cu.update(evento);
            }
            
            out = Integer.toString(evento.getIdEvento());
        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = "{\"exception\":\"Error en los datos introducidos o en el formato.\"}";
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    @POST
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@FormParam("usuario") @DefaultValue("") String usuario, @FormParam("etiqueta") @DefaultValue("") String etiqueta) {
        String out = null;
        ControllerEvento ce = null;
        List<Evento> eventos = null;
        try {
            ce = new ControllerEvento();
            eventos = ce.getAll(usuario, etiqueta);
            out = new Gson().toJson(eventos);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @POST
    @Path("asistir")
    @Produces(MediaType.APPLICATION_JSON)
    public Response asistir(@FormParam ("idEvento") @DefaultValue("") String idEvento, @FormParam("idUsuario") @DefaultValue("") String idUsuario) throws SQLException{
        String out = null;
        ControllerEvento ce = null;
        try{
            ce = new ControllerEvento();
            ce.asistir(idEvento, idUsuario);
            out = "{\"correcto\":\"asistencia registrada correctamente.\"}";
        } catch(Exception e){
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @POST
    @Path("eliminarAsistencia")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarAsistencia(@FormParam ("idEvento") @DefaultValue("") String idEvento, @FormParam("idUsuario") @DefaultValue("") String idUsuario) throws SQLException{
        String out = null;
        ControllerEvento ce = null;
        try{
            ce = new ControllerEvento();
            ce.eliminarAsistencia(idEvento, idUsuario);
            out = "{\"correcto\":\"asistencia eliminada correctamente.\"}";
        } catch(Exception e){
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @POST
    @Path("getEventosApuntado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventosApuntado(@FormParam("usuario") @DefaultValue("") String usuario) {
        String out = null;
        ControllerEvento ce = null;
        List<Evento> eventos = null;
        try {
            ce = new ControllerEvento();
            eventos = ce.eventosApuntado(usuario);
            out = new Gson().toJson(eventos);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @POST
    @Path("getMisEventos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMisEventos(@FormParam("usuario") @DefaultValue("") String usuario) {
        String out = null;
        ControllerEvento ce = null;
        List<Evento> eventos = null;
        try {
            ce = new ControllerEvento();
            eventos = ce.misEventos(usuario);
            out = new Gson().toJson(eventos);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
}
