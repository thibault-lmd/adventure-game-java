/**
 * Classe TransporterRoom - pièce spéciale qui téléporte le joueur aléatoirement.
 * Hérite de Room et redéfinit getExit() pour retourner une pièce aléatoire.
 *
 * @author LAM-DETRAIT Thibault
 * @version 2026.04
 */
public class TransporterRoom extends Room
{
    private RoomRandomizer aRandomizer; // choisit la pièce de destination aléatoire

    /**
     * Initialise la pièce téléporteur avec son image et son randomizer.
     */
    public TransporterRoom( final String pDescription, final String pImage, final RoomRandomizer pRandomizer )
    {
        super( pDescription, pImage );
        this.aRandomizer = pRandomizer;
    }

    /**
     * Retourne une pièce aléatoire quelle que soit la direction indiquée.
     */
    @Override
    public Room getExit( final String pDirection )
    {
        return this.aRandomizer.getRandomRoom();
    }

    /**
     * Retourne une pièce aléatoire quelle que soit la direction indiquée.
     * Redéfinit getExitIgnoreDoors() pour que le comportement soit cohérent.
     */
    @Override
    public Room getExitIgnoreDoors( final String pDirection )
    {
        return this.aRandomizer.getRandomRoom();
    }
}