package Client.Game;

import Client.Render.Renderer;
import Client.Window.ClientWindow;
import Client.Window.InputManager;
import Util.ILogicTarget;
import Util.IRenderTarget2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ClientGame implements Runnable
{
	//=====================================================================
	// Singleton Constructor
	//---------------------------------------------------------------------

	/***
	 * ClientGame singleton constructor
	 */
	private ClientGame()
	{
		mLogicTargets = Collections.synchronizedList( new ArrayList<ILogicTarget>() );
		mLogicTargetsToAdd = new ArrayList<>();
		mLogicTargetsToRemove = new ArrayList<>();
	}

	//=====================================================================
	// Static Methods
	//---------------------------------------------------------------------

	/***
	 * Returns the ClientGame singleton creating it if it had not been so
	 * @return ClientGame singleton
	 * @throws InterruptedException mutex interrupted
	 */
	public static ClientGame get() throws InterruptedException
	{
		if(sClientGame == null)
		{
			sClientGameMutex = new Semaphore(1)   ; // we could sync based on the ClientGame
			sClientGameMutex.acquire();                    // but I think using a mutex here gives more flex
			sClientGame = new ClientGame();                // we've locked until startup can finish
			sClientGameMutex.release();
		}

		return sClientGame;
	}

	/***
	 * Adds a logic target to the list to update
	 * @param logicTarget LogicTarget to add
	 */
	public static void addLogicTarget(ILogicTarget logicTarget)
	{
		try
		{
			sClientGameMutex.acquire();
			sClientGame.mLogicTargetsToAdd.add(logicTarget);
			sClientGameMutex.release();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/***
	 * Removes a logicTarget from the list
	 * @param logicTarget LogicTarget to remove
	 */
	public static void removeLogicTarget(ILogicTarget logicTarget)
	{
		try
		{
			sClientGameMutex.acquire();
			sClientGame.mLogicTargetsToRemove.add(logicTarget);
			sClientGameMutex.release();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public static void wipeLogicTarget()
	{
		try
		{
			sClientGameMutex.release();
			sClientGame.mLogicTargetsToRemove.addAll(sClientGame.mLogicTargets);
			sClientGameMutex.acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public static int getWindowWidth()
	{
		return sClientGame.mClientWindow.getWidth();
	}

	public static int getWindowHeight()
	{
		return sClientGame.mClientWindow.getHeight();
	}

	public static void requestWindowClose()
	{
		sClientGame.mClientWindow.requestClose();
	}

	/***
	 * Adds a renderTarget2D to the list of render targets
	 * @param renderTarget2D RenderTarget2D to add
	 */
	public static void addRenderTarget(IRenderTarget2D renderTarget2D)
	{
		try
		{
			sClientGameMutex.acquire();
			sClientGame.mRenderer.addRenderTargets(renderTarget2D);
			sClientGameMutex.release();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/***
	 * Removes a RenderTarget2D from the list of render targets
	 * @param renderTarget2D RenderTarget to remove
	 */
	public static void removeRenderTarget(IRenderTarget2D renderTarget2D)
	{
		try
		{
			sClientGameMutex.acquire();
			sClientGame.mRenderer.removeRenderTargets(renderTarget2D);
			sClientGameMutex.release();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}

	public static void wipeRenderTarget()
	{
		try
		{
			sClientGameMutex.acquire();
			sClientGame.mRenderer.wipeRenderTarget();
			sClientGameMutex.release();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}

	public static void forceRender()
	{
		try
		{
			sClientGameMutex.acquire();
			sClientGame.mRenderer.renderTargets();
			sClientGame.mClientWindow.repaint();
			sClientGameMutex.release();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public static InputManager getInput()
	{
		return sClientGame.mInputManager;
	}

	//=====================================================================
	// Public Methods
	//---------------------------------------------------------------------

	/***
	 * Run the ClientGame
	 */
	@Override
	public void run()
	{
		try
		{
			sClientGameMutex.acquire();
			startUp();
			sClientGameMutex.release();
			gameLoop();
			cleanUp();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	//=====================================================================
	// Protected Methods
	//---------------------------------------------------------------------

	/***
	 * Start up the clientGame
	 * @throws InterruptedException mutex interrupted
	 */
	protected void startUp() throws InterruptedException
	{
		mIsCloseRequested = false;

		mClientWindow = new ClientWindow();

		mInputManager = new InputManager(mClientWindow);

		mRenderer     = new Renderer(
			mClientWindow,
			0,
			0,
			mClientWindow.getWidth(),
			mClientWindow.getHeight()
		);


		mClientWindow.show();
	}

	/***
	 * GameLoop updating targets and rendering targets till the window is closed
	 * @throws InterruptedException mutex interrupted
	 */
	protected void gameLoop() throws InterruptedException
	{
		long startTime = System.nanoTime();
		long priorTime = startTime;
		do
		{
			priorTime = startTime;
			startTime = System.currentTimeMillis();
			long deltaTime = startTime - priorTime;

			if(mLogicTargetsToAdd.size() > 0)
			{
				mLogicTargets.addAll(mLogicTargetsToAdd);
				mLogicTargetsToAdd.clear();
			}

			if(mLogicTargetsToRemove.size() > 0 )
			{
				mLogicTargets.removeAll(mLogicTargetsToRemove);
				mLogicTargetsToRemove.clear();
			}

			// update logicTargets
			for(ILogicTarget t : mLogicTargets)
			{
				t.update(deltaTime);
			}

			mRenderer.renderTargets();
			mInputManager.clear();

			// TODO: framesync
			mIsCloseRequested = mClientWindow.isCloseRequested();
			Thread.sleep(0,100);
		}
		while (!mIsCloseRequested);

	}

	/***
	 * Clean up left over resources after the window closed
	 */
	protected void cleanUp()
	{
		// the window has closed
		mClientWindow.close();
		// release resources
	}

	//=====================================================================
	// Protected variables
	//---------------------------------------------------------------------
	protected final List<ILogicTarget>      mLogicTargets;
	protected final ArrayList<ILogicTarget> mLogicTargetsToAdd;
	protected final ArrayList<ILogicTarget> mLogicTargetsToRemove;

	protected boolean                       mIsCloseRequested;
	protected Renderer                      mRenderer;
	protected ClientWindow                  mClientWindow;
	protected InputManager                  mInputManager;

	//=====================================================================
	// Private variables
	//---------------------------------------------------------------------
	private static ClientGame sClientGame;
	private static Semaphore  sClientGameMutex;
}
