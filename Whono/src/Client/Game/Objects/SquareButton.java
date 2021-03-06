package Client.Game.Objects;

import Client.Game.ClientGame;

import java.awt.*;
import java.util.ArrayList;

public abstract class SquareButton extends Button
{
	public SquareButton(long ID)
	{
		super(ID);
	}

	public SquareButton(long ID, Image singleSprite)
	{
		super(ID, singleSprite);
	}

	public SquareButton(long ID, float x, float y, int width, int height, Image sprite)
	{
		super(ID, x, y, width, height, sprite);
	}

	public SquareButton(long ID, float x, float y, int width, int height, ArrayList<Image> spriteList)
	{
		super(ID, x, y, width, height, spriteList);
	}

	public SquareButton(long ID, float x, float y, int width, int height, String name)
	{
		super(ID, x, y, width, height, name);
	}

	@Override
	public void update(long deltaTime)
	{
		if( isInBounds( ClientGame.getInput().getMouseX(), ClientGame.getInput().getMouseY() )
		    && ClientGame.getInput().getMouseLeftPressed() ) // if the mouse is released in bounds
		{
			onMousePress();
		}

		if( isInBounds( ClientGame.getInput().getMouseX(), ClientGame.getInput().getMouseY() )
			&& ClientGame.getInput().getMouseLeftReleased() ) // if the mouse is released in bounds
		{
			onMouseRelease();
		}}

	@Override
	public boolean isInBounds(int x, int y)
	{
		return x > mX
			&& x < mX + mWidth
			&& y > mY
			&& y < mY + mHeight;
	}

}
