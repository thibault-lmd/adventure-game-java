import java.util.HashMap;
import java.util.Set;


/**
 * Classe Room - représente une pièce dans The Submerged City.
 * Chaque pièce a une description, des sorties vers d'autres pièces,
 * une image et une liste d'items.
 *
 * @author LAM-DETRAIT Thibault
 * @version 2026.04
 */
public class Room
{
    private String aDescription;          // description de la pièce
    private HashMap<String, Room> aExits; // sorties disponibles depuis cette pièce
    private String aImageName;            // nom du fichier image associé à la pièce
    private ItemList aItems;              // items présents dans la pièce
    private HashMap<String, Door> aDoors; // clé = nom de la clé, valeur = la porte
    private boolean aHasMonster; // true si la pièce contient un monstre

    /**
     * Initialise une pièce avec sa description et son image.
     * Les sorties et les items sont vides au départ.
     */
    public Room( final String pDescription, final String pImage )
    {
        this.aDescription = pDescription;
        this.aExits       = new HashMap<String, Room>();
        this.aImageName   = pImage;
        this.aItems       = new ItemList();
        this.aDoors       = new HashMap<String, Door>();
        this.aHasMonster = false;
    }
    
    
    /**
     * Ajoute un item dans la pièce.
     */
    public void addItem( final Item pItem )
    {
        this.aItems.addItem( pItem );
    }

    /**
     * Retire un item de la pièce par son nom et le retourne.
     * Retourne null si l'item est introuvable.
     */
    public Item removeItem( final String pItemName )
    {
        return this.aItems.removeItem( pItemName );
    }

    /**
     * Retourne la description des items présents dans la pièce.
     */
    public String getItemString()
    {
        return this.aItems.getItemString( "No item here.", "Items:" );
    }
    
    /**
     * Retourne true si la pièce passée en paramètre est une sortie de cette pièce.
     */
    public boolean isExit( final Room pRoom )
    {
        return this.aExits.containsValue( pRoom );
    }

    /**
     * Retourne la description courte de la pièce.
     */
    public String getDescription()
    {
        return this.aDescription;
    }

    /**
     * Retourne la pièce voisine dans la direction indiquée,
     * ou null si aucune sortie n'existe dans cette direction.
     */
    public Room getExit( final String pDirection )
    {
        Room vRoom = aExits.get( pDirection );
        if ( vRoom != null && this.getLockedDoor( vRoom ) != null ) {
            return null;
        }
        return vRoom;
    }
    
    /**
     * Définit si la pièce contient un monstre.
     */
    public void setMonster( final boolean pHasMonster )
    {
        this.aHasMonster = pHasMonster;
    }
    
    /**
     * Retourne true si la pièce contient un monstre.
     */
    public boolean hasMonster()
    {
        return this.aHasMonster;
    }
    
    /**
     * Définit une sortie depuis cette pièce dans la direction donnée.
     */
    public Room setExits( final String pDirection, final Room pClose )
    {
        return aExits.put( pDirection, pClose );
    }

    /**
     * Retourne une description complète de la pièce :
     * sa description, ses sorties et ses items.
     */
    public String getLongDescription()
    {
        return "You are " + aDescription + ".\n" + getExitString() + "\n"
             + getItemString();
    }

    /**
     * Retourne la liste des sorties disponibles depuis cette pièce.
     */
    public String getExitString()
    {
        String vReturnString = "Exits:";
        Set<String> keys = aExits.keySet();
        for ( String exit : keys ) {
            vReturnString += " " + exit;
        }
        return vReturnString;
    }

    /**
     * Retourne le nom du fichier image associé à cette pièce.
     */
    public String getImageName()
    {
        return this.aImageName;
    }
    
        /**
     * Ajoute une porte verrouillée à cette pièce.
     */
    public void addDoor( final Door pDoor )
    {
        this.aDoors.put( pDoor.getKeyName(), pDoor );
    }
    
    /**
     * Retourne la porte verrouillée menant vers la pièce indiquée, ou null si aucune.
     */
    public Door getLockedDoor( final Room pRoom )
    {
        for ( Door vDoor : this.aDoors.values() ) {
            if ( vDoor.connectsTo( pRoom ) && vDoor.isLocked() ) {
                return vDoor;
            }
        }
        return null;
    }
    
    /**
     * Retourne la porte correspondant à la clé donnée, ou null si aucune.
     */
    public Door getLockedDoorByKey( final String pKeyName )
    {
        return this.aDoors.get( pKeyName );
    }

    /**
     * Retourne la pièce voisine sans vérifier les portes verrouillées.
     */
    public Room getExitIgnoreDoors( final String pDirection )
    {
        return aExits.get( pDirection );
    }
}