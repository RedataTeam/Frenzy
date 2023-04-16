/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redata.frenzy.model;

public class Notificacion {
    int idNotificacion;
    Usuario usuario;
    String titulo;
    String cuerpo;

    public Notificacion() {
    }

    public Notificacion(int idNotificacion, Usuario usuario, String titulo, String cuerpo) {
        this.idNotificacion = idNotificacion;
        this.usuario = usuario;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
    }

    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }
}
