package socialserver;

import java.net.*;
import java.io.*;

/**
 * Classe che gestisce la connessione del server con un singolo client
 *
 * @author Ivan De Simone
 */
public class ThreadSingleConnection implements Runnable {

    //riferimento all'oggetto che gestisce tutte le connessioni
    private ThreadSocialConnectionManager socialManager;
    //connessione con il client
    private Socket client = null;
    //canali di input ed output
    private BufferedReader input = null;
    private PrintWriter output = null;
    Thread me;

    /**
     * Costruttore della classe
     *
     * @param manager gestore di tutte le connessioni
     * @param client connessione stabilita con il client
     */
    public ThreadSingleConnection(ThreadSocialConnectionManager manager, Socket client) {
        this.socialManager = manager;
        this.client = client;
        //creazione canali di input e output
        try {
            this.input = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.output = new PrintWriter(this.client.getOutputStream(), true);
        } catch (IOException e) {
            output.println("Errore in fase di creazione");
        }
        me = new Thread(this);
        me.start();
    }

    /**
     * Quando riceve un messaggio agisce di conseguenza: - LEGGI = restituisce
     * gli ultimi 5 post - POST + stringa = aggiunge un nuovo post in memoria -
     * COMMENTO + post + stringa = aggiunge un commento al post scelto
     */
    @Override
    public void run() {
        //resta sempre in ascolto
        while (true) {
            try {
                String msg = null;
                //rimango in attesa dei messaggi mandati dal client 
                while ((msg = input.readLine()) == null) {
                }
                //controlla quale comando contiene il messaggio
                if (msg.contains("LEGGI")) {
                    //manda i post
                    sendPost();
                } else if (msg.contains("POST ")) {
                    //salva un nuovo post
                    savePost(msg);
                } else if (msg.contains("COMMENTO ")) {
                    //aggiunge un commento al post
                    addComment(msg);
                }
            } catch (IOException e) {
                output.println("Errore in fase di esecuzione richiesta");
            }
        }
    }

    /**
     * Invia i 5 post all'utente tramite il canale di output
     */
    private void sendPost() {
        //array contenente i 5 post ottenuti dal gestore connessioni
        String[] l = socialManager.readPost();
        //invio al client
        try {
            for (int i = 0; i < 5; i++) {
                output.println(l[i]);
            }
        } catch (Exception e) {
            System.out.println("Errore in fase di spedizione post");
        }
    }

    /**
     * Salva il nuovo post nella memoria del server
     *
     * @param post stringa contenente il comando ed il testo del nuovo post
     */
    private void savePost(String post) {
        //rimuove il comando e invia il testo al gestore
        socialManager.savePost(post.replaceFirst("POST ", ""));
    }

    /**
     * Aggiunge il commento al post desiderato
     *
     * @param msg stringa contenente il comando + numero del post + testo del
     * commento
     */
    private void addComment(String msg) {
        //rimuove il comando
        String tempCmd = msg.replaceFirst("COMMENTO ", "");
        try {
            //trova il numero del post da commentare
            int post = findPostToComment(tempCmd);
            //recupera il testo del commento
            String comment = tempCmd.replaceFirst(String.valueOf(post) + " ", "");
            //aggiunta del commento tramite il gestore connessioni
            socialManager.addComment(post, comment);
        } catch (NumberFormatException e) {
            System.out.println("Errore nella scrittura del numero del post");
        }
    }

    /**
     * Estrae dal comando il numero del post da commentare
     *
     * @param cmd comando compreso di numero post + commento
     * @return numero del post da commentare
     * @throws NumberFormatException eccezione in caso non venga scritto
     * correttamente il numero del post da commentare
     */
    private int findPostToComment(String cmd) throws NumberFormatException {
        //prende la prima cifra che sa essere il valore in stringa
        String postNumber = String.valueOf(cmd.charAt(0));
        //restituisce il valore in formato int
        return Integer.parseInt(postNumber);
    }

}
