package Client.Game.Objects.MainMenu;

import Client.Game.ClientGame;
import Client.Game.Objects.ImageManager;
import Client.Game.Objects.InitObject;
import Client.Game.Objects.StaticImageObject;

public class MainMenuInit extends InitObject
{
	public MainMenuInit()
	{
		super();
	}

	@Override
	protected void setup()
	{
		// title
		int titleWidth  = (int) (362f * 1.4f);
		int titleHeight = (int) (242f * 1.4f);
		StaticImageObject logo = new StaticImageObject(
				0,
				ClientGame.getWindowWidth()/2 - titleWidth/2,
				0,
				titleWidth,
				titleHeight,
				ImageManager.loadImage("Whono/Assets/whonoTilt.png"));

		StaticImageObject background = new StaticImageObject(
				1,
				0,
				0,
				ClientGame.getWindowWidth(),
				ClientGame.getWindowHeight(),
				ImageManager.loadImage("Whono/Assets/BackgroundTile.png"));

		addGameObject(background);
		addGameObject(logo);
		addGameObject(new PlaySoloButton());
		addGameObject(new PlayButton());
		addGameObject(new ExitButton());

	}

}
