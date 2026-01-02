package scenes;

import dev.gamekit.animation.Animation;
import dev.gamekit.animation.AnimationCurve;
import dev.gamekit.core.Application;
import dev.gamekit.core.IO;
import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.ui.widgets.Button;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.utils.Vector;
import entities.Background;
import entities.Enclosure;
import entities.Explosion;
import entities.crafts.*;
import entities.landing.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import static dev.gamekit.utils.Math.degToRad;

public class TestScene extends Scene implements Craft.Host {
  private static final BufferedImage NEWSPAPER_IMG = IO.getResourceImage("newspaper.png");

  private final Enclosure enclosure;
  private Animation newspaperAnimation;

  public TestScene() {
    super("Test Scene");

    enclosure = new Enclosure(this, Plane::new, Heli::new, Jet::new, Seaplane::new, Blimp::new);

//    RigidBody.DEBUG_DRAW = true;
//    Collider.DEBUG_DRAW = true;
    Craft.ENABLED = true;
  }

  @Override
  protected void start() {
    addChild(enclosure);
    addChild(new Background());
    addChild(new Airstrip(new Vector(-256, 0), degToRad(30)));
    addChild(new Jetstrip(new Vector(256, 0), degToRad(105)));
    addChild(new Helipad(new Vector(), 0));
    addChild(new Blimpad(new Vector(0, -256), degToRad(105)));
    addChild(new Seaport(new Vector(0, 256), degToRad(30)));
    Application.getInstance().scheduleTask(() -> addChild(enclosure.spawnCraft()), 2500);
    Application.getInstance().scheduleTask(() -> addChild(enclosure.spawnCraft()), 3500);
    Application.getInstance().scheduleTask(() -> addChild(enclosure.spawnCraft()), 5000);
    Application.getInstance().scheduleTask(() -> addChild(enclosure.spawnCraft()), 6500);
    Application.getInstance().scheduleTask(() -> addChild(enclosure.spawnCraft()), 8000);
  }

  @Override
  protected void render() {
    Renderer.clear(Color.BLACK);
  }

  @Override
  protected Widget createUI() {
    return Stack.create(
      Center.create(
        newspaperAnimation != null ?
          Rotated.create(
            newspaperAnimation.getValue() * degToRad(1080),
            Scaled.create(
              Math.max(0.01, newspaperAnimation.getValue()),
              Opacity.create(
                newspaperAnimation.getValue(),
                Image.create(NEWSPAPER_IMG)
              )
            )
          ) : Empty.create()
      ),

      Center.create(
        newspaperAnimation != null ?
          Padding.create(
            600 + (int) (newspaperAnimation.getValue() * 45), 0, 0, 0,
            Opacity.create(
              newspaperAnimation.getValue(),
              Button.create(
                props -> {
                  props.mouseListener = e -> {
                    if (e.type == MouseEvent.Type.CLICK)
                      Application.getInstance().loadScene(new TestScene());
                  };
                },
                Padding.create(
                  12, 12, 24, 12,
                  Text.create("Continue")
                )
              )
            )
          ) : Empty.create()
      )
    );
  }

  @Override
  public void onCraftNearMiss() {
    logger.debug("Crafts missed nearly");
  }

  @Override
  public void onCraftCrash(Vector location) {
    logger.debug("Crafts crashed");
    Craft.ENABLED = false;

    addChild(new Explosion(location));

    if (newspaperAnimation == null) {
      Application.getInstance().scheduleTask(() -> {
        newspaperAnimation = new Animation(1000, Animation.RepeatMode.NONE, AnimationCurve.EASE_OUT_SINE);
        newspaperAnimation.setValueListener(value -> updateUI());
        newspaperAnimation.start();
      }, 1500);
    }
  }

  @Override
  public void onCraftLanded() {
    logger.debug("Crafts landed");
  }
}
