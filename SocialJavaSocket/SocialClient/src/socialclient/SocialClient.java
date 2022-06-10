package socialclient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Classe che gestisce la finestra del client
 *
 * @author Ivan De Simone
 */
public class SocialClient extends JFrame {

    /**
     * Costruttore della classe
     */
    public SocialClient() {
        //nome della finestra
        super("Social Client");
        //imposta la grandezza della finestra 
        this.setSize(new Dimension(500, 400));
        //imposta la finestra al centro dello schermo
        this.setLocationRelativeTo(null);
        //abilita la finestra
        this.setEnabled(true);
        //istanzia il pannello del client
        SocialClientPanel pan = new SocialClientPanel();
        this.getContentPane().add(pan);
        //lo rende visibile
        this.setVisible(true);
    }
}

/**
 * Classe che gestisce i pannelli di inserimento nome utente, visualizzazione ed
 * invio dei messaggi
 *
 * @author Ivan De Simone
 */
class SocialClientPanel extends JPanel implements ActionListener {

    //lista dei post da visualizzare
    private final List list;
    //campo di testo in cui scrivere i comandi
    private final JTextField cmdText;
    //campo di testo in cui inserire il nome utente
    private final JTextField userText;
    //thread gestore delle operazioni del client
    private ThreadSocialClient clientThreadManager;
    //ip e porta del server
    private final String ipServer = "127.0.0.1";
    private final int port = 6789;

    /**
     * Costruttore della classe
     */
    public SocialClientPanel() {
        super();
        this.setBackground(Color.YELLOW);

        //pannello inserimento nome utente e istruzioni comandi
        JPanel userPanel = new JPanel(new BorderLayout(20, 5));
        userPanel.setBackground(Color.YELLOW);

        //label e casella di testo per il nome utente
        JLabel nameLabel = new JLabel("Inserisci il nome utente: ", JLabel.CENTER);
        nameLabel.setForeground(Color.BLACK);
        userText = new JTextField("Utente");
        //istruzioni per i comandi
        JLabel guideLabel = new JLabel("<html>Comandi: LEGGI = mostra gli ultimi 5 post."
                + "<br>POST [stringa] = pubblica il post."
                + "<br>COMMENTO [#post] [testo] = commenta il post indicato.</html>", JLabel.CENTER);
        guideLabel.setForeground(Color.BLACK);

        //aggiunta degli elementi
        userPanel.add(nameLabel, BorderLayout.WEST);
        userPanel.add(userText, BorderLayout.CENTER);
        userPanel.add(guideLabel, BorderLayout.SOUTH);

        //pannello lista post  
        JPanel listPanel = new JPanel(new BorderLayout(20, 5));
        listPanel.setBackground(Color.YELLOW);

        //lista dei post
        list = new List();
        list.setBackground(Color.lightGray);
        list.setSize(100, 50);
        list.setVisible(true);

        //colonne ai lati della lista post
        JLabel social1 = new JLabel("    Social", JLabel.CENTER);
        social1.setForeground(Color.RED);
        JLabel social2 = new JLabel("Social    ", JLabel.CENTER);
        social2.setForeground(Color.RED);

        //aggiunta degli elementi
        listPanel.add(social1, BorderLayout.WEST);
        listPanel.add(list, BorderLayout.CENTER);
        listPanel.add(social2, BorderLayout.EAST);

        //pannello invio nuovo comando
        JPanel newCmd = new JPanel(new BorderLayout(20, 5));
        newCmd.setBackground(Color.YELLOW);

        //label e casella di testo per il comando
        JLabel newLabel = new JLabel("Nuovo Comando -> ", JLabel.CENTER);
        newLabel.setForeground(Color.BLACK);
        cmdText = new JTextField("");

        //bottone di invio
        JButton sendButton = new JButton("Invia");
        sendButton.addActionListener(this);

        //aggiunta degli elementi
        newCmd.add(newLabel, BorderLayout.WEST);
        newCmd.add(cmdText, BorderLayout.CENTER);
        newCmd.add(sendButton, BorderLayout.EAST);

        //aggiunta dei pannelli
        this.setLayout(new BorderLayout(0, 5));
        add(userPanel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER);
        add(newCmd, BorderLayout.SOUTH);

        //connessione al server
        connect();
    }

    /**
     * Effettua la connessione con il server
     */
    private void connect() {
        //instanzio il thread gestore
        clientThreadManager = new ThreadSocialClient(list, userText, ipServer, port);
    }

    /**
     * Invia il testo quando viene premuto il pulsante invia
     *
     * @param e evento del click sul pulsante
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String bottone = e.getActionCommand();
        if (bottone.equals("Invia")) {
            //invio del comando al server
            clientThreadManager.sendCommand(cmdText.getText());
            //svuota la casella di testo
            cmdText.setText("");
        }
    }
}
