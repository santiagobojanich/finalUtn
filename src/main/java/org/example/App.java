package org.example;

import java.sql.SQLException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class App {

    private static Scanner sc = new Scanner(System.in).useDelimiter("\n");
    private static Conexion conexion = new Conexion();


    public static void main(String[] args) throws SQLException, JsonProcessingException {

        int opcion = 0;

        while (opcion != 4) {
            System.out.println("------------BIENVENIDO A SU GESTOR PATROCINADO POR LA UTN------------");
            System.out.println("Seleccione una opción:");
            System.out.println("1. Agregar alumno");
            System.out.println("2. Agregar materia");
            System.out.println("3. Validar Inscripcion");
            System.out.println("4. Salir");

            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    crearAlumno();
                    break;
                case 2:
                    crearMateria();
                    break;
                case 3:
                  validarCorrelatividad();
                    break;
                case 4:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        }
        sc.close();
    }


    public static void crearAlumno() throws SQLException {

        Alumno alumno = new Alumno();
        System.out.println("Ingrese el nombre del alumno");
        String nombre = sc.next();
        alumno.setNombre(nombre);

        int legajo;
        String legajoStr;
        Pattern pattern = Pattern.compile("^\\d{5}$"); // regex para verificar 5 dígitos

        do {
            System.out.println("Ingrese el legajo del alumno (debe tener 5 dígitos)");
            legajoStr = sc.next();
            Matcher matcher = pattern.matcher(legajoStr);
            if (matcher.matches()) {
                legajo = Integer.parseInt(legajoStr);
                alumno.setLegajo(legajo);
                break;
            } else {
                System.out.println("El legajo debe tener 5 dígitos.");
            }
        } while (true);

        String option;
        ArrayList<String> aprobadas = new ArrayList<>();

        do {
            System.out.println("Ingrese las materias aprobadas del alumno");
            String apro = sc.next();
            aprobadas.add(apro);
            System.out.println("Desea ingresar otra materia aprobada?");
            System.out.println("-S / -N");
            option = sc.next().toLowerCase();
        } while (option.equals("s"));

        String aprobadasJson = new Gson().toJson(aprobadas);

        conexion.abrirConexion();
        Statement stmt = conexion.conectar.createStatement();
        stmt.executeUpdate("INSERT INTO alumnos VALUES(\"" + nombre + "\",'" + legajo + "', '" + aprobadasJson + "');");
        conexion.cerrarConexion();
        System.out.println("Alumno agregado con exito");
    }

    public static Alumno getAlumno(int legajo) throws SQLException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        conexion.abrirConexion();

        Statement stmt = conexion.conectar.createStatement();
        //busqueda por legajo para evitar nombres repetidos
        ResultSet rs = stmt.executeQuery("SELECT * FROM alumnos WHERE legajo=" + legajo + ";");
        if (!rs.next()) {
            conexion.cerrarConexion();
            return null;
        }
        Alumno alumno = new Alumno(rs.getString("nombre"), rs.getInt("legajo"));

        String jsonText = mapper.writeValueAsString(rs.getString("materias_aprobadas"));
        ArrayList<String> nombreCorrelativas = mapper.readValue(jsonText, ArrayList.class);
        alumno.setMateriasAprobadas(nombreCorrelativas);

        conexion.cerrarConexion();

        return alumno;
    }

    public static void crearMateria() throws SQLException {

        Materia materia = new Materia();

        System.out.println("Que nombre quiere que tenga la materia?");
        String nombre = sc.next();
        materia.setNombre(nombre);

        System.out.println("Cuantas correlativas tiene?");

        int numero = sc.nextInt();

        System.out.println("Que materias desea agregar a las correlativas?");
        ArrayList<String> correlativas = new ArrayList<>();

        String corre;

        for (int i = 0; i < numero; i++) {
            corre = sc.next();
            correlativas.add(corre);
        }

        String correlativasJson = new Gson().toJson(correlativas);

        conexion.abrirConexion();
        Statement stmt = conexion.conectar.createStatement();
        stmt.executeUpdate("INSERT INTO materias VALUES(\"" + nombre + "\",'" + correlativasJson + "');");
        conexion.cerrarConexion();
        System.out.println("Materia creada con exito");
    }


    public static Materia getMateria(String nombre) throws SQLException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        conexion.abrirConexion();
        Statement stmt = conexion.conectar.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM materias WHERE nombre=\"" + nombre + "\";");
        if (!rs.next()) {
            conexion.cerrarConexion();
            return null;
        }
        Materia materia = new Materia(rs.getString("nombre"));

        materia.setCorrelativas(mapper.readValue(mapper.writeValueAsString(rs.getString("correlativas")), ArrayList.class));

        conexion.cerrarConexion();

        return materia;
    }

    ;

    public static void validarCorrelatividad() throws SQLException, JsonProcessingException {

        System.out.println("Ingrese el legajo del alumno a inscribir");
        int legajo = sc.nextInt();
        System.out.println("A que materia quiere inscribirlo?");
        String materia = sc.next();

        Inscripcion insc = new Inscripcion();
        Alumno alumno = getAlumno(legajo);
        Materia mat = getMateria(materia);
        if (alumno != null && mat != null) {
            insc.validar(alumno, mat);
        } else {
            System.out.println("Alumno o materia inexistente");
        }


    }

}

