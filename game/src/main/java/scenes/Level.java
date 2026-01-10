package scenes;

import dev.gamekit.animation.Animation;
import dev.gamekit.animation.AnimationCurve;
import dev.gamekit.core.Application;
import dev.gamekit.core.IO;
import dev.gamekit.core.Scene;
import dev.gamekit.systems.signals.Signal;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.utils.Vector;
import entities.Enclosure;
import entities.Explosion;
import entities.crafts.Craft;

import java.awt.image.BufferedImage;

import static dev.gamekit.utils.Math.degToRad;

public abstract class Level extends Scene implements Craft.Host {
  private static final BufferedImage NEWSPAPER_IMG = IO.getResourceImage("newspaper.png");
  private static Level current;

  protected final Enclosure enclosure;

  private final Signal<Class<?>> landingIndicatorSignal;
  private final Animation newspaperAnimation;
  private final Animation restartUiAnimation;
  private final int totalCrafts;
  private final long craftDelayMs;
  private final long craftIntervalMs;
  private int spawnedCrafts = 0;
  private int landedCrafts = 0;

  public Level(String name, int totalCrafts, long craftDelayMs, long craftIntervalMs, Craft.Creator... craftCreators) {
    super(name);

    this.totalCrafts = totalCrafts;
    this.craftDelayMs = craftDelayMs;
    this.craftIntervalMs = craftIntervalMs;

    enclosure = new Enclosure(this, craftCreators);

    landingIndicatorSignal = new Signal<>();

    restartUiAnimation = new Animation(1000, Animation.RepeatMode.NONE, AnimationCurve.EASE_OUT_BOUNCE);
    restartUiAnimation.setValueListener(value -> updateUI());

    newspaperAnimation = new Animation(1000, Animation.RepeatMode.NONE, AnimationCurve.EASE_OUT_SINE);
    newspaperAnimation.setValueListener(value -> {
      updateUI();

      if (value > 0.85 && restartUiAnimation.getValue() == 0) {
        restartUiAnimation.start();
      }
    });

    Craft.ENABLED = true;
  }

  public static Signal<Class<?>> getLandingIndicatorSignal() {
    return current.landingIndicatorSignal;
  }

  @Override
  protected void start() {
    Application.getInstance().scheduleTask(this::spawnCraft, craftDelayMs);
    Level.current = this;
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
        restartUiAnimation != null ?
          Padding.create(
            600 + (int) (restartUiAnimation.getValue() * 45), 0, 0, 0,
            Opacity.create(
              restartUiAnimation.getValue(),
              Button.create(
                props -> props.mouseListener = e -> {
                  if (e.type == MouseEvent.Type.CLICK)
                    Application.getInstance().loadScene(new TestScene());
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
    Application.getInstance().scheduleTask(newspaperAnimation::start, 1500);
  }

  @Override
  public void onCraftLanded() {
    logger.debug("Crafts landed");
    landedCrafts++;

    if (landedCrafts == totalCrafts) {
      // TODO: End level
    }
  }

  @Override
  protected void dispose() {
    Level.current = null;
  }

  private void spawnCraft() {
    addChild(enclosure.spawnCraft());
    spawnedCrafts++;

    if (spawnedCrafts < totalCrafts) {
      Application.getInstance().scheduleTask(this::spawnCraft, craftIntervalMs);
    }
  }
}
