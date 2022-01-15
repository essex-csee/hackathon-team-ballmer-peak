package Client.Game.Objects.GameScreen;

import Client.Game.Objects.InitObject;
import Client.Game.Objects.MainMenu.PlayButton;

public class GameScreenInit extends InitObject
{
    public GameScreenInit()
    {
        super();
    }

    int cardID = 0;

    public void initMenu()
    {
        ExitButton quit = new ExitButton();
        addGameObject(quit);
        PauseButton pause = new PauseButton();
        addGameObject(pause);
    }

    public void initDeck()
    {
        // TODO: CardButton needs parameters for the Type/Colour
        // ID: 2
        CardButton deck = new CardButton();
        addGameObject(deck);
    }

    public void initPile()
    {
        CardButton pile = new CardButton();
        addGameObject(pile);
        // ID: 3
    }

    public void initHand()
    {
        // Get player Hand
        // ID: 4 - handsize
        //
        // iterate through player hand
        //      CardButton offset
        //      CardButton c = new CardButton();
        //      addGameObject(c);
    }

    @Override
    protected void setup()
    {
        // Get GameState
        initMenu();
        initDeck();
        initPile();
        initHand();
    }

}
