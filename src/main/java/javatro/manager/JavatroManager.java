// @@author flyingapricot
package javatro.manager;

import javatro.core.Ante;
import javatro.core.Deck;
import javatro.core.JavatroCore;
import javatro.core.JavatroException;
import javatro.display.UI;
import javatro.display.screens.Screen;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The {@code JavatroManager} class serves as the main controller (manager) of the game,
 * coordinating interactions between the model ({@code JavatroCore}) and the view ({@code display}).
 * It listens for property changes and updates the game state accordingly.
 */
public class JavatroManager implements PropertyChangeListener {

    /** The main view responsible for rendering the user interface. */
    public static UI ui;

    public static JavatroCore jc;

    public static Ante ante;

    /** The main model responsible for handling game logic. */
    public static int roundCount = 1;

    public static boolean runningTests =
            false; // If tests are running, some settings will be adjusted

    /**
     * Constructs a {@code JavatroManager} and registers it as an observer to the view.
     *
     * @param ui The main view of the game.
     * @param jc The main model of the game.
     */
    public JavatroManager(UI ui, JavatroCore jc) throws JavatroException {
        JavatroManager.ui = ui;
        JavatroManager.jc = jc;
        UI.getParser().addPropertyChangeListener(this); // Register as an observer
    }
    /**
     * Changes the currently displayed screen.
     *
     * @param destinationScreen The new screen to be displayed.
     */
    public static void setScreen(Screen destinationScreen) throws JavatroException {
        ui.setCurrentScreen(destinationScreen);
    }

    /**
     * Begins the game by initializing the game model and registering necessary observers.
     *
     * @throws JavatroException If an error occurs during game initialization.
     */
    public static void beginGame(Deck.DeckType deckType) throws JavatroException {
        jc.setupNewGame(deckType);
    }

    /**
     * Handles property change events from the view. If the property change corresponds to user
     * input, it executes the appropriate command.
     *
     * @param evt The property change event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("userInput")) {
            // Execute the respective command
            try {
                UI.getCurrentScreen().getCommand((int) evt.getNewValue() - 1).execute();
            } catch (JavatroException e) {
                System.out.println(e.getMessage());
                try {
                    ui.setCurrentScreen(UI.getCurrentScreen());
                } catch (JavatroException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
