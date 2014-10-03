package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sound.LocalMusic;
import de.hochschuletrier.gdw.ss14.states.GameStates;

public class LevelMenu extends LaserCatMenu
{
	private LevelMenuListener levelMenuListener;
	private Integer levelIndex;
	private Label levelLabel;
	private String levelString;
	
	@Override
	public void init(AssetManagerX assetManager)
	{
		super.init(assetManager);
		System.out.println("init LevelMenu");
		
		levelIndex = new Integer(0);
		
		numberOfButtons = 4;
		name = new String[numberOfButtons];
		name[0] = "Start";
		name[1] = "Level-";
		name[2] = "Level+";
		name[3] = "Return";
		addButtonsToFrame();

		levelMenuListener = new LevelMenuListener();

		for (Button b : button)
		{
			b.addListener(LaserCatMenu.soundListener);
			b.addListener(this.levelMenuListener);
		}
		

}

	protected void addButtonsToFrame()
	{
		button = new Button[numberOfButtons];
		label = new Label[numberOfButtons+1];

		label[0] = new Label(name[0], basicSkin);
		label[1] = new Label(name[1], basicSkin);
		label[2] = new Label("Level\n(no works yet)", basicSkin);
		label[3] = new Label(name[2], basicSkin);
		label[4] = new Label(name[3], basicSkin);

		for(Label l: label)
		{
			widgetFrame.add(l).expandX().space(20).spaceBottom(10);
			l.setAlignment(Align.center);
		}
		
		widgetFrame.row();
		for(int i = 0; i<numberOfButtons; i++)
		{
			button[i] = new Button(catSkin, "bell");
			button[i].setName("bell");
		}
		widgetFrame.add(button[0]).size(Value.percentWidth(widthOfWidgetFrame/6, table)).top().space(20).spaceTop(10);
		widgetFrame.add(button[1]).size(Value.percentWidth(widthOfWidgetFrame/6, table)).top().space(20).spaceTop(10);
		
		levelLabel = new Label(levelIndex.toString(), basicSkin);
		widgetFrame.add(levelLabel).center();
		
		widgetFrame.add(button[2]).size(Value.percentWidth(widthOfWidgetFrame/6, table)).top().space(20).spaceTop(10);
		widgetFrame.add(button[3]).size(Value.percentWidth(widthOfWidgetFrame/6, table)).top().space(20).spaceTop(10);
		
		name = null;
	}

	private class LevelMenuListener extends ClickListener
	{
		public void clicked(InputEvent event, float x, float y)
		{
			for (int i = 0; i < numberOfButtons; i++)
			{
				if (button[i] != event.getListenerActor())
					continue;

				switch (i)
				{
				case 0:
					GameStates.GAMEPLAY.activate();
					break;
				case 1:
					levelIndex = levelIndex > 0 ? (levelIndex-1) : 0;
					levelLabel.setText(levelIndex.toString());
					System.out.println("Decrease Level to " + levelIndex);
					break;
				case 2:
					levelIndex = levelIndex < 10 ? (levelIndex+1) : 10;
					levelLabel.setText(levelIndex.toString());
					System.out.println("Increase Level to " + levelIndex);
					break;
				case 3:
					GameStates.MAINMENU.activate();
					break;
				}
			}
		}
	}
}