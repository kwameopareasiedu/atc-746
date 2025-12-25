package entities.landing;

import dev.gamekit.core.IO;
import dev.gamekit.utils.Vector;
import utils.Physic;

import java.awt.image.BufferedImage;

public class Helipad extends Pad {
  private static final BufferedImage SPRITE = IO.getResourceImage("helipad.png");

  public Helipad(Vector initialPosition, double initialRotation) {
    super("Helipad", initialPosition, initialRotation);
  }

  @Override
  protected Parameters getParameters() {
    return new Parameters(SPRITE, Physic.Tag.HELIPAD, Physic.CategoryMask.HELIPAD, Physic.CollisionMask.HELIPAD);
  }
}
