package org.example;

import java.util.ArrayList;

public class Materia {

    String nombre;

    ArrayList<String> correlativas = new ArrayList<>();

    public Materia(String nombre) {
        this.nombre = nombre;
    }

    public Materia() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<String> getCorrelativas() {
        return correlativas;
    }

    public void setCorrelativas(ArrayList<String> correlativas) {
        this.correlativas = correlativas;
    }

    @Override
    public String toString() {
        return "Materia{" + "nombre=" + nombre + ", correlativas=" + correlativas + '}';
    }



}
