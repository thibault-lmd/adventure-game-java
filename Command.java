/**
 * Classe Command - représente une commande tapée par le joueur.
 * Une commande est composée d'un premier mot et
 * d'un second mot optionnel .
 *
 * @author LAM-DETRAIT Thibault
 * @version 2026.04
 */
public class Command
{
    private String aCommandWord; // premier mot de la commande
    private String aSecondWord;  // second mot optionnel, null si absent

    /**
     * Initialise une commande avec son mot principal et son complément éventuel.
     */
    public Command( final String pCommandWord, final String pSecondWord )
    {
        this.aCommandWord = pCommandWord;
        this.aSecondWord  = pSecondWord;
    }

    /**
     * Retourne le premier mot de la commande.
     */
    public String getCommandWord()
    {
        return this.aCommandWord;
    }

    /**
     * Retourne le second mot de la commande.
     */
    public String getSecondWord()
    {
        return this.aSecondWord;
    }

    /**
     * Retourne true si la commande contient un second mot.
     */
    public boolean hasSecondWord()
    {
        return this.aSecondWord != null;
    }

    /**
     * Retourne true si le premier mot de la commande est inconnu.
     */
    public boolean isUnknown()
    {
        return this.aCommandWord == null;
    }
} 