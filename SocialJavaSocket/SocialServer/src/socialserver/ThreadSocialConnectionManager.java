package socialserver;

import java.awt.List;
import java.io.IOException;
import java.net.*;
import javax.swing.JOptionPane;

/**
 * Classe che gestisce le connessioni con tutti i client
 *
 * @author Ivan De Simone
 */
public class ThreadSocialConnectionManager implements Runnable {

    //numero massimo di connessioni (default è 10)
    private int maxConnection = 10;
    //lista di elementi salvati e visualizzati
    private final List list;
    //array con le singole connessioni
    private final ThreadSingleConnection[] connectionList;
    //socket lato server
    private ServerSocket serverSocial;
    Thread me;

    /**
     * Costruttore della classe
     *
     * @param maxConnection numero massimo di connessioni
     * @param list lista di elementi salvati e visualizzati
     */
    public ThreadSocialConnectionManager(int maxConnection, List list) {
        this.maxConnection = maxConnection - 1;
        this.list = list;
        this.connectionList = new ThreadSingleConnection[this.maxConnection];
        me = new Thread(this);
        me.start();
    }

    /**
     * Crea la socket del server e si mette in ascolto per accettare le nuove
     * connessioni dai client
     */
    @Override
    public void run() {
        //controllo per accettare nuove connessioni
        boolean ok = true;
        //instanza della socket server sulla porta 6789
        try {
            serverSocial = new ServerSocket(6789);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Impossibile instanziare il server");
            ok = false;
        }

        //se l'istanza è stata creata procede ad accettare connessioni
        if (ok) {
            try {
                for (int i = 0; i < maxConnection; i++) {
                    Socket temp = null;
                    temp = serverSocial.accept();
                    //aggiunge la nuova connessione all'elenco
                    connectionList[i] = new ThreadSingleConnection(this, temp);
                }
                //raggiunto il limite di connessioni chiude il server
                serverSocial.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Impossibile instanziare server chat");
            }
        }
    }

    /**
     * Aggiunge il nuovo post alla lista sul server
     *
     * @param post stringa post da memorizzare
     */
    protected void savePost(String post) {
        //se la lista è piena rimuove l'elemento più vecchio, il primo
        if (list.getItemCount() == 5) {
            list.remove(0);
        }
        //aggiunge quello nuovo
        list.add(post);
    }

    /**
     * Manda al thread richiedente la lista dei 5 post
     *
     * @return array con gli ultimi 5 post
     */
    protected String[] readPost() {
        return list.getItems();
    }

    /**
     * Aggiunge il commento al post desiderato
     *
     * @param post numero del post da commentare (corrisponde alla posizione +
     * 1)
     * @param comment testo del commento
     */
    protected void addComment(int post, String comment) {
        //aggiunge alla stringa del post il commento
        list.replaceItem(list.getItem(post - 1) + " - Commento: " + comment, post - 1);
    }
}
