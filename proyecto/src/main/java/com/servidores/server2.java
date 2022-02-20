package com.servidores;
import com.pfiltro.*;
import com.pfiltro.*;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
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
import com.usuarios.Aspirante;
import com.usuarios.Empleador;
import com.pfiltro.*;

public class server2 {

    
    
    public server2() {
    }

    private ArrayList<Oferta> ofertas;

    public server2(ArrayList<Oferta> ofertas) {
        this.ofertas = ofertas;
    }



    public ArrayList<Oferta> getOfertas() {
        return ofertas;
    }



    public void setOfertas(ArrayList<Oferta> ofertas) {
        this.ofertas = ofertas;
    }




    public static void main(String[] args) {
         
        try (ZContext contexto = new ZContext()) {
            
           
            ZMQ.Socket servidor = contexto.createSocket(SocketType.REP);
            try {
                InetAddress a = InetAddress.getByName("25.4.120.237");
                if (a.isReachable(2000)) {
                servidor.bind("tcp://25.4.120.237:9999"); //IP FILTRO
                while(true){
                System.out.println("Listo para recibir ofertas del FILTRO y almacenarlas: ");
                // RECIBE LA OFERTA
                String mensaje = servidor.recvStr();
                System.out.println("Oferta: " + mensaje + ", recibida y almacenada.");
                servidor.send(mensaje);
                Thread.sleep(1000);
                }
                } else {
                servidor.bind("tcp://25.57.158.0:9999"); //IP BACKUP
                while(true){
                System.out.println("Listo para recibir ofertas del BACKUP y almacenarlas: ");
                // RECIBE LA OFERTA
                String mensaje = servidor.recvStr();
                System.out.println("Oferta: " + mensaje + ", recibida y almacenada.");
                servidor.send(mensaje);
                Thread.sleep(1000);
                }

                }
            } catch (Exception e) {
                //TODO: handle exception
            }


        } catch (Exception e) {

        }

    }
/*
    public void eliminarVacante(String linea) throws IOException{
        File inputFile = new File("Ofertas.txt");
        File tempFile = new File("myTempFile.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String lineToRemove = linea;
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
        // trim newline when comparing with lineToRemove
        String trimmedLine = currentLine.trim();
         if(trimmedLine.equals(lineToRemove)) continue;
         writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close(); 
        reader.close(); 
        boolean xd = tempFile.renameTo(inputFile);
    }
*/
    public String getOffer(String id){
        String vacante = "";
        try  
        {  
        File file = new File("Ofertas.txt");    //creates a new file instance  
        FileReader fr = new FileReader(file);   //reads the file  
        BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream  
        String line;  
        
        while((line=br.readLine())!=null)  
        {  
        String[] partes = line.split(",");
        if(partes[0].equals(id)){
            vacante = partes[2];
            return vacante;
        }
        
        }  
        fr.close(); 
        return vacante;   //closes the stream and release the resources    
        }  
        catch(IOException e)  
        {  
        e.printStackTrace();  
        }
        return vacante;
        }


    public void imprimirCitas(int id){
        String cod = Integer.toString(id);
        String vacante;
    try  
    {  
    File file = new File("Citas.txt");    //creates a new file instance  
    FileReader fr = new FileReader(file);   //reads the file  
    BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream  
    String line;  
    
    while((line=br.readLine())!=null)  
    {  
    String[] partes = line.split(",");
    if(partes[0].equals(cod)){
        vacante = getOffer(partes[1]);
        System.out.println("Cita disponible para la vacante de: " + vacante + " con id: " + id);
    }
    }
    fr.close();    //closes the stream and release the resources    
    }  
    catch(IOException e)  
    {  
    e.printStackTrace();  
    }  
    }


    public boolean noExiste(String user, String oferta){
        boolean seEncontro = false;
                try  
                {  
                File file = new File("Citas.txt");    //creates a new file instance  
                FileReader fr = new FileReader(file);   //reads the file  
                BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream  
                String line;  
               
                while((line=br.readLine())!=null)  
                {  
                String[] partes = line.split(",");
                if(partes[0].equals(user) && partes[1].equals(oferta)){
                    seEncontro = true;
                }
                
                }  
                fr.close();    //closes the stream and release the resources    
                }  
                catch(IOException e)  
                {  
                e.printStackTrace();  
                }  
                return seEncontro;
          }


    //Almacena la cita
    public void generarCita(String oferta, String id){
        
        try (FileWriter f = new FileWriter("Citas.txt", true);
        BufferedWriter b = new BufferedWriter(f);
        PrintWriter p = new PrintWriter(b);) {
            p.println(id+","+oferta);
            } catch (IOException i) {
            i.printStackTrace();
             }
          }


    //Busca una oferta compatible con el usuario y retorna el ID
    public void buscarOferta(Aspirante aspirante){
        String oferta = "";
    try  
    {  
    File file = new File("Ofertas.txt");    //creates a new file instance  
    FileReader fr = new FileReader(file);   //reads the file  
    BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream  
    String line;  
    
    while((line=br.readLine())!=null)  
    {  
    String[] partes = line.split(",");
    if(Integer.parseInt(partes[1])==(aspirante.getSector()[0]) || 
    Integer.parseInt(partes[1])==(aspirante.getSector()[1])){
        if(partes[4].equals(aspirante.getHabilidad()) && Integer.parseInt(partes[5])==aspirante.getExperiencia() && partes[6].equals(aspirante.getFormacion()))
        {
         oferta = partes[0]; //Se obtiene ID
         if(!noExiste(Integer.toString(aspirante.getId()), oferta))
         {
            generarCita(oferta,Integer.toString(aspirante.getId()));
            //eliminarVacante(line);
        } 
        } 
    } else { return;}
    }  
    fr.close();    //closes the stream and release the resources    
    }  
    catch(IOException e)  
    {  
    e.printStackTrace();  
    }  
    }

    
    

    public  void registrarEmpleador(Empleador emp){
        try (FileWriter f = new FileWriter("Empleadores.txt", true);
        BufferedWriter b = new BufferedWriter(f);
        PrintWriter p = new PrintWriter(b);) {

            p.println(emp.getId()+","+emp.getContraseña());


            } catch (IOException i) {
            i.printStackTrace();
             }
          }

          public  void registrarOferta(Oferta of){
            try (FileWriter f = new FileWriter("Ofertas.txt", true);
            BufferedWriter b = new BufferedWriter(f);
            PrintWriter p = new PrintWriter(b);) {
    
                p.println(of.getId()+","+of.getSector()+","+of.getNombre()+","+of.getEmple()+","+of.getHabilidad()+","+of.getXp()+","+of.getFormacion());
    
    
                } catch (IOException i) {
                i.printStackTrace();
                 }
              }



          public boolean existeE(String usuario, String contra){
            boolean seEncontro = false;
        try  
        {  
        File file = new File("Empleadores.txt");    //creates a new file instance  
        FileReader fr = new FileReader(file);   //reads the file  
        BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream  
        String line;  
        
        while((line=br.readLine())!=null)  
        {  
        String[] partes = line.split(",");
        if(partes[0].equals(usuario) && partes[1].equals(contra)){
            seEncontro = true;
        }
        
        }  
        fr.close();    //closes the stream and release the resources    
        }  
        catch(IOException e)  
        {  
        e.printStackTrace();  
        }  
        return seEncontro;
        }

        public Empleador obtenerEmp(String usuario, String contra){
             
            try  
            {  
            Empleador emp = null;
            File file = new File("Empleadores.txt");    //creates a new file instance  
            FileReader fr = new FileReader(file);   //reads the file  
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream  
            String line;  
           
            while((line=br.readLine())!=null)  
            {  
            String[] partes = line.split(",");
            if(partes[0].equals(usuario) && partes[1].equals(contra)){
                 emp = new Empleador(Integer.parseInt(partes[0]),partes[1]);
                 return emp;
            }
            
            }  
            fr.close();    //closes the stream and release the resources    
            return emp;
            }  
            catch(IOException e)  
            {  
            e.printStackTrace();  
            }
            return null;
             
            }
            public int generarIDoferta(){
                int lineas = 0; 

                try  
                {  
                File file = new File("Ofertas.txt");    //creates a new file instance  
                FileReader fr = new FileReader(file);   //reads the file  
                BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream  
                String line;
                 
                while((line=br.readLine())!=null)  
                {  
                lineas++;
                }  
                fr.close(); 
                return lineas;   //closes the stream and release the resources    
                }  
                catch(IOException e)  
                {  
                e.printStackTrace();  
                }
                return lineas;  
                
               

            }

            public void registrarAspirante(Aspirante asp){
                try (FileWriter f = new FileWriter("Aspirantes.txt", true);
                BufferedWriter b = new BufferedWriter(f);
                PrintWriter p = new PrintWriter(b);) {
        
                    p.println(asp.getId() + "," + asp.getContraseña()+","+asp.getExperiencia()+","+asp.getSector()[0]+","+asp.getSector()[1]+","+asp.getHabilidad()+","+asp.getFormacion());
        
        
                    } catch (IOException i) {
                    i.printStackTrace();
                     }
                  }
                
                  public boolean existe(String usuario, String contra){
                      boolean seEncontro = false;
                try  
                {  
                File file = new File("Aspirantes.txt");    //creates a new file instance  
                FileReader fr = new FileReader(file);   //reads the file  
                BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream  
                String line;  
               
                while((line=br.readLine())!=null)  
                {  
                String[] partes = line.split(",");
                if(partes[0].equals(usuario) && partes[1].equals(contra)){
                    seEncontro = true;
                }
                
                }  
                fr.close();    //closes the stream and release the resources    
                }  
                catch(IOException e)  
                {  
                e.printStackTrace();  
                }  
                return seEncontro;
                }
                
         public Aspirante obtenerAsp(String usuario, String contra){
                     
                try  
                {  
                Aspirante aspi = null;
                File file = new File("Aspirantes.txt");    //creates a new file instance  
                FileReader fr = new FileReader(file);   //reads the file  
                BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream  
                String line;  
               
                while((line=br.readLine())!=null)  
                {  
                String[] partes = line.split(",");
                if(partes[0].equals(usuario) && partes[1].equals(contra)){
                    int[] sectores = new int[2];
                    sectores[0]=Integer.parseInt(partes[3]);
                    sectores[1]=Integer.parseInt(partes[4]);
                     aspi = new Aspirante(Integer.parseInt(partes[0]),Integer.parseInt(partes[1]),Integer.parseInt(partes[2]),sectores,partes[5],partes[6]);
                }
                
                }  
                fr.close();    //closes the stream and release the resources    
                return aspi;
                }  
                catch(IOException e)  
                {  
                e.printStackTrace();  
                }
                return null;
                 
                }


    

}