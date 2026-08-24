import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Classe GameEngine - moteur du jeu The Submerged City.
 * Gère la logique du jeu : déplacements, commandes, affichage.
 *
 * @author LAM-DETRAIT Thibault
 * @version 2026.04
 */
public class GameEngine
{
    private Player          aPlayer;      // le joueur
    private Parser          aParser;      // analyseur de commandes
    private UserInterface   aGui;         // interface graphique
    private Room            aVictoryRoom; // pièce de victoire
    private boolean         aTestMode;    // true uniquement pendant l'exécution d'un fichier de test
    private RoomRandomizer  aRandomizer;  // randomizer pour la TransporterRoom et la commande alea

    /**
     * Initialise le moteur de jeu en créant le parser et les pièces.
     */
    public GameEngine()
    {
        this.aParser   = new Parser();
        this.aTestMode = false;
        this.createRooms();
    }

    /**
     * Relie l'interface graphique au moteur et affiche le message de bienvenue.
     */
    public void setGUI( final UserInterface pUserInterface )
    {
        this.aGui = pUserInterface;
        this.printWelcome();
    }

    /**
     * Crée toutes les pièces, relie leurs sorties,
     * crée le joueur et place les items dans les pièces.
     */
    private void createRooms()
    {
        String textMonster = "in a room with a horrifying monster";
        String textKey     = "in a very messy room with a key in the center";

        Room vStart   = new Room( "in the entrance of the submerged city", "Panorama.png" );
        Room v2       = new Room( "in an empty street with beautiful buildings", "Road.png" );
        Room v3       = new Room( "in the General Quarter, there is oxygen in this place", "Rest.png" );
        Room v4       = new Room( textMonster, "Monster.png" );
        Room v5       = new Room( "in a room with a chest in the center", "Chest.png" );
        Room v6       = new Room( textMonster, "Monster2.png" );
        Room v7       = new Room( textKey, "Key1.png" );
        Room v8       = new Room( "in an empty street with beatiful buildings", "Road2.png" );
        Room v9       = new Room( textMonster, "Monster3.png" );
        Room v10      = new Room( textKey, "Chest2.png" );
        Room vVictory = new Room( "in the tower you wanted to find for so long", "LightHouse.png" );

        vStart.setExits( "down", v3 );
        v2.setExits( "east", v3 );
        v2.setExits( "south", v5 );
        v3.setExits( "east", v4 );
        v3.setExits( "south", v6 );
        v3.setExits( "west", v2 );
        v4.setExits( "south", v7 );
        v4.setExits( "west", v3 );
        v5.setExits( "north", v2 );
        v6.setExits( "north", v3 );
        v6.setExits( "east", v7 );
        v6.setExits( "south", v8 );
        v7.setExits( "west", v6 );
        v8.setExits( "north", v6 );
        v8.setExits( "up", vVictory );
        v8.setExits( "south", v9 );
        v9.setExits( "north", v8 );
        v9.setExits( "south", v10 );
        v10.setExits( "north", v9 );
        vVictory.setExits( "down", v8 );

        // création du joueur avec un poids maximum de 10kg
        this.aPlayer = new Player( "Hero", vStart, 10.0 );

        // création des items
        Item vFins      = new Item( "Fins",      "a pair of perfectly fine diving fins, maybe it could help you move around the city", 1.0 );
        Item vKey1      = new Item( "Key1",      "a mysterious key, it seems like it could open a door", 0.1 );
        Item vKey2      = new Item( "Key2",      "a mysterious key, it seems like it could open the lighthouse door", 0.1 );
        Item vBottle    = new Item( "Bottle",    "a marvelous bottle of oxygen", 2.0 );
        Item vTelescope = new Item( "Telescope", "a beautiful telescope in which you could see the whole city", 5.0 );
        Item vTrash     = new Item( "Trash",     "just some useless trash sitting next to the key", 10.0 );
        Item vCookie    = new Item( "Cookie",    "a magic cookie, it looks delicious", 0.1 );
        Item vBeamer    = new Beamer( "Beamer",  "a mysterious teleportation device", 1.0 );

        // placement des items dans les pièces
        vStart.addItem( vTelescope );
        v3.addItem( vCookie );
        v3.addItem( vBottle );
        v5.addItem( vFins );
        v7.addItem( vTrash );
        v7.addItem( vKey1 );
        v10.addItem( vKey2 );
        v2.addItem( vBeamer );

        // portes verrouillées
        Door vDoor1 = new Door( v3, v6, "Key1" );
        v3.addDoor( vDoor1 );
        v6.addDoor( vDoor1 );

        Door vDoor2 = new Door( v8, vVictory, "Key2" );
        v8.addDoor( vDoor2 );
        vVictory.addDoor( vDoor2 );

        this.aVictoryRoom = vVictory;

        // création du randomizer et ajout des pièces accessibles par téléportation
        this.aRandomizer = new RoomRandomizer();
        this.aRandomizer.addRoom( "vStart", vStart );
        this.aRandomizer.addRoom( "v2",     v2 );
        this.aRandomizer.addRoom( "v3",     v3 );
        this.aRandomizer.addRoom( "v4",     v4 );
        this.aRandomizer.addRoom( "v5",     v5 );
        this.aRandomizer.addRoom( "v6",     v6 );
        this.aRandomizer.addRoom( "v7",     v7 );
        this.aRandomizer.addRoom( "v8",     v8 );
        this.aRandomizer.addRoom( "v9",     v9 );
        this.aRandomizer.addRoom( "v10",    v10 );

        // création et connexion de la pièce téléporteur
        TransporterRoom vTransporter = new TransporterRoom( "in a strange glowing room", "Transporter.png", this.aRandomizer );
        v2.setExits( "north", vTransporter );
        vTransporter.setExits( "south", v2 );
        
        // création des monstre 
        v4.setMonster( true );
        v6.setMonster( true );
        v9.setMonster( true );
    }

    /**
     * Tente de déplacer le joueur dans la direction indiquée.
     * Vérifie les portes verrouillées, l'oxygène et la condition de victoire.
     */
    private void goRoom( final Command pCommand )
    {
        if ( !pCommand.hasSecondWord() ) {
            this.aGui.println( "Go where?" );
            return;
        }

        String vDirection = pCommand.getSecondWord();
        Room vNextRoom = this.aPlayer.getCurrentRoom().getExitIgnoreDoors( vDirection );
        if ( vNextRoom == null ) {
            this.aGui.println( "There is no door!" );
            return;
        }

        // vérification de la porte verrouillée
        Door vDoor = this.aPlayer.getCurrentRoom().getLockedDoor( vNextRoom );
        if ( vDoor != null ) {
            this.aGui.println( "This door is locked! Find the right key and use 'unlock'." );
            return;
        }

        // vérification de l'oxygène
        if ( this.aPlayer.decrementMoves() ) {
            this.aGui.println( "You ran out of oxygen... Game over." );
            this.aGui.enable( false );
            return;
        } else {
            this.aGui.println( "Oxygen left: " + this.aPlayer.getMovesLeft() + " moves." );
        }

        this.aPlayer.goRoom( vNextRoom );
        this.printLocationInfo();

        // vérification de la victoire
        if ( this.aPlayer.getCurrentRoom() == this.aVictoryRoom ) {
            this.aGui.println( "Congratulations! You found the tower and mapped the city! You win!" );
            this.aGui.enable( false );
        }
        
        // vérification si la pièce contient un monstre
        if ( this.aPlayer.getCurrentRoom().hasMonster() ) {
            this.aGui.println( this.aPlayer.monsterPenalty() );
        }
    }

    /**
     * Tente de ramasser l'item indiqué dans la pièce courante.
     */
    private void takeItem( final Command pCommand )
    {
        if ( !pCommand.hasSecondWord() ) {
            this.aGui.println( "Take what?" );
            return;
        }
        this.aGui.println( this.aPlayer.takeItem( pCommand.getSecondWord() ) );
    }

    /**
     * Tente de déposer l'item indiqué dans la pièce courante.
     */
    private void dropItem( final Command pCommand )
    {
        if ( !pCommand.hasSecondWord() ) {
            this.aGui.println( "Drop what?" );
            return;
        }
        this.aGui.println( this.aPlayer.dropItem( pCommand.getSecondWord() ) );
    }

    /**
     * Tente d'ouvrir une porte verrouillée avec la clé indiquée.
     */
    private void unlockDoor( final Command pCommand )
    {
        if ( !pCommand.hasSecondWord() ) {
            this.aGui.println( "Unlock with what key?" );
            return;
        }
        this.aGui.println( this.aPlayer.unlockDoor( pCommand.getSecondWord() ) );
    }

    /**
     * Retourne le joueur dans la pièce précédente.
     * Distingue le cas du début du jeu et celui d'une trap door.
     */
    private void goBack()
    {
        Room vPrevious = this.aPlayer.goBack();
        if ( vPrevious == null ) {
            if ( this.aPlayer.isPreviousRoomsEmpty() ) {
                this.aGui.println( "You can't go back, you are at the beginning!" );
            } else {
                this.aGui.println( "You can't go back, there is a trap door!" );
            }
            return;
        }
        this.printLocationInfo();
    }

    /**
     * Affiche le message de bienvenue et la description de la pièce de départ.
     */
    private void printWelcome()
    {
        this.aGui.println( "Welcome to the Submerged City!" );
        this.aGui.println( "This city is a beautiful and intriguing place" );
        this.aGui.println( "Type 'help' if you need help." );
        this.aGui.println( "\n" );
        this.printLocationInfo();
    }

    /**
     * Affiche la liste des commandes disponibles.
     */
    private void printHelp()
    {
        this.aGui.println( "You are lost. You are alone." );
        this.aGui.println( "You wander around at the Submerged City." );
        this.aGui.println( " " );
        this.aGui.println( "Your command words are:" );
        this.aGui.println( this.aParser.getCommandString() );
    }

    /**
     * Termine le jeu en désactivant l'interface.
     */
    private boolean quit( final Command pCommand )
    {
        if ( pCommand.hasSecondWord() ) {
            this.aGui.println( "Quit what?" );
            return false;
        } else {
            this.aGui.println( "Thanks for playing this incredible game goodbye :)" );
            this.aGui.enable( false );
            return true;
        }
    }

    /**
     * Lit un fichier texte et exécute chaque ligne comme une commande.
     * Active le mode test pendant l'exécution pour autoriser la commande alea.
     */
    private void test( final Command pCommand )
    {
        if ( !pCommand.hasSecondWord() ) {
            this.aGui.println( "Test which file?" );
            return;
        }
        String vFileName = pCommand.getSecondWord() + ".txt";
        try {
            this.aTestMode = true; // activation du mode test
            Scanner vScanner = new Scanner( new File( vFileName ) );
            while ( vScanner.hasNextLine() ) {
                String vLine = vScanner.nextLine();
                this.interpretCommand( vLine );
            }
            vScanner.close();
            this.aTestMode = false; // désactivation du mode test
        } catch ( FileNotFoundException e ) {
            this.aGui.println( "File not found: " + vFileName );
            this.aTestMode = false;
        }
    }

    /**
     * Charge le téléporteur avec la pièce courante.
     */
    private void chargeBeamer( final Command pCommand )
    {
        if ( !pCommand.hasSecondWord() ) {
            this.aGui.println( "Charge what?" );
            return;
        }
        this.aGui.println( this.aPlayer.chargeBeamer( pCommand.getSecondWord() ) );
    }

    /**
     * Déclenche le téléporteur et affiche la nouvelle pièce.
     */
    private void fireBeamer( final Command pCommand )
    {
        if ( !pCommand.hasSecondWord() ) {
            this.aGui.println( "Fire what?" );
            return;
        }
        this.aGui.println( this.aPlayer.fireBeamer( pCommand.getSecondWord() ) );
        this.printLocationInfo();
    }

    /**
     * Commande alea : force ou libère le prochain tirage aléatoire.
     * Disponible uniquement en mode test.
     */
    private void alea( final Command pCommand )
    {
        if ( !this.aTestMode ) {
            this.aGui.println( "This command is only available in test mode!" );
            return;
        }
        if ( pCommand.hasSecondWord() ) {
            Room vRoom = this.findRoom( pCommand.getSecondWord() );
            if ( vRoom == null ) {
                this.aGui.println( "Unknown room: " + pCommand.getSecondWord() );
                return;
            }
            this.aRandomizer.setForcedRoom( vRoom );
            this.aGui.println( "Teleportation forced to: " + vRoom.getDescription() );
        } else {
            this.aRandomizer.clearForcedRoom();
            this.aGui.println( "Teleportation is now truly random." );
        }
    }
    
    private void bonjour( final Command pCommand )
    {
        if ( pCommand.hasSecondWord() ) {
            this.aGui.println( "bonjour " + pCommand.getSecondWord() );
        } else {
            this.aGui.println( "bonjour" );
        }
    }

    /**
     * Retourne la pièce correspondant à l'identifiant donné via le randomizer.
     */
    private Room findRoom( final String pRoomId )
    {
        return this.aRandomizer.getRoom( pRoomId );
    }

    /**
     * Analyse et exécute la commande tapée par le joueur.
     */
    public void interpretCommand( final String pCommandLine )
    {
        this.aGui.println( "> " + pCommandLine );
        Command vCommand = this.aParser.getCommand( pCommandLine );

        if ( vCommand.isUnknown() ) {
            this.aGui.println( "I don't know what you mean..." );
            return;
        }

        String vCommandWord = vCommand.getCommandWord();
        if ( vCommandWord.equals( "help" ) ) {
            this.printHelp();
        } else if ( vCommandWord.equals( "go" ) ) {
            this.goRoom( vCommand );
        } else if ( vCommandWord.equals( "look" ) ) {
            if ( vCommand.hasSecondWord() ) {
                this.aGui.println( "Look what? This command has no second word!" );
            } else {
                this.printLocationInfo();
            }
        } else if ( vCommandWord.equals( "back" ) ) {
            if ( vCommand.hasSecondWord() ) {
                this.aGui.println( "Back what? This command has no second word!" );
            } else {
                this.goBack();
            }
        } else if ( vCommandWord.equals( "eat" ) ) {
            if ( vCommand.hasSecondWord() ) {
                this.aGui.println( this.aPlayer.eatItem( vCommand.getSecondWord() ) );
            } else {
                this.aGui.println( "Eat what?" );
            }
        } else if ( vCommandWord.equals( "test" ) ) {
            this.test( vCommand );
        } else if ( vCommandWord.equals( "take" ) ) {
            this.takeItem( vCommand );
        } else if ( vCommandWord.equals( "drop" ) ) {
            this.dropItem( vCommand );
        } else if ( vCommandWord.equals( "items" ) ) {
            this.aGui.println( this.aPlayer.getCarriedItemsString() );
        } else if ( vCommandWord.equals( "quit" ) ) {
            this.quit( vCommand );
        } else if ( vCommandWord.equals( "charge" ) ) {
            this.chargeBeamer( vCommand );
        } else if ( vCommandWord.equals( "fire" ) ) {
            this.fireBeamer( vCommand );
        } else if ( vCommandWord.equals( "unlock" ) ) {
            this.unlockDoor( vCommand );
        } else if ( vCommandWord.equals( "alea" ) ) {
            this.alea( vCommand );
        } else if ( vCommandWord.equals( "bonjour" ) ){
            this.bonjour( vCommand);
        }
    }

    /**
     * Affiche la description de la pièce courante et son image.
     */
    public void printLocationInfo()
    {
        this.aGui.println( this.aPlayer.getCurrentRoom().getLongDescription() );
        if ( this.aPlayer.getCurrentRoom().getImageName() != null ) {
            this.aGui.showImage( this.aPlayer.getCurrentRoom().getImageName() );
        }
    }
} 