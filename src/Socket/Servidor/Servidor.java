package Socket.Servidor;

import  java.net.*;
import  java.io.*;
import  java.util.Scanner;
import  Socket.joc.Joc;

public class Servidor {
    public static Socket socket,socket2;
    private ServerSocket serverSocket;
    public static DataInputStream entrada1 = null, entrada2 = null;
    public static DataOutputStream sortida1 = null, sortida2 = null;
    char[][] taulaO = new char[7][6];

    Scanner scan = new Scanner(System.in);
    final String SALIDA = "salir()";



    /**
     * conexion() Mètode per connectar-se amb els clients
     * @param puerto Integer que identifica el procés al qual lliurar el missatge dins de la màquina
     */
    public void conexion(int puerto){
        try{
            serverSocket = new ServerSocket(puerto);
            mostrarTexto("Esperant conecció en el port "+ String.valueOf(puerto)+ " ...");
            socket = serverSocket.accept();
            mostrarTexto("Primer jugador acceptat, esperant segon jugador ...");
            socket2 = serverSocket.accept();
            mostrarTexto(" Connecció acceptada, començar joc");
            //enviar(sortida1,"Tu començes");
        } catch (IOException e) {
            new IllegalArgumentException("No ha pogut fer conecció");
            System.exit(0);
        }
    }


    /**
     * ejecutarConexion() Mètode realitza la connexió amb els dos clients
     * @param puerto Integer que identifica el procés al qual lliurar el missatge dins de la màquina
     */
    public void ejecutarConexion(int puerto) {
        conexion(puerto);
        flujos();
        Joc.comencar();
        while (!entrada2.equals(SALIDA) || !entrada1.equals(SALIDA)) {
            escriur(sortida1);
            rebreDadesSoket(entrada1);

            escriur(sortida2);
            rebreDadesSoket(entrada2);

        }
        //tencarConneccio();
    }


    /**
     * flujos() Mètode per crear els ports on rep i envia la informació (crear sockets)
     */
    public void flujos(){
        try {
            entrada1 = new DataInputStream(socket.getInputStream());
            sortida1 = new DataOutputStream(socket.getOutputStream());
            entrada2 = new DataInputStream(socket2.getInputStream());
            sortida2 = new DataOutputStream(socket2.getOutputStream());

            sortida1.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * escriur() Mètode que permet als clients poder escriure
     * @param s DataOutput que és el missatge que veuen els clients per saber quan es el seu torn
     */
    public static void escriur(DataOutputStream s){
        String t = "";

        t = "1 2 3 4 5 6 7 \n";

        int num = 1;
        for (int i = 0; i < Joc.mapaO[0].length -1; i++) {
            t+=String.format("%-2d ",num);
            num ++;

        }
        t += "\n";
        for (int i = 1; i < Joc.mapaO.length ; i++) {
            for (int j = 1; j <Joc.mapaO[0].length ;j++) {
                t+=(Joc.mapaO[i][j] + "  ");
            }
            t += "\n";
        }

        enviar(s,t+"És el teu torn, posa un num");

    }

    /**
     * rep les dades del client
     * @param _e DataInputStream
     */
    public void rebreDadesSoket(DataInputStream _e){
        String st = "";
        try {

            st = (String) _e.readUTF();
             int t = Integer.parseInt(st);
            mostrarTexto("\n[Cliente] => " + st);

            if (_e.equals(entrada1)){
                Joc.jugada(t, _e,"X");
            }else {Joc.jugada(t,_e,"O");            }

            /* if (_e == entrada1){
                enviar(sortida2,st);
                System.out.println("missatge enviat"  );
            }else if (_e == entrada2){
                enviar(sortida1,st);
            }else {
                System.out.println("No es pot enviar");
            }*/

        } catch (IOException e) {
            tencarConneccio();
        }
    }

    /**
     * enviar() Mètode que agafa el missatge que s'escriu i el codifca en format utf i l'envia
     * @param sortida  DataOutput  que envia el missatge
     * @param s String del missatge que s'ha escrit
     */
    public static void enviar(DataOutputStream sortida,String s){
        try {
            sortida.writeUTF(s);
            sortida.flush();
        } catch (IOException e) {
            new IllegalArgumentException("error al enviar");
        }
    }


    /**
     * tencarConnecio() Mètode que tanca tots els sockets/connexió
     */
    public static void tencarConneccio(){
        try {
            entrada1.close();
            sortida1.close();
            entrada2.close();
            sortida2.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            mostrarTexto("conversacio fallada");
            System.exit(0);
        }
    }


    /**
     * mostrarTexto() Mètode que mostra el text que s'ha escrit
     * @param s String que conté el missatge que s'ha escrit
     */
    public static void mostrarTexto (String s){
        System.out.println(s);
    }



    public static void main(String[] args) throws IOException {
        Servidor s = new Servidor();
        Scanner sc = new Scanner(System.in);

        mostrarTexto("Ingresa el puerto [5050 por defecto]: ");
        String puerto = sc.nextLine();
        if (puerto.length() <= 0) puerto = "5050";
        s.ejecutarConexion(Integer.parseInt(puerto));


    }
}