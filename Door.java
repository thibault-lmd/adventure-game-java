/**
 * Classe Door - représente une porte pouvant être verrouillée.
 * Une porte relie deux pièces et nécessite une clé spécifique pour être ouverte.
 *
 * @author LAM-DETRAIT Thibault
 * @version 2026.04
 */
public class Door
{
    private Room aRoom1;      // première pièce reliée par la porte
    private Room aRoom2;      // deuxième pièce reliée par la porte
    private boolean aLocked;  // true si la porte est verrouillée
    private String aKeyName;  // nom de la clé nécessaire pour ouvrir la porte

    /**
     * Initialise une porte entre deux pièces avec une clé spécifique.
     */
    public Door( final Room pRoom1, final Room pRoom2, final String pKeyName )
    {
        this.aRoom1   = pRoom1;
        this.aRoom2   = pRoom2;
        this.aLocked  = true;
        this.aKeyName = pKeyName;
    }

    /**
     * Retourne true si la porte est verrouillée.
     */
    public boolean isLocked()
    {
        return this.aLocked;
    }

    /**
     * Tente d'ouvrir la porte avec la clé donnée.
     * Retourne true si la clé est la bonne, false sinon.
     */
    public boolean unlock( final String pKeyName )
    {
        if ( this.aKeyName.equals( pKeyName ) ) {
            this.aLocked = false;
            return true;
        }
        return false;
    }

    /**
     * Retourne true si la pièce donnée est l'une des deux pièces reliées par la porte.
     */
    public boolean connectsTo( final Room pRoom )
    {
        return this.aRoom1 == pRoom || this.aRoom2 == pRoom;
    }
    
    public String getKeyName()
    {
        return this.aKeyName;
    }
} 