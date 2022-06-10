package socialserver;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Classe che gestisce la finestra del server
 *
 * @author Ivan De Simone
 */
public class SocialServer extends JFrame {

    /**
     * Costruttore della classe
     */
    public SocialServer() {
        //imposta il nome della finestra
        super("Social Server");
        //imposta la grandezza della finestra
        this.setSize(new Dimension(500, 300));
        //imposta la finestra al centro dello schermo
        this.setLocationRelativeTo(null);
        //abilita la finestra
        this.setEnabled(true);
        //crea il pannello per la visualizzazione dei post
        SocialServerPanel pan = new SocialServerPanel();
        this.getContentPane().add(pan);
        //lo rende visibile
        this.setVisible(true);
    }
}

/**
 * Classe che gestisce il pannello di visualizzazione dei post
 *
 * @author Ivan De Simone
 */
class SocialServerPanel extends JPanel implements ActionListener {

    private final List list;
    private ThreadSocialConnectionManager serviceManager;

    /**
     * Costruttore della classe
     */
    public SocialServerPanel() {
        super();
        //pannello superiore: lista post
        JPanel listPanel = new JPanel(new BorderLayout(20, 5));
        listPanel.setBackground(Color.ORANGE);
        //creazione lista ed elementi di default
        list = new List(5);
        list.add("Utente: primo");
        list.add("Utente: secondo");
        list.add("Utente: terzo");
        list.add("Utente: quarto");
        list.add("Utente: quinto");
        list.setBackground(Color.lightGray);
        list.setSize(100, 50);
        list.setVisible(true);
        //scritte laterali social 
        JLabel social1 = new JLabel("    Social", JLabel.CENTER);
        social1.setForeground(Color.RED);
        JLabel social2 = new JLabel("Social    ", JLabel.CENTER);
        social2.setForeground(Color.RED);
        //aggiunta oggetti sul pannello
        listPanel.add(social1, BorderLayout.WEST);
        listPanel.add(list, BorderLayout.CENTER);
        listPanel.add(social2, BorderLayout.EAST);

        //aggiunta del pannello
        this.setLayout(new BorderLayout(0, 5));
        add(listPanel, BorderLayout.CENTER);

        //avvia la connessione con i client
        connetti();
    }

    /**
     * Creazione del gestore connessioni del server
     */
    private void connetti() {
        //istanzia il thread che gestisce le connessioni, max 10 client
        serviceManager = new ThreadSocialConnectionManager(10, list);
    }

    /**
     * Metodo obbligatorio per gestire gli eventi
     *
     * @param ae evento generato
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println("actionPerformed called");
    }

}
