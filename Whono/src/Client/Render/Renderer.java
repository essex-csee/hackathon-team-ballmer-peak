package Client.Render;

import Client.Window.ClientWindow;
import Util.IRenderTarget2D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Renderer extends JPanel
{
	//=====================================================================
	// Constructor
	//---------------------------------------------------------------------
	public Renderer(ClientWindow window, int x, int y, int width, int height )
	{
		super();
		window.add(this);

		this.mRenderTargets2D         = new ArrayList<IRenderTarget2D>();
		this.mRenderTargets2DToAdd    = new ArrayList<>();
		this.mRenderTargets2DToRemove = new ArrayList<>();
		this.listMutex                = new Semaphore(1);


		this.mX = x;
		this.mY = y;
		this.mWidth  = width;
		this.mHeight = height;
	}

	//=====================================================================
	// Methods
	//---------------------------------------------------------------------


	@Override
	public void repaint()
	{
		super.repaint();
	}

	@Override
	public void paintComponent(Graphics g)
	{

		g.clearRect(0, 0, mWidth, mHeight);

		try
		{
			listMutex.acquire();

			if (!mRenderTargets2DToRemove.isEmpty())
			{
				mRenderTargets2D.removeAll(mRenderTargets2DToRemove);
				mRenderTargets2D.clear();

			}

			if (!mRenderTargets2DToAdd.isEmpty())
			{
				mRenderTargets2D.addAll(mRenderTargets2DToAdd);
				mRenderTargets2DToAdd.clear();

			}

			listMutex.release();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		((ArrayList<IRenderTarget2D>) mRenderTargets2D.clone()).parallelStream().forEachOrdered(t -> t.draw((Graphics2D) g));

	}

	public void addRenderTargets( IRenderTarget2D target )
	{
		try
		{
			listMutex.acquire();
			if (target != null)
			{
				mRenderTargets2DToAdd.add(target);
			}
			listMutex.release();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void removeRenderTargets( IRenderTarget2D target )
	{
		try
		{
			listMutex.acquire();
			if (target != null)
			{
				mRenderTargets2DToRemove.add(target);
			}
			listMutex.release();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void wipeRenderTarget()
	{
		mRenderTargets2DToAdd.clear();
		mRenderTargets2DToRemove.clear();
		mRenderTargets2D.clear();
	}

	public void renderTargets()
	{
		this.repaint();
	}

	protected final ArrayList<IRenderTarget2D>            mRenderTargets2D;
	protected final ArrayList<IRenderTarget2D>            mRenderTargets2DToAdd;
	protected final ArrayList<IRenderTarget2D>            mRenderTargets2DToRemove;
	protected final Semaphore                             listMutex;

	protected final int mX;
	protected final int mY;
	protected final int mWidth;
	protected final int mHeight;
}
