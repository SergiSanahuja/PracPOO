package Socket.Client;

import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * Aquesta classe conté els atributs i els mètodes d'un client
 * @author Sergi Sanahuja Torrent i Maria Rodriguez Palomino
 * @version 25/03/2022
 */

public class Client {
    private Socket socket;
    private DataInputStream Entrada = null;
    private DataOutputStream Sortida = null;
    Scanner teclado = new Scanner(System.in);
    final String EXIT = "salir()";

    /**
     *coneccio() Mètode per connectar-se amb el servidor
     * @param ip Sting Busca la ip del servidor per poder connectar-se
     * @param port int identifica el procés al qual lliurar el missatge dins de la màquina
     */
    public void coneccio(String ip, int port){
        try{
            socket = new Socket(ip, port);
            mostrarTexto("Conectado a :" + socket.getInetAddress().getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     *Flujos() Mètode per crear els ports on rep i envia la informació (crear sockets)
     */
    public void Flujos() {
        try{
            Entrada = new DataInputStream(socket.getInputStream());
            Sortida = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *rebreDades() Mètode per dessencriptar missatge i llegir-lo
     */
    public void rebreDades(){
        String st = "";
        try {

            st = (String) Entrada.readUTF();
            mostrarTexto("\n[Cliente] => " + st);
            System.out.println("\n[Usted] => ");

        } catch (IOException e) {
            tencarConneccio();
        }
    }

    /**
     *enviar() Mètode que agafa el missatge que s'escriu i el codifca en format utf i l'envia
     * @param s string que conté el missatge que s'escriu
     */
    public void enviar(String s){
        try{
            System.out.println("missatje enviar, esperi el seu torn");
            Sortida.writeUTF(s);
            Sortida.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *ejecutarConexion() Mètode realitza la connexió amb el servidor
     * @param ip Busca la ip del servidor per poder connectar-se
     * @param puerto Identifica el procés al qual lliurar el missatge dins de la màquina
     */
    public void ejecutarConexion(String ip ,int puerto) {
        Scanner scan = new Scanner(System.in);
        coneccio(ip,puerto);
        Flujos();

        while (true) {
            rebreDades();
            enviar(scan.nextLine());
        }
            //tencarConneccio();
    }

    /**
     *escriure() Mètode que permet al client escriure un missatge
     */
    public void escriure(){
        while (true){
            System.out.println("[Usted] => ");
            Scanner scan = new Scanner(System.in);
            enviar(scan.nextLine());
        }
    }

    /**
     *mostrarTexto() Mètode que mostra al client el text que ha escrit l'altre client
     * @param s  String que conté el missatge que s'escriu
     */
    public static void mostrarTexto (String s){
        System.out.println(s);
    }

    /**
     * tencarConnecio() Mètode que tanca tots els sockets/connexió amb servidor
     */
    public void tencarConneccio(){
        try {
            Entrada.close();
            Sortida.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            mostrarTexto("conversacio fallada");
            System.exit(0);
        }
    }
    public static void main(String[] argumentos) {
        Client cliente = new Client();
        Scanner escaner = new Scanner(System.in);
        mostrarTexto("Ingresa la IP: [localhost por defecto] ");
        String ip = escaner.nextLine();
        if (ip.length() <= 0) ip = "localhost";

        mostrarTexto("Puerto: [5050 por defecto] ");
        String puerto = escaner.nextLine();
        if (puerto.length() <= 0) puerto = "5050";
        cliente.ejecutarConexion(ip, Integer.parseInt(puerto));
        cliente.escriure();

    }

}
