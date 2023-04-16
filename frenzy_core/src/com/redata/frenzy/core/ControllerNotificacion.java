
package com.redata.frenzy.core;

import com.redata.frenzy.db.ConexionMySQL;
import com.redata.frenzy.model.Notificacion;
import com.redata.frenzy.model.Persona;
import com.redata.frenzy.model.Usuario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GTO
 */
public class ControllerNotificacion {
    
    public int insert(Notificacion n) throws SQLException {
        String query = "{call insertarNotificacion(?, ?, ?, ?)}"; //Datos de retorno

        int idNotificacionGenerado = -1;
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
        cstmt.setInt(1, n.getUsuario().getIdUsuario());
        cstmt.setString(2, n.getTitulo());
        cstmt.setString(3, n.getCuerpo());
        

        //Registramos los parámetros de salida:
        //Types se importa de sql y se le define el tipo de datos de sql
        cstmt.registerOutParameter(4, Types.INTEGER);

        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idNotificacionGenerado = cstmt.getInt(4);

        n.setIdNotificacion(idNotificacionGenerado);

        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Usuario generado:
        return idNotificacionGenerado;
    }

    public List<Notificacion> getAll(int idUsuario) throws Exception {
        // La consulta SQL a ejecutar:
        String query = "SELECT * FROM notis";

        // Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        // Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        // Con este objeto ejecutaremos la consulta:
        // Para ejecutar instrucciones SQL
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();

        // Cada registro de empleado lo agregamos a una lista
        List <Notificacion> notificaciones = new ArrayList<>();
        

        while (rs.next()) {
            notificaciones.add(fill(rs));
        }
        System.out.println(notificaciones.size());
        // Cerramos conexión
        rs.close();
        pstmt.close();
        connMySQL.close();

        return notificaciones;
    }

    private Notificacion fill(ResultSet rs) throws Exception {
        Notificacion n = new Notificacion();
        Usuario u = new Usuario();
        //Le establecemos a persona los valores
        n.setIdNotificacion(rs.getInt("idNotificaciones"));
        n.setTitulo(rs.getString("titulo"));
        n.setCuerpo(rs.getString("cuerpo"));
        
        u.setIdUsuario(rs.getInt("idUsuario"));

        n.setUsuario(u);
        //Devolvemos cliente
        return n;
    }
    
    public void delete(int idNotificacion) throws SQLException {
        String query = "{call eliminarNotificacion(?)}"; 

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
        cstmt.setInt(1, idNotificacion);

        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }
}
