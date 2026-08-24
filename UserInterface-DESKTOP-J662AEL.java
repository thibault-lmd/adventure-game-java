import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

/**
 * Classe UserInterface - interface graphique de The Submerged City.
 * Gère l'affichage de l'image, du log de texte et les interactions
 * via les boutons et le champ de saisie clavier.
 *
 * @author LAM-DETRAIT Thibault
 */
public class UserInterface implements ActionListener
{
    private GameEngine aEngine;    // moteur de jeu qui traite les commandes
    private JFrame     aMyFrame;       // fenêtre principale
    private JTextField aEntryField; // champ de saisie des commandes
    private JTextArea  aLog;           // zone d'affichage du texte du jeu
    private JLabel     aImage;     // zone d'affichage de l'image de la pièce

    // Boutons de déplacement et de commande
    private JButton aHelpButton;
    private JButton aGoNorthButton;
    private JButton aGoSouthButton;
    private JButton aGoEastButton;
    private JButton aGoWestButton;
    private JButton aGoUpButton;
    private JButton aGoDownButton;
    private JButton aQuitButton;
    private JButton aBackButton;
    private JButton aLookButton;
    
    /**
     * Initialise l'interface en lui associant le moteur de jeu
     * et en construisant la fenêtre graphique.
     */
    public UserInterface( final GameEngine pGameEngine )
    {
        this.aEngine = pGameEngine;
        this.createGUI();
    }

    /**
     * Affiche du texte dans le log sans retour à la ligne.
     */
    public void print( final String pText )
    {
        this.aLog.append( pText );
        this.aLog.setCaretPosition( this.aLog.getDocument().getLength() );
    }

    /**
     * Affiche du texte dans le log avec un retour à la ligne.
     */
    public void println( final String pText )
    {
        this.print( pText + "\n" );
    }

    /**
     * Charge et affiche l'image correspondant à la pièce courante.
     * Affiche un message dans la console si l'image est introuvable.
     */
    public void showImage( final String pImageName )
    {
        String vImagePath = "Images/" + pImageName;
        URL vImageURL = this.getClass().getClassLoader().getResource( vImagePath );
        if ( vImageURL == null ) {
            System.out.println( "Image not found : " + vImagePath );
        } else {
            ImageIcon vIcon = new ImageIcon( vImageURL );
            this.aImage.setIcon( vIcon );
            this.aMyFrame.pack();
        }
    }

    /**
     * Active ou désactive le champ de saisie et tous les boutons.
     * Appelé en fin de partie pour bloquer toute interaction.
     */
    public void enable( final boolean pOnOff )
    {
        this.aEntryField.setEditable( pOnOff );
        if ( pOnOff ) {
            this.aEntryField.getCaret().setBlinkRate( 500 );
            this.aEntryField.addActionListener( this );
        } else {
            this.aEntryField.getCaret().setBlinkRate( 0 );
            this.aEntryField.removeActionListener( this );
        }
        aHelpButton.setEnabled( pOnOff );
        aGoNorthButton.setEnabled( pOnOff );
        aGoSouthButton.setEnabled( pOnOff );
        aGoEastButton.setEnabled( pOnOff );
        aGoWestButton.setEnabled( pOnOff );
        aGoUpButton.setEnabled( pOnOff );
        aGoDownButton.setEnabled( pOnOff );
        aQuitButton.setEnabled( pOnOff );
        aBackButton.setEnabled( pOnOff );
        aLookButton.setEnabled( pOnOff );
    }

    /**
     * Construit la fenêtre graphique avec ses trois zones :
     * image en haut, log au centre, boutons et saisie en bas.
     */
    private void createGUI()
    {
        this.aMyFrame    = new JFrame( "Submerged City" );
        this.aEntryField = new JTextField( 34 );

        // zone de texte non éditable avec ascenseur
        this.aLog = new JTextArea();
        this.aLog.setEditable( false );
        JScrollPane vListScroller = new JScrollPane( this.aLog );
        vListScroller.setPreferredSize( new Dimension( 400, 200 ) );
        vListScroller.setMinimumSize( new Dimension( 100, 100 ) );

        this.aImage = new JLabel();

        // création des boutons
        aHelpButton    = new JButton( "Help" );
        aGoNorthButton = new JButton( "Go North" );
        aGoSouthButton = new JButton( "Go South" );
        aGoEastButton  = new JButton( "Go East" );
        aGoWestButton  = new JButton( "Go West" );
        aGoUpButton    = new JButton( "Go Up" );
        aGoDownButton  = new JButton( "Go Down" );
        aQuitButton    = new JButton( "Quit" );
        aBackButton    = new JButton( "Back" );
        aLookButton    = new JButton( "Look" );

        // enregistrement de l'écouteur sur chaque bouton
        aHelpButton.addActionListener( this );
        aGoNorthButton.addActionListener( this );
        aGoSouthButton.addActionListener( this );
        aGoEastButton.addActionListener( this );
        aGoWestButton.addActionListener( this );
        aGoUpButton.addActionListener( this );
        aGoDownButton.addActionListener( this );
        aQuitButton.addActionListener( this );
        aBackButton.addActionListener( this );
        aLookButton.addActionListener( this );

        // disposition des boutons en grille 3x3
        JPanel vButtonPanel = new JPanel( new GridLayout( 3, 4 ) );
        vButtonPanel.add( aGoUpButton );
        vButtonPanel.add( aGoNorthButton );
        vButtonPanel.add( aGoDownButton );
        vButtonPanel.add( aGoWestButton );
        vButtonPanel.add( aBackButton );
        vButtonPanel.add( aGoEastButton );
        vButtonPanel.add( aHelpButton );
        vButtonPanel.add( aGoSouthButton );
        vButtonPanel.add( aQuitButton );
        vButtonPanel.add( aLookButton );

        // boutons au-dessus du champ de saisie
        JPanel vSouthPanel = new JPanel( new BorderLayout() );
        vSouthPanel.add( vButtonPanel,       BorderLayout.NORTH );
        vSouthPanel.add( this.aEntryField,   BorderLayout.SOUTH );

        // assemblage du panneau principal
        JPanel vPanel = new JPanel( new BorderLayout() );
        vPanel.add( this.aImage,   BorderLayout.NORTH  );
        vPanel.add( vListScroller, BorderLayout.CENTER );
        vPanel.add( vSouthPanel,   BorderLayout.SOUTH  );

        this.aMyFrame.getContentPane().add( vPanel, BorderLayout.CENTER );

        // réaction à la touche ENTRÉE dans le champ de saisie
        this.aEntryField.addActionListener( this );

        // fermeture du programme quand la fenêtre est fermée
        this.aMyFrame.addWindowListener(
            new WindowAdapter() {
                @Override public void windowClosing( final WindowEvent pE )
                {
                    System.exit( 0 );
                }
            } );

        this.aMyFrame.pack();
        this.aMyFrame.setVisible( true );
        this.aEntryField.requestFocus();
    }

    /**
     * Appelée automatiquement lors d'un clic sur un bouton ou d'une
     * validation dans le champ de saisie. Identifie la source de l'événement
     * et envoie la commande correspondante au moteur de jeu.
     */
    @Override public void actionPerformed( final ActionEvent pE )
    {
        Object vSource = pE.getSource();

        if ( vSource == aHelpButton ) {
            this.aEngine.interpretCommand( "help" );
        } else if ( vSource == aGoNorthButton ) {
            this.aEngine.interpretCommand( "go north" );
        } else if ( vSource == aGoSouthButton ) {
            this.aEngine.interpretCommand( "go south" );
        } else if ( vSource == aGoEastButton ) {
            this.aEngine.interpretCommand( "go east" );
        } else if ( vSource == aGoWestButton ) {
            this.aEngine.interpretCommand( "go west" );
        } else if ( vSource == aGoUpButton ) {
            this.aEngine.interpretCommand( "go up" );
        } else if ( vSource == aGoDownButton ) {
            this.aEngine.interpretCommand( "go down" );
        } else if ( vSource == aQuitButton ) {
            this.aEngine.interpretCommand( "quit" );
        } else if ( vSource == aBackButton ) {
            this.aEngine.interpretCommand( "back" );
        } else if ( vSource == aLookButton ) {
            this.aEngine.interpretCommand( "look" );

        } else {
            this.processCommand();
        }
    }

    /**
     * Récupère la commande tapée dans le champ de saisie,
     * vide le champ et l'envoie au moteur de jeu.
     */
    private void processCommand()
    {
        String vInput = this.aEntryField.getText();
        this.aEntryField.setText( "" );
        this.aEngine.interpretCommand( vInput );
    }
} 