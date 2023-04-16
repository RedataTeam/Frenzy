
package com.redata.frenzy.rest;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.redata.frenzy.core.ControllerNotificacion;
import com.redata.frenzy.core.ControllerUsuario;
import com.redata.frenzy.model.Notificacion;
import com.redata.frenzy.model.Usuario;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;



@Path("notificaciones")
public class RESTNotificacion {
    
    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosNotificacion") @DefaultValue("") String datosNotificacion) {
        String out = null;
        Gson gson = new Gson();
        Notificacion notificaciones = null;
        ControllerNotificacion cn = new ControllerNotificacion();
        try {
            notificaciones = gson.fromJson(datosNotificacion, Notificacion.class);
            cn.insert(notificaciones);
            
            out = Integer.toString(notificaciones.getIdNotificacion());
        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = "{\"exception\":\"Error en los datos introducidos o en el formato.\"}";
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("idUsuario") @DefaultValue("0") int idUsuario) {
        String out = null;
        ControllerNotificacion cn = null;
        List<Notificacion> notificaciones = null;
        try {
            cn = new ControllerNotificacion();
            notificaciones = cn.getAll(idUsuario);
            out = new Gson().toJson(notificaciones);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("filtro") @DefaultValue("") String filtro,
                            @FormParam("idNotificacion") @DefaultValue("0") int idNotificacion) {
        String out = null;
        ControllerNotificacion ce = null;
        try {
            ce = new ControllerNotificacion();
            ce.delete(idNotificacion);
            out = new Gson().toJson("si");
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
}
