package entities;

import dev.gamekit.components.Sprite;
import dev.gamekit.core.Application;
import dev.gamekit.core.Component;
import dev.gamekit.core.Entity;
import dev.gamekit.core.IO;
import dev.gamekit.settings.Settings;

import java.awt.image.BufferedImage;
import java.util.List;

public class Background extends Entity {
  private static final BufferedImage GROUND_IMG = IO.getResourceImage("ground_grass_1.png");

  public Background() {
    super("Background");
  }

  @Override
  protected List<Component> getComponents() {
    Settings settings = Application.getInstance().getSettings();
    Sprite sprite = new Sprite(GROUND_IMG);
    sprite.setSize(settings.resolution.width(), settings.resolution.height());

    return List.of(sprite);
  }
}
