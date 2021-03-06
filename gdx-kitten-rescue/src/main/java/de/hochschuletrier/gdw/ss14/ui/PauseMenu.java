package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.gamestates.GameStateEnum;

public class PauseMenu extends LaserCatMenu
{
	private PauseMenuListener pauseMenuListener;
	@Override
	public void init(AssetManagerX assetManager)
	{
		super.init(assetManager);
		numberOfButtons = 3;
		name = new String[numberOfButtons];
		name[0] = "Resume";
		name[1] = "Options";
		name[2] = "Main Menu";
		addButtonsToFrame();
		
		pauseMenuListener = new PauseMenuListener();
		
		for (UIButton b:button)
		{
			b.addListener(soundListener);
			b.addListener(this.pauseMenuListener);
			b.setOverAnimation(catSkin, "bell", frameDuration);
		}
	}
	
	protected void addButtonsToFrame()
	{
		button = new UIButton[numberOfButtons];
		label = new Label[numberOfButtons];

		for(int i=0; i<numberOfButtons; i++)
		{
			label[i] = new Label(name[i], catSkin);
			widgetFrame.add(label[i]).expandX().space(20).spaceBottom(10);
		}
		
		widgetFrame.row();
		for(int i = 0; i<numberOfButtons; i++)
		{
			button[i] = new UIButton(catSkin, "bell");
			button[i].setName("bell");
			widgetFrame.add(button[i]).size(Value.percentWidth(widthOfWidgetFrame/6, table)).top().space(20).spaceTop(10);
		}
		name = null;
	}
	
	private class PauseMenuListener extends ClickListener
	{
		public void clicked(InputEvent event, float x, float y)
		{
			for(int i=0; i<numberOfButtons; i++)
			{
				if(button[i] != event.getListenerActor())
					continue;
				
					switch (i)
					{
						case 0:
							GameStateEnum.GAMEPLAY.activate();
							break;
						case 1:
							GameStateEnum.OPTIONSMENU.activate();
							break;
						case 2:
							GameStateEnum.MAINMENU.activate();
							break;
						default:
							System.out.println("You just fucked up");		
							break;
				}
			}
		}
	}
}
