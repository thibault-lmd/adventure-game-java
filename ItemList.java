import java.util.HashMap;

/**
 * Classe ItemList - gère une collection d'items.
 * Utilisée par Room et Player pour stocker leurs items
 * sans que la collection soit accessible depuis l'extérieur 
 * comme demandé dans l'énoncé du 7.31.1
 *
 * @author LAM-DETRAIT Thibault
 * @version 2026.04
 */
public class ItemList
{
    private HashMap<String, Item> aItems; // clé = nom de l'item, valeur = l'item

    /**
     * Initialise une liste d'items vide.
     */
    public ItemList()
    {
        this.aItems = new HashMap<String, Item>();
    }

    /**
     * Ajoute un item à la liste en utilisant son nom comme clé.
     */
    public void addItem( final Item pItem )
    {
        this.aItems.put( pItem.getName(), pItem );
    }

    /**
     * Retire un item de la liste par son nom et le retourne.
     * Retourne null si aucun item ne correspond.
     */
    public Item removeItem( final String pItemName )
    {
        return this.aItems.remove( pItemName );
    }

    /**
     * Retourne true si la liste ne contient aucun item.
     */
    public boolean isEmpty()
    {
        return this.aItems.isEmpty();
    }

    /**
     * Retourne la liste de tous les items avec leur poids total.
     * Affiche pEmptyMessage si la liste est vide,
     * sinon affiche pPrefix suivi de chaque item et du poids total.
     */
    public String getItemString( final String pEmptyMessage, final String pPrefix )
    {
        if ( this.aItems.isEmpty() ) {
            return pEmptyMessage;
        }
        double vTotalWeight = 0.0;
        String vResult = pPrefix;
        for ( Item vItem : this.aItems.values() ) {
            vResult      += "\n  - " + vItem.toString();
            vTotalWeight += vItem.getWeight();
        }
        vResult += "\nTotal weight: " + vTotalWeight + "kg";
        return vResult;
    }
} 