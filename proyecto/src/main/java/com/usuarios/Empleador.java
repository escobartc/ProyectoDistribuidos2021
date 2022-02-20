
package com.usuarios;
import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;

import javax.print.attribute.standard.RequestingUserName;

import java.util.ArrayList; // import the ArrayList class
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;  // Import the File class
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.PrintWriter;
import java.net.InetAddress;

import com.pfiltro.Oferta;

import org.json.JSONObject;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import com.servidores.server;
import com.pfiltro.*;

public class Empleador 
{
    private int id;
    private String contraseña;
    private ArrayList<Oferta> ofertas;

    //private String nombre;

    
    public Empleador(int id, String contraseña, ArrayList<Oferta> ofertas) {
        this.id = id;
        this.contraseña = contraseña;
        this.ofertas = ofertas;
    }


    public Empleador(int id, String contraseña) {
        this.id = id;
        this.contraseña = contraseña;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getContraseña() {
        return contraseña;
    }


    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }


    public ArrayList<Oferta> getOfertas() {
        return ofertas;
    }


    public void setOfertas(ArrayList<Oferta> ofertas) {
        this.ofertas = ofertas;
    }


    public static void main( String[] args )
    {
        server s = new server();
        Empleador emple = null;
        boolean logueado = false;
        int jonas = 0;
        String options = null;
        int id;
        String contra;
        Scanner scan = new Scanner(System.in); // entrada
        do {
            System.out.println("Bienvenido al sistema de bolsa de empleo, usted ingresó como empleador");
            System.out.println("1) Iniciar sesión");
            System.out.println("2) Registrarse");
            System.out.println("3) Publicar oferta");
            System.out.println("0) Salir");
            options = scan.next();
            
            switch (options) {

                case "1":
                if(!logueado)
                {
                    System.out.println("Ingrese su ID:");
                    String cod = scan.next();
                    System.out.println("Ingrese su contraseña:");
                    String pw = scan.next();
                    if(s.existeE(cod, pw)){
                        emple = s.obtenerEmp(cod, pw);
                        System.out.println("Inició sesión correctamente");
                        logueado = true;
                    }
                    else {System.out.println("Los datos son incorrectos o el usuario no existe");}
                } else {System.out.println("Ya está logueado");}
                break;     
                
                case "2":
                /*Validar si este usuario ya está
                Si el usuario ya esta logueado, avisar
                si no, continúael registro
                */
                if(!logueado){
                    System.out.println("Ingrese su ID: ");
                    id = scan.nextInt();
                    System.out.println("Ingrese su contraseña: ");
                    contra = scan.next();
                    if(!s.existeE(Integer.toString(id), contra)){
                        ArrayList<Oferta> ofertascreadas = new ArrayList<>();
                        emple = new Empleador(id,contra,ofertascreadas);
                        s.registrarEmpleador(emple);
                        logueado = true;
                        System.out.println("Registro exitoso");
                    } else { System.out.println("El usuario ya existe");}
                    
                } else { System.out.println("Ya está registrado");}
                
                break;


                case "3":
                if(logueado){

                    int idoferta = s.generarIDoferta();
                    System.out.println("Ingrese Sector (1-5): ");
                    int sectoroferta = scan.nextInt();
                    System.out.println("Ingrese nombre de la vacante: ");
                    String vacante = scan.next();
                    System.out.println("Ingrese habilidad requerida (Lider/Agil/Comunicativo/Eficaz): ");
                    String skill = scan.next();
                    System.out.println("Ingrese nivel de educación requerido (Bachiller/Tecnico/Tecnologo/Profesional): ");
                    String edu = scan.next();
                    System.out.println("Ingrese experiencia requerida en años: ");
                    int exp = scan.nextInt();
                    Oferta of = new Oferta(idoferta,sectoroferta,vacante,emple.getId(),skill,exp,edu);
                    s.registrarOferta(of);

                    try(ZContext contexto = new ZContext()){
                        System.out.println("Enviando oferta al servidor a través del filtro...");
                        
                        ZMQ.Socket publicador = contexto.createSocket(SocketType.PUB);

                        //Primero se verifica que haya conexión Empleador - Filtro

        try {
            InetAddress a = InetAddress.getByName("25.4.120.237"); //IP filtro
            if (a.isReachable(2000)) {
            System.out.println("Conexión a filtro: UP");
            publicador.bind("tcp://25.6.14.194:2099"); //IP EMPLE
            String mensaje = "Sector: " + of.getSector() + " Vacante: " + of.getNombre();
            while(jonas<2){
                publicador.send(mensaje); 
                jonas++; 
                Thread.sleep(1000);
            }
            System.out.println("Oferta creada: " + mensaje);

            }else{
                System.out.println("Conexión a filtro: DOWN, conectando a BACKUP"); //IP BACKUP
                publicador.bind("tcp://25.6.14.194:5555"); //IP EMPLE
            String mensaje = "Sector: " + of.getSector() + " Vacante: " + of.getNombre();
            while(jonas<2){
                publicador.send(mensaje); 
                jonas++; 
                Thread.sleep(1000);
            }
            System.out.println("Oferta creada: " + mensaje);

            }
        } catch (Exception e) {
            //TODO: handle exception
        }


                    }
                    catch(Exception e){ 
                    } 
                } else {System.out.println("Debe iniciar sesión para ejecutar esta acción");}
                jonas=0;
                break;

               
               
                    
        
            }
        } while (!options.equals("0")); // salir
       
    }

        
}
