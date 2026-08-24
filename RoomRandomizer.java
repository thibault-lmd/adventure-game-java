import java.util.HashMap;
import java.util.Random;

/**
 * Classe RoomRandomizer - choisit une pièce aléatoire parmi une liste de pièces.
 *
 * @author LAM-DETRAIT Thibault
 * @version 2026.04
 */
public class RoomRandomizer
{
    private HashMap<String, Room> aRooms; // liste des pièces accessibles par téléportation
    private Random aRandom;         // générateur de nombres aléatoires
    private Room aForcedRoom;       // piece forcée pour les tests qui vaut null si le tirage est aléatoire
    
    /**
     * Initialise le randomizer avec une liste vide de pièces.
     */
    public RoomRandomizer()
    {
        this.aRooms = new HashMap<String, Room>();
        this.aRandom = new Random();
        this.aForcedRoom = null;
    }

    /**
     * Ajoute une pièce dans la liste des pièces accessibles.
     */
    public void addRoom( final String pId, final Room pRoom )
    {
        this.aRooms.put( pId, pRoom );
    }

    /**
     * Retourne une pièce aléatoire parmi la liste.
     */
    public Room getRandomRoom()
    {
        if ( this.aForcedRoom != null ) {
            return this.aForcedRoom;
        }
        Room[] vRooms = this.aRooms.values().toArray( new Room[0] );
        return vRooms[ this.aRandom.nextInt( vRooms.length ) ];
    }
    
    /**
     * Force la prochaine téléportation vers une pièce précise (mode test uniquement).
     */
    public void setForcedRoom( final Room pRoom )
    {
        this.aForcedRoom = pRoom;
    }
    
    /**
     * Remet le tirage en mode aléatoire réel.
     */
    public void clearForcedRoom()
    {
        this.aForcedRoom = null;
    }
    
    /**
     * Retourne la pièce correspondant à l'identifiant donné, ou null si introuvable.
     */
    public Room getRoom( final String pId )
    {
        return this.aRooms.get( pId );
    }
} 