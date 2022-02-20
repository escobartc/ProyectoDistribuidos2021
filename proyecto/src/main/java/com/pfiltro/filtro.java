package com.pfiltro;
import com.servidores.*;
import com.usuarios.*;
import java.util.ArrayList; 
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import java.io.*;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
public class filtro {

    private ArrayList<Oferta> ofertas;

    public ArrayList<Oferta> getOfertas() {
        return ofertas;
    }

    public void setOfertas(ArrayList<Oferta> ofertas) {
        this.ofertas = ofertas;
    }

    public filtro(ArrayList<Oferta> ofertas) {
        this.ofertas = ofertas;
    }

    public static void main(String[] args) {
        int nofertas = 0;
        ArrayList<Oferta> ofertasrecibidas = new ArrayList<>();
         int jonas = 0;
         int servidor = 0;
                  
         while(true){

            try(ZContext contexto = new ZContext()){
            ZMQ.Socket suscriptor = contexto.createSocket(SocketType.SUB);
            suscriptor.connect("tcp://25.6.14.194:2099"); //IP Empleador 25.6.14.194
            suscriptor.subscribe("");
            System.out.println("Esperando ofertas del empleador");
            String mensaje = suscriptor.recvStr();
            System.out.println("Oferta recibida: " + mensaje);

                Thread.sleep(1000); 
                if(nofertas%2==0){
                System.out.println(mensaje);
                System.out.println("Se envía al servidor 1");
                ZMQ.Socket sus = contexto.createSocket(SocketType.REQ); //Porque con el server la comunicación es request/reply
                sus.connect("tcp://25.6.14.194:2100"); //Aquí va la dir. Hamachi Server 1
                sus.send(mensaje); //Envía el mensaje
                String respuesta = sus.recvStr();
                System.out.println("Respuesta del server: " + respuesta);
                Thread.sleep(1000); 
                ZMQ.Socket publicador = contexto.createSocket(SocketType.PUB);
                publicador.bind("tcp://25.4.120.237:3333");  // IP ASPIRANTE
                while(jonas<2){
                    publicador.send(respuesta); 
                    jonas++;
                    Thread.sleep(1000);
                }
                
                jonas = 0;
                nofertas++;
                } else {
                    System.out.println(mensaje);
                System.out.println("Se envía al servidor 2");
                ZMQ.Socket sus = contexto.createSocket(SocketType.REQ); //Porque con el server la comunicación es request/reply
                sus.connect("tcp://25.57.158.0:9999"); //Aquí va la dir. Hamachi server 2
                sus.send(mensaje); //Envía el mensaje
                String respuesta = sus.recvStr();
                System.out.println("Respuesta del server: " + respuesta);
                Thread.sleep(1000); 
                ZMQ.Socket publicador = contexto.createSocket(SocketType.PUB);
                publicador.bind("tcp://25.4.120.237:3333"); // IP ASPIRANTE
                while(jonas<2){
                    publicador.send(respuesta); 
                    jonas++;
                    Thread.sleep(1000);
                }
                
                jonas = 0;
                nofertas++;
                }
                
                
            }
                 
        catch(Exception e){

        }
    }

    }


public static void gestionarOfertas(String oferta){
    
    

    

}

    

}