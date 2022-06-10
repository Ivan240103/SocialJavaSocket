package socialclient;

import java.awt.*;
import java.net.*;
import java.io.*;
import javax.swing.*;

/**
 * Classe che contiene la logica di funzionamento del client
 *
 * @author Ivan De Simone
 */
public class ThreadSocialClient implements Runnable {

    //lista di post
    private List list;
    //nome utente
    private JTextField user;
    //connessione con il server
    private Socket client;
    //canali di input ed output
    private BufferedReader input = null;
    private PrintWriter output = null;
    Thread me;

    /**
     * Costruttore della classe
     *
     * @param list lista di elementi da visualizzare
     * @param user campo di testo contenente il nome utente
     * @param ipServer indirizzo ip del server a cui collegarsi
     * @param port porta del server a cui collegarsi
     */
    public ThreadSocialClient(List list, JTextField user, String ipServer, int port) {
        this.list = list;
        this.user = user;
        //crea la connessione ed i canali di input ed output
        try {
            client = new Socket(ipServer, port);
            this.input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.output = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Impossibile connettersi al server");
        }
        //avvio del client
        me = new Thread(this);
        me.start();
    }

    /**
     * Aspetta di ricevere le comunicazioni dal server
     */
    @Override
    public void run() {
        while (true) {
            //aspetta di ricevere i 5 nuovi post inviati dal server
            for (int i = 0; i < 5; i++) {
                try {
                    String msg = null;
                    while ((msg = input.readLine()) == null) {
                    }
                    list.add((i + 1) + ") " + msg);
                    list.select(list.getItemCount() - 1);
                } catch (IOException e) {
                    System.out.println("Errore in lettura messaggi");
                }
            }
        }
    }

    /**
     * Invia il comando da eseguire al server
     *
     * @param cmd comando da eseguire ['LEGGI', 'POST + stringa', 'COMMENTO']
     */
    public void sendCommand(String cmd) {
        try {
            if (cmd.contains("LEGGI")) {
                //se il comando è LEGGI prima svuota la lista dai precedenti post
                list.removeAll();
            } else if (cmd.contains("POST ")) {
                //se il comando è post aggiunge all'inizio il nome utente
                cmd = cmd.replaceFirst("POST ", "POST " + user.getText() + ": ");
            } else if (cmd.contains("COMMENTO ")) {
                //se il comando è commento aggiunge il nome utente alla fine tra
                //parentesi
                cmd = cmd.concat(" (" + user.getText() + ")");
            }
            //manda il comando
            output.println(cmd);
        } catch (Exception e) {
            System.out.println("Errore in fase di invio del comando");
        }
    }
}
