package com.pfiltro;

import com.usuarios.Empleador;

public class Oferta {
private int id;
private int sector;
private String nombre;
private int emple;
private String habilidad;
private int xp;
private String formacion;



public String getFormacion() {
    return formacion;
}

public void setFormacion(String formacion) {
    this.formacion = formacion;
}

public Oferta(int id, int sector, String nombre, int emple, String habilidad, int xp, String formacion) {
    this.id = id;
    this.sector = sector;
    this.nombre = nombre;
    this.emple = emple;
    this.habilidad = habilidad;
    this.xp = xp;
    this.formacion = formacion;
}

public String getHabilidad() {
    return habilidad;
}

public void setHabilidad(String habilidad) {
    this.habilidad = habilidad;
}

public int getXp() {
    return xp;
}

public void setXp(int xp) {
    this.xp = xp;
}

public int getEmple() {
    return emple;
}

public void setEmple(int emple) {
    this.emple = emple;
}

public Oferta(int id, int sector, String nombre, int emple) {
    this.id = id;
    this.sector = sector;
    this.nombre = nombre;
    this.emple = emple;
}

public Oferta(int id, int sector, String nombre) {
    this.id = id;
    this.sector = sector;
    this.nombre = nombre;
}

public int getId() {
    return id;
}

public void setId(int id) {
    this.id = id;
}

public int getSector() {
    return sector;
}

public void setSector(int sector) {
    this.sector = sector;
}

public String getNombre() {
    return nombre;
}

public void setNombre(String nombre) {
    this.nombre = nombre;
}


    
}
