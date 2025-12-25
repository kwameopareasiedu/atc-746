package entities.landing;

import dev.gamekit.core.IO;
import dev.gamekit.utils.Vector;
import utils.Physic;

import java.awt.image.BufferedImage;

public class Blimpad extends Pad {
  private static final BufferedImage SPRITE = IO.getResourceImage("blimp_pad.png");

  public Blimpad(Vector initialPosition, double initialRotation) {
    super("Blimpad", initialPosition, initialRotation);
  }

  @Override
  protected Parameters getParameters() {
    return new Parameters(SPRITE, Physic.Tag.BLIMPAD, Physic.CategoryMask.BLIMPAD, Physic.CollisionMask.BLIMPAD);
  }
}
