import java.util.Stack;

/**
 * Classe Player - représente le joueur dans The Submerged City.
 * Stocke la position courante, l'historique des déplacements et les items portés.
 *
 * @author LAM-DETRAIT Thibault
 * @version 2026.04
 */
public class Player
{
    private String aName;
    private Room aCurrentRoom;
    private Stack<Room> aPreviousRooms; // historique des pièces visitées
    private ItemList aCarriedItems;     // items actuellement portés par le joueur
    private double aMaxWeight;          // poids maximum que le joueur peut porter
    private double aCurrentWeight;      // poids total actuellement porté
    private int aMovesLeft;             // oxygene restant
    private boolean aHasOxygen;         // true si le joueur a utilisé une bouteille d'oxygène
    private boolean aHasFins;           // true si le joueur porte les palmes

    /**
     * Initialise le joueur avec son nom, sa pièce de départ et son poids maximum.
     */
    public Player( final String pName, final Room pStartRoom, final double pMaxWeight )
    {
        this.aName          = pName;
        this.aCurrentRoom   = pStartRoom;
        this.aPreviousRooms = new Stack<Room>();
        this.aCarriedItems  = new ItemList();
        this.aMaxWeight     = pMaxWeight;
        this.aCurrentWeight = 0.0;
        this.aMovesLeft     = 5;
        this.aHasOxygen     = false;
        this.aHasFins       = false;
    }

    /**
     * Retourne la pièce où se trouve le joueur.
     */
    public Room getCurrentRoom() { return this.aCurrentRoom; }

    /**
     * Retourne le nom du joueur.
     */
    public String getName() { return this.aName; }

    /**
     * Retourne le nombre de mouvements d'oxygène restants.
     */
    public int getMovesLeft() { return this.aMovesLeft; }

    /**
     * Retourne true si le joueur a utilisé une bouteille d'oxygène.
     */
    public boolean hasOxygen() { return this.aHasOxygen; }

    /**
     * Ajoute 30 mouvements d'oxygène quand le joueur utilise la bouteille.
     */
    public void equipOxygen()
    {
        this.aHasOxygen = true;
        this.aMovesLeft = 30;
    }

    /**
     * Retourne true si l'historique des pièces visitées est vide.
     */
    public boolean isPreviousRoomsEmpty()
    {
        return this.aPreviousRooms.isEmpty();
    }

    /**
     * Décrémente l'oxygène restant à chaque déplacement.
     * Retourne true si le joueur est à court d'oxygène, false sinon.
     */
    public boolean decrementMoves()
    {
        this.aMovesLeft--;
        return this.aMovesLeft <= 0;
    }

    /**
     * Déplace le joueur vers une nouvelle pièce et sauvegarde la pièce courante dans l'historique.
     */
    public void goRoom( final Room pNextRoom )
    {
        this.aPreviousRooms.push( this.aCurrentRoom );
        this.aCurrentRoom = pNextRoom;
    }

    /**
     * Retourne le joueur dans la pièce précédente en dépilant l'historique.
     * Retourne null si le joueur est déjà au point de départ ou face à une trap door.
     */
    public Room goBack()
    {
        if ( this.aPreviousRooms.isEmpty() ) {
            return null;
        }
        Room vPreviousRoom = this.aPreviousRooms.peek();
        if ( !this.aCurrentRoom.isExit( vPreviousRoom ) ) {
            return null;
        }
        this.aCurrentRoom = this.aPreviousRooms.pop();
        return this.aCurrentRoom;
    }

    /**
     * Charge le téléporteur porté par le joueur avec la pièce courante.
     */
    public String chargeBeamer( final String pItemName )
    {
        Item vItem = this.aCarriedItems.removeItem( pItemName );
        if ( vItem == null ) {
            return "You are not carrying '" + pItemName + "'!";
        }
        if ( !( vItem instanceof Beamer ) ) {
            this.aCarriedItems.addItem( vItem );
            return "This item is not a beamer!";
        }
        Beamer vBeamer = (Beamer) vItem;
        vBeamer.charge( this.aCurrentRoom );
        this.aCarriedItems.addItem( vBeamer );
        return "Beamer charged in " + this.aCurrentRoom.getDescription() + "!";
    }

    /**
     * Déclenche le téléporteur et transporte le joueur dans la pièce mémorisée.
     */
    public String fireBeamer( final String pItemName )
    {
        Item vItem = this.aCarriedItems.removeItem( pItemName );
        if ( vItem == null ) {
            return "You are not carrying '" + pItemName + "'!";
        }
        if ( !( vItem instanceof Beamer ) ) {
            this.aCarriedItems.addItem( vItem );
            return "This item is not a beamer!";
        }
        Beamer vBeamer = (Beamer) vItem;
        if ( !vBeamer.isCharged() ) {
            this.aCarriedItems.addItem( vBeamer );
            return "The beamer is not charged!";
        }
        Room vRoom = vBeamer.fire();
        this.aPreviousRooms.push( this.aCurrentRoom );
        this.aCurrentRoom = vRoom;
        this.aCarriedItems.addItem( vBeamer );
        return "You have been teleported!";
    }

    /**
     * Tente d'ouvrir une porte verrouillée avec un item porté par le joueur.
     */
    public String unlockDoor( final String pKeyName )
    {
        Item vKey = this.aCarriedItems.removeItem( pKeyName );
        if ( vKey == null ) {
            return "You are not carrying '" + pKeyName + "'!";
        }
        Door vDoor = this.aCurrentRoom.getLockedDoorByKey( pKeyName );
        if ( vDoor == null ) {
            this.aCarriedItems.addItem( vKey );
            return "There is no locked door for this key here!";
        }
        vDoor.unlock( pKeyName );
        this.aCarriedItems.addItem( vKey );
        return "You unlocked the door with " + pKeyName + "!";
    }

    /**
     * Tente de ramasser un item dans la pièce courante.
     * Vérifie que le poids total ne dépasse pas le maximum autorisé.
     * Met à jour aHasFins si le joueur ramasse les palmes.
     */
    public String takeItem( final String pItemName )
    {
        Item vItem = this.aCurrentRoom.removeItem( pItemName );
        if ( vItem == null ) {
            return "There is no item called '" + pItemName + "' here!";
        }
        if ( this.aCurrentWeight + vItem.getWeight() > this.aMaxWeight ) {
            this.aCurrentRoom.addItem( vItem );
            return "Too heavy! You can only carry " + ( this.aMaxWeight - this.aCurrentWeight ) + "kg more.";
        }
        if ( vItem.getName().equals( "Fins" ) ) {
            this.aHasFins = true;
        }
        this.aCarriedItems.addItem( vItem );
        this.aCurrentWeight += vItem.getWeight();
        return "You picked up: " + vItem.toString();
    }

    /**
     * Applique une pénalité d'oxygène quand le joueur rencontre un monstre.
     * Sans palmes : perd 2 déplacements. Avec palmes : perd seulement 1.
     * Retourne un message indiquant la pénalité appliquée.
     */
    public String monsterPenalty() 
    {
        if ( this.aHasFins ) {
            this.aMovesLeft -= 1;
            return "A monster is here! Your fins protect you, you only lose 1 extra oxygen move!";
        } else {
            this.aMovesLeft -= 2;
            return "A monster is here! You lose 2 extra oxygen moves!";
        }
    }

    /**
     * Dépose un item porté dans la pièce courante.
     * Met à jour aHasFins si le joueur dépose les palmes.
     */
    public String dropItem( final String pItemName )
    {
        if ( this.aCarriedItems.isEmpty() ) {
            return "You are not carrying anything!";
        }
        Item vItem = this.aCarriedItems.removeItem( pItemName );
        if ( vItem == null ) {
            return "You are not carrying '" + pItemName + "'!";
        }
        if ( vItem.getName().equals( "Fins" ) ) {
            this.aHasFins = false;
        }
        this.aCurrentRoom.addItem( vItem );
        this.aCurrentWeight -= vItem.getWeight();
        return "You dropped: " + vItem.toString();
    }

    /**
     * Mange un item porté par le joueur.
     * Si c'est le Cookie magique, le poids maximum est doublé.
     * Si c'est la Bottle, le joueur gagne 30 déplacements d'oxygène.
     */
    public String eatItem( final String pItemName )
    {
        Item vItem = this.aCarriedItems.removeItem( pItemName );
        if ( vItem == null ) {
            return "You are not carrying '" + pItemName + "'!";
        }
        if ( vItem.getName().equals( "Cookie" ) ) {
            this.aMaxWeight *= 2;
            return "You ate the magic cookie! You can now carry " + this.aMaxWeight + "kg.";
        }
        if ( vItem.getName().equals( "Bottle" ) ) {
            this.equipOxygen();
            return "You equipped the oxygen bottle! You can now breathe freely.";
        }
        this.aCurrentWeight -= vItem.getWeight();
        return "You ate " + vItem.getName();
    }

    /**
     * Retourne la liste des items portés avec le poids actuel et le poids maximum.
     */
    public String getCarriedItemsString()
    {
        return this.aCarriedItems.getItemString(
            "You are not carrying anything.",
            "You are carrying (" + this.aCurrentWeight + "/" + this.aMaxWeight + "kg):"
        );
    }
}