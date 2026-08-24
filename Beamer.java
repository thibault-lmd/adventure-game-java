/**
 * Classe Beamer - téléporteur pouvant être chargé et déclenché.
 * Hérite de Item car c'est un objet que le joueur peut ramasser.
 *
 * @author LAM-DETRAIT Thibault
 * @version 2026.04
 */
public class Beamer extends Item
{
    private Room aChargedRoom; // pièce mémorisée lors de la charge, null si non chargé

    /**
     * Initialise le téléporteur, non chargé au départ.
     */
    public Beamer( final String pName, final String pDescription, final double pWeight )
    {
        super( pName, pDescription, pWeight );
        this.aChargedRoom = null;
    }

    /**
     * Charge le téléporteur en mémorisant la pièce courante.
     */
    public void charge( final Room pRoom )
    {
        this.aChargedRoom = pRoom;
    }

    /**
     * Retourne la pièce mémorisée et remet le téléporteur à zéro.
     * Retourne null si le téléporteur n'est pas chargé.
     */
    public Room fire()
    {
        Room vRoom = this.aChargedRoom;
        this.aChargedRoom = null; // doit être rechargé après utilisation
        return vRoom;
    }

    /**
     * Retourne true si le téléporteur est chargé.
     */
    public boolean isCharged()
    {
        return this.aChargedRoom != null;
    }
}