package org.example;

import java.util.Date;


public class Inscripcion {

    Materia materia;

    Alumno alumno;

    Date fecha = new Date();

    public Inscripcion() {
    }

    public Inscripcion(Materia materia, Alumno alumno) {
        this.materia = materia;
        this.alumno = alumno;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void validar(Alumno alumno, Materia materia) {
        if(alumno.getMateriasAprobadas().containsAll(materia.getCorrelativas())){
            System.out.println("El alumno puede inscribirse a esta materia");
        } else {
            System.out.println("El alumno no puede inscribirse a esta materia ya que no aprobo todas las correlativas a la misma");
        }
    };


    public void prueba () {

    }


}