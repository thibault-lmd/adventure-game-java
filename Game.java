/**
 * Classe Game - point d'entrée du jeu The Submerged City.
 * Crée et relie le moteur de jeu et l'interface graphique.
 *
 * @author LAM-DETRAIT Thibault
 */
public class Game
{
    private UserInterface aGui;    // interface graphique du jeu
    private GameEngine aEngine;    // moteur de jeu

    /**
     * Initialise le jeu en créant le moteur et l'interface graphique,
     * puis les relie entre eux.
     */
    public Game()
    {
        this.aEngine = new GameEngine();
        this.aGui    = new UserInterface( this.aEngine );
        this.aEngine.setGUI( this.aGui );
    }
}