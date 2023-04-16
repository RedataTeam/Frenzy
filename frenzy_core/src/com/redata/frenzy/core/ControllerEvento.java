
package com.redata.frenzy.core;

import com.redata.frenzy.db.ConexionMySQL;
import com.redata.frenzy.model.Evento;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


public class ControllerEvento {
    public int insert(Evento e) throws SQLException{
        String query = "{call insertarEvento(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //Datos del Evento
                + "?)}"; //Datos de retorno
        
        int idEventoGenerado = -1;
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        //Creamos un callableStatement pasandole la llamada 
        //Para ejecutar instrucciones NO SQL
        CallableStatement cstmt = conn.prepareCall(query);
        
        //Establecemos los valores de los parámetros de los datos personales 
        //en el orden en que los pide el procedimiento almacenado, 
        //comenzando en 1:
        //Establecemos los datos en el mismo orden que tenemos en la base de datos
        cstmt.setInt(1, e.getIdUsuario());
        cstmt.setString(2, e.getNombre());
        cstmt.setString(3, e.getUbicacion());
        cstmt.setString(4, e.getFecha());
        cstmt.setString(5, e.getHoraInicio());
        cstmt.setString(6, e.getHoraTermino());
        cstmt.setString(7, e.getMotivo());
        cstmt.setString(8, e.getDescripcion());
        cstmt.setDouble(9, e.getCosto());
        cstmt.setString(10, e.getFotografia());
        cstmt.setString(11, e.getPrivacidad());
        cstmt.setString(12, e.getEtiquetas());
        cstmt.setBoolean(13, e.isEstatus());
        
        //Registramos los parámetros de salida:
        //Types se importa de sql y se le define el tipo de datos de sql
        cstmt.registerOutParameter(14, Types.INTEGER);
        
        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idEventoGenerado = cstmt.getInt(14);

        e.setIdEvento(idEventoGenerado);
                
        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Usuario generado:
        return idEventoGenerado;
    }
    
    public void update(Evento e) throws SQLException{
        String query = "{call actualizarEvento(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}"; //Datos del Evento
        
        
        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(query);
        
        cstmt.setString(1, e.getNombre());
        cstmt.setString(2, e.getUbicacion());
        cstmt.setString(3, e.getFecha());
        cstmt.setString(4, e.getHoraInicio());
        cstmt.setString(5, e.getHoraTermino());
        cstmt.setString(6, e.getMotivo());
        cstmt.setString(7, e.getDescripcion());
        cstmt.setDouble(8, e.getCosto());
        cstmt.setString(9, e.getFotografia());
        cstmt.setString(10, e.getPrivacidad());
        cstmt.setString(11, e.getEtiquetas());
        cstmt.setBoolean(12, e.isEstatus());
        
        
        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }
    
    public List<Evento> getAll(String usuario, String etiqueta) throws Exception {
        //La consulta SQL a ejecutar:
        String query = "SELECT * FROM evento where idEvento not in (select idEvento from asistentes where idUsuario =" + usuario +
                        ") and estatus = 1 and etiquetas like '%"+etiqueta+"%'" + " AND idUsuario != "+usuario; 
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la consulta:
        //Para ejecutar instrucciones SQL
        PreparedStatement pstmt = conn.prepareStatement(query);

        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();

        //Cada registro de empleado lo agregamos a una lista
        List<Evento> eventos = new ArrayList<>();

        while (rs.next()) {
            eventos.add(fill(rs));
        }

        //Cerramos conexion
        rs.close();
        pstmt.close();
        connMySQL.close();

        return eventos;
    }
    
    private Evento fill(ResultSet rs) throws Exception {
        Evento e = new Evento();

        //Le establecemos a persona los valores
        e.setIdEvento(rs.getInt("idEvento"));
        e.setIdUsuario(rs.getInt("idUsuario"));
        e.setNombre(rs.getString("nombre"));
        e.setUbicacion(rs.getString("ubicacion"));
        e.setFecha(rs.getString("fecha"));
        e.setHoraInicio(rs.getString("horaInicio"));
        e.setHoraTermino(rs.getString("horaTermino"));
        e.setMotivo(rs.getString("motivo"));
        e.setDescripcion(rs.getString("descripcion"));
        e.setCosto(rs.getDouble("costo"));
        e.setFotografia(rs.getString("fotografia"));
        e.setPrivacidad(rs.getString("privacidad"));    
        e.setEtiquetas(rs.getString("etiquetas"));
        e.setEstatus(rs.getBoolean("estatus"));

        //Devolvemos Evento
        return e;
    }
    
    public void asistir(String idEvento, String idUsuario) throws SQLException{
        String query = "INSERT INTO asistentes VALUE("+idEvento+","+idUsuario+")"; 
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.execute();

        pstmt.close();
        connMySQL.close();
    }
    
    public void eliminarAsistencia(String idEvento, String idUsuario) throws SQLException{
        String query = "DELETE FROM asistentes WHERE idEvento = "+idEvento+" AND idUsuario = "+idUsuario;
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.execute();

        pstmt.close();
        connMySQL.close();
    }
    
    public List<Evento> eventosApuntado(String idUsuario) throws SQLException, Exception{
        String query = "SELECT * FROM evento WHERE idEvento in (SELECT idEvento FROM asistentes WHERE idUsuario = "+idUsuario+")";
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();
        List<Evento> eventos = new ArrayList<>();

        while (rs.next()) {
            eventos.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return eventos;
    }
    
    public List<Evento> misEventos(String idUsuario) throws SQLException, Exception{
        String query = "SELECT * FROM evento WHERE idUsuario = "+idUsuario;
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();
        List<Evento> eventos = new ArrayList<>();

        while (rs.next()) {
            eventos.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return eventos;
    }
}
