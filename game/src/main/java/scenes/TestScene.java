package scenes;

import dev.gamekit.utils.Vector;
import entities.Background;
import entities.crafts.*;
import entities.landing.*;

import static dev.gamekit.utils.Math.degToRad;

public class TestScene extends Level {
  public TestScene() {
    super("Test Scene", 5, 1500, 1000, Plane::new, Heli::new, Jet::new, Seaplane::new, Blimp::new);
  }

  @Override
  protected void start() {
    super.start();

    addChild(new Background());
    addChild(enclosure);
    addChild(new Airstrip(new Vector(-256, 0), degToRad(30)));
    addChild(new Jetstrip(new Vector(256, 0), degToRad(105)));
    addChild(new Helipad(new Vector(), 0));
    addChild(new Blimpad(new Vector(0, -256), degToRad(105)));
    addChild(new Seaport(new Vector(0, 256), degToRad(30)));
  }
}
