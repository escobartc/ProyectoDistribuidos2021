package com.usuarios;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.List;
import java.util.Scanner;

import com.servidores.server;

import org.json.simple.parser.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;


public class Aspirante {

int id;
int contraseña;
int experiencia;
int[] sector;
String habilidad;
String formacion;





    public int[] getSector() {
    return sector;
}






public String getFormacion() {
        return formacion;
    }






    public void setFormacion(String formacion) {
        this.formacion = formacion;
    }






public Aspirante(int id, int contraseña, int experiencia, int[] sector, String habilidad, String formacion) {
        this.id = id;
        this.contraseña = contraseña;
        this.experiencia = experiencia;
        this.sector = sector;
        this.habilidad = habilidad;
        this.formacion = formacion;
    }






public Aspirante(int id, int contraseña, int experiencia, int[] sector, String habilidad) {
        this.id = id;
        this.contraseña = contraseña;
        this.experiencia = experiencia;
        this.sector = sector;
        this.habilidad = habilidad;
    }


public String getHabilidad() {
        return habilidad;
    }

    public void setHabilidad(String habilidad) {
        this.habilidad = habilidad;
    }


public void setSector(int[] sector) {
    this.sector = sector;
}


public int getId() {
    return id;
}



public void setId(int id) {
    this.id = id;
}



public int getContraseña() {
    return contraseña;
}



public void setContraseña(int contraseña) {
    this.contraseña = contraseña;
}



public int getExperiencia() {
    return experiencia;
}


public void setExperiencia(int experiencia) {
    this.experiencia = experiencia;
}


    public Aspirante(int id, int contraseña, int experiencia, int[] sector) {
    this.id = id;
    this.contraseña = contraseña;
    this.experiencia = experiencia;
    this.sector = sector;
}


    public static void main(String[] args) {
        server s = new server();
        Aspirante aspi = null;
        String options = null;
        String options2 = "0";
        String eleccion = null;
        int sectores[] = new int[2];
        int iduser;
        int contraseña;
        boolean logueado = false;
        Scanner scan = new Scanner(System.in); // entrada
        do {
            try(ZContext contexto = new ZContext()){
            System.out.println("Bienvenido al sistema de bolsa de empleo, usted ingresó como aspirante");
            System.out.println("1) Registrarse y suscribirse a sector(es)");
            System.out.println("2) Iniciar sesión");
            System.out.println("3) Recibir ofertas");
            System.out.println("4) Consultar citas (En caso de ser compatible con una oferta, se le asignará una cita)");
            System.out.println("0) Salir");
            options = scan.next();
            switch (options) {
                case "1":
                if(!logueado){
                    System.out.println("Ingrese codigo de identificación: ");
                    iduser = scan.nextInt();
                    System.out.println("Ingrese contraseña: ");
                    contraseña = scan.nextInt();
                    if(!s.existe(Integer.toString(iduser), Integer.toString(contraseña))){
                        System.out.println("Ingrese Años de experiencia: ");
                        int años = scan.nextInt();
                        System.out.println("Ingrese el número del primer sector al que se desea inscribir, los sectores son:");
                        System.out.println("1) Sector 1");
                        System.out.println("2) Sector 2");
                        System.out.println("3) Sector 3");
                        System.out.println("4) Sector 4");
                        System.out.println("5) Sector 5");
                        sectores[0] = scan.nextInt();
                        System.out.println("Suscrito en el sector " + sectores[0]);
                        System.out.println("Ingrese el número del segundo sector..");
                        sectores[1] = scan.nextInt();
                        System.out.println("Suscrito en el sector " + sectores[1]);
                        System.out.println("Ingrese habilidad distintiva: (Lider/Agil/Comunicativo/Eficaz)");
                        String hab = scan.next();
                        System.out.println("Ingrese formación académica (Bachiller/Tecnico/Tecnologo/Profesional):");
                        String f = scan.next();
                         aspi = new Aspirante(iduser, contraseña, años, sectores,hab,f);
                        s.registrarAspirante(aspi);
                        logueado = true;
                        System.out.println("Usuario creado");
                    } else {System.out.println("Ya está registrado un usuario con ese codigo");}
                    
                } else { System.out.println("Ya está registrado");}
                    break;

                case "2":
                    //Validar que existe el usuario
                    //Si existe, continúa.
                    //tomar aspirante de la base de datos
                    if(!logueado){
                        System.out.println("Ingrese codigo de identificación: ");
                        String usuario = scan.next();
                        System.out.println("Ingrese contraseña: ");
                        String contra = scan.next();
                        if(s.existe(usuario, contra)){
                             aspi = s.obtenerAsp(usuario, contra);
                             System.out.println("Usuario verificado: " + aspi.getId());
                             logueado = true;
                        }
                        else {System.out.println("Los datos de inicio son incorrectos");}
                    } else {System.out.println("Ya está logueado");}
                    
                    break;
                   

                
                    case "3":
                    if(logueado){
                        
                        System.out.println("Esperando ofertas filtradas");
                        ZMQ.Socket suscriptor = contexto.createSocket(SocketType.SUB);
                        try {
                        InetAddress a = InetAddress.getByName("25.4.120.237");
                         if (a.isReachable(2000)) {
                            System.out.println("Conexión a FILTRO");
                            suscriptor.connect("tcp://25.4.120.237:3333"); // IP FILTRO
                            suscriptor.subscribe("Sector: " + aspi.getSector()[0]);
                            suscriptor.subscribe("Sector: " + aspi.getSector()[1]);
                           while(!options2.equals("1")){
                            String mensaje = suscriptor.recvStr();
                            System.out.println(mensaje);
                            Thread.sleep(1000);
                            System.out.println("Presione 1 para salir, cualquier otra cosa para continuar");
                            options2 = scan.next();
                           }
                           options2 = "0";
                         } else {
                            System.out.println("Conexión a BACKUP"); 
                            suscriptor.connect("tcp://25.57.158.0:4444");// IP BACKUP
                            suscriptor.subscribe("Sector: " + aspi.getSector()[0]);
                            suscriptor.subscribe("Sector: " + aspi.getSector()[1]);
                           while(!options2.equals("1")){
                            String mensaje = suscriptor.recvStr();
                            System.out.println(mensaje);
                            Thread.sleep(1000);
                            System.out.println("Presione 1 para salir, cualquier otra cosa para continuar");
                            options2 = scan.next();
                           }
                           options2 = "0";
                        }
                    } catch (Exception e) {
                        //TODO: handle exception
                    }
                    } else {System.out.println("Debe iniciar sesión para recibir ofertas");}
                        
                            
                    
                    break;


                    case "4":
                    if(logueado){
                        //Leer las citas disponibles para este usuario
                        s.buscarOferta(aspi);
                        System.out.println("-----------------------------------------");
                        s.imprimirCitas(aspi.getId());
                        System.out.println("-----------------------------------------");

                    } else {System.out.println("Debe iniciar sesión para recibir ofertas");}

            }
        }
        catch(Exception e){

        }
    } while (!options.equals("0")); // salir
    }

    


		
          }
          
    
    

    

