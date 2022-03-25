package Socket.joc;

import Socket.Servidor.Servidor;

import java.io.DataInputStream;
import java.util.Objects;
import java.util.Scanner;

/**
 * Aquesta classe conté els atributs i els mètodes d'un client
 * @author Sergi Sanahuja Torrent i Maria Rodriguez Palomino
 * @version 25/03/2022
 */


public class Joc {
    public static String[][] mapaO;
    public static int numcol=7, numfilas = 7;
    public static boolean fi = false;

    /**
     *inicialitzar() Inicialitza el joc creant les files y columnes corresponents
     */
    public static void inicialitzar(){
        int f = 7, c=7;

        mapaO = new String[f][c];
        IniCamp(mapaO, "·");

    }

    /**
     *IniCamp() Inicialitza els dos arrays de la taula
     * @param cmini Array Bidimensional que conté la part visible de la taula
     * @param caracter String del tipus de dada que es mostra a la taula
     */
    private static void IniCamp(String[][] cmini, String caracter ){
        for (int i = 0; i < cmini.length; i++) {
            for (int j = 0; j < cmini[0].length; j++) {
                cmini[i][j] = caracter;
            }
        }
    }


    /**
     *Mostrar_camps() Mostra la taula del joc
     * @param mapa Array bidimensional que conté la part visible de la taula
     */
    private static void Mostrar_camp(String[][] mapa){

        int num = 1;
        for (int i = 0; i < mapa[0].length -1; i++) {
            System.out.printf("%-2d ",num);
            num ++;

        }
        System.out.println();
        for (int i = 1; i < mapa.length ; i++) {
            for (int j = 1; j < mapa[0].length ;j++) {
                System.out.print(mapa[i][j] + "  ");
            }
            System.out.println();
        }
    }

    /**
     *posicio() Mètode que s'encarrega d'analitzar si ja hi ha algun camp ple
     * @param c És un integer per saber el numero de la columna
     * @param _e És un DataInput que és l'entrada
     * @param a  és un String que mostra el caràcter de les fitxes
     */
    public static void posicio(int c, DataInputStream _e, String a){
       int f = 0;

        if (Objects.equals(mapaO[0][c], "X") || Objects.equals(mapaO[0][c], "O")){
            System.out.println("esta ple");
        }
        else {
            for (int i = 0; i < numfilas; i++) {
                if (!Objects.equals(mapaO[i][c], "·")){
                    f = i-1;
                    break;
                }
                f = i;
            }
        }

        if (a.equals("X")){
            mapaO[f][c] = "X";
        }else{
            mapaO[f][c] = "O";
        }
    }

    /**
     *jugada() Mètode que s'encarrega de donar al jugador la fitxa corresponent
     * @param t Integer transformat d'un String
     * @param e És un DataInput que és l'entrada
     * @param c  És un integer per saber el numero de la columna
     */
    public static void jugada(int t, DataInputStream e, String c){

        Joc.posicio(t,e,c);

        Mostrar_camp(mapaO);
        VerificarGuanyador("jugador 1","X");
        VerificarGuanyador("jugador 2","O");

    }

    /**
     *proba() Mètode que s'encarrega de fer la proba de si s'ha introduït el número correcte de la columna
     * @param t Integer transformat d'un String
     * @return retorna el integer columna
     */
    public static boolean proba(int t){
        if (t < 0 || t > 6 ){
            return true;
        }
        return false;
    }


    /**
     *VerificarGuanyador() Mètode que s'encarrega de comptar les fitxes per saber el guanyador
     * @param jugador String del client participant
     * @param caracter String de la ficha del jugador
     */
    public static void VerificarGuanyador(String jugador, String caracter){
        //guanyador horitzontal

        for (int i = 0; i < numfilas; i++) {
            for (int j = 0; j < numcol -3; j++) {
                if (mapaO[i][j].equals(caracter) && mapaO[i][j + 1].equals(caracter) && mapaO[i][j + 2].equals(caracter) && mapaO[i][j + 3].equals(caracter)){
                    fi = true;

                    System.out.println("Ha guanyat " + jugador);
                    Servidor.enviar(Servidor.sortida1,"ha guanyat" + jugador);
                    Servidor.enviar(Servidor.sortida2,"ha guanyat" + jugador);

                    Servidor.tencarConneccio();
                }
            }
        }

        //guanyador vertical
        for (int i = 0; i < numfilas; i++) {
            for (int j = 0; j < numcol - 3; j++) {
                if (mapaO[j][i].equals(caracter) && mapaO[j + 1][i].equals(caracter) && mapaO[j + 2][i].equals(caracter) && mapaO[j + 3][i].equals(caracter)) {
                    fi = true;

                    System.out.println("Ha guanyat " + jugador);
                    Servidor.enviar(Servidor.sortida1,"ha guanyat" + jugador);
                    Servidor.enviar(Servidor.sortida2,"ha guanyat" + jugador);
                    Servidor.tencarConneccio();
                }
            }
        }

        //guanyador diagonal
        for (int i = 0; i < numcol - 4 + 1; i++) {
            for (int j = 0; j < numfilas - 4 + 1 ; j++) {
                if (mapaO[j][i].equals(caracter) && mapaO[j + 1][i + 1].equals(caracter) && mapaO[j + 2][i + 2].equals(caracter) && mapaO[j + 3][i + 3].equals(caracter)) {
                    fi = true;

                    System.out.println("Ha guanyat " + jugador);
                    System.out.println("Ha guanyat " + jugador);
                    Servidor.enviar(Servidor.sortida1,"ha guanyat" + jugador);
                    Servidor.enviar(Servidor.sortida2,"ha guanyat" + jugador);
                    Servidor.tencarConneccio();
                }
            }
        }

        for (int i = numcol; i > 3; i--) {
            for (int j = 0; j < numfilas - 3 ; j++) {
                if (mapaO[j][i - 1].equals(caracter) && mapaO[j + 1][i - 2].equals(caracter) && mapaO[j + 2][i - 3].equals(caracter) && mapaO[j + 3][i - 4].equals(caracter)) {
                    fi = true;

                    System.out.println("Ha guanyat " + jugador);
                    Servidor.enviar(Servidor.sortida1,"ha guanyat" + jugador);
                    Servidor.enviar(Servidor.sortida2,"ha guanyat" + jugador);
                    Servidor.tencarConneccio();
                }

            }

        }



    }

    /**
     * Inicialitza tot el joc
     */
    public static void comencar() {
        Scanner scan = new Scanner(System.in);
        Joc conecta = new Joc();
        inicialitzar();
        Mostrar_camp(mapaO);

       scan.close();

    }

}
