package Client.Game.Objects;

import Client.Game.Objects.GameScreenSolo.BoardSolo;

import java.util.ArrayList;

public class GameStateSolo extends GameObject
{

    // Possible Game States

    public enum State {
        MAINMENU,PLAYERJOIN,GAMESTART,GAMESTAT,GAMEOVER,LEADERBOARD
    }

    private State currentState = State.MAINMENU;

    public State getCurrentState(){ return  currentState;}

    public static GameStateSolo createGameState(int numberOfPlayers)
    {
        GameStateSolo g = new GameStateSolo(numberOfPlayers);

        g.mDeck = DeckFactory.createDeck();
        g.mHands = new ArrayList<Hand>();

        for(int i = 0; i < 1; i++)
        {
            g.mHands.add(HandFactory.createHand(g.mDeck));
            g.mBoard.addHand(g.mHands.get(i));
        }

        g.mBoard.addDeck(g.mDeck);

        g.getBoard().addToPile(g.mDeck.drawCard());


        return g;
    }

    public GameStateSolo(int numberOfPlayers)
    {
        super(-1);
        mBoard = new BoardSolo();
    }

    public BoardSolo getBoard()
    {
        return mBoard;
    }

    @Override
    public void update(long deltaTime)
    {
        /*
        if(ClientGame.getInput().getKeyPressed('q'))
        {
            // Close window
            // Theres no way to access mIsCloseRequested to close the window
        }
        // Perform behaviour based on current state
        switch(getCurrentState())
        {
            case MAINMENU:
                // Draw main menu
                break;
            case GAMESTART:
                // Initialise game
                break;
            case LEADERBOARD:
                // Draw leaderboard
                break;
            case PLAYERJOIN:
                // Player Join Game
                break;
            case GAMEOVER:
                // Game over condition check
                break;
            case GAMESTAT:
                // Game state 'increment'
                break;
            default:
                System.out.println("Invalid State in GameState.");
        }
         */
    }

    protected Deck mDeck;
    protected ArrayList<Hand> mHands;
    protected BoardSolo mBoard;

}
