/**
 * Classe Item - représente un objet pouvant se trouver dans une pièce ou être porté par le joueur.
 * Chaque item possède un nom, une description et un poids.
 *
 * @author LAM-DETRAIT Thibault
 * @version 2026.04
 */
public class Item
{
    private String aName;   // nom court utilisé pour identifier l'item dans les commandes
    private String aDescription;    // description détaillée affichée au joueur
    private double aWeight;     // poids de l'item en kilogrammes

    /**
     * Initialise un item avec son nom, sa description et son poids.
     */
    public Item( final String pName, final String pDescription, final double pWeight )
    {
        this.aName        = pName;
        this.aDescription = pDescription;
        this.aWeight      = pWeight;
    }

    /**
     * Retourne le nom court de l'item.
     */
    public String getName()
    {
        return this.aName;
    }

    /**
     * Retourne la description détaillée de l'item.
     */
    public String getDescription()
    {
        return this.aDescription;
    }

    /**
     * Retourne le poids de l'item.
     */
    public double getWeight()
    {
        return this.aWeight;
    }

    /**
     * Retourne une représentation textuelle de l'item
     * avec son nom, sa description et son poids.
     */
    public String toString()
    {
        return this.aName + ": " + this.aDescription + " (weight: " + this.aWeight + "kg)";
    }
} 