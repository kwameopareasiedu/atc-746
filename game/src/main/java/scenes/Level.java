package scenes;

import dev.gamekit.audio.AudioClip2D;
import dev.gamekit.audio.AudioGroup;
import dev.gamekit.core.Application;
import dev.gamekit.core.Audio;
import dev.gamekit.core.IO;
import dev.gamekit.core.Scene;
import dev.gamekit.systems.signals.Signal;
import dev.gamekit.ui.widgets.Empty;
import dev.gamekit.ui.widgets.Stack;
import dev.gamekit.ui.widgets.Theme;
import dev.gamekit.ui.widgets.Widget;
import dev.gamekit.utils.Vector;
import entities.Enclosure;
import entities.Explosion;
import entities.crafts.Craft;
import ui.LevelFailedPopup;
import ui.LevelSuccessPopup;

import java.awt.*;

public abstract class Level extends Scene implements Craft.Host {
  private static final Font DEFAULT_FONT = IO.getResourceFont("MontserratAlternates-Bold.otf");

  private static Level current;

  static {
    Audio.preload("bg", new AudioClip2D("gameplay_bg_music.wav", AudioGroup.MUSIC, 0.75));
    Audio.preload("proximity", new AudioClip2D("proximity_sfx.wav", AudioGroup.EFFECTS, 1));
    Audio.preload("landed", new AudioClip2D("landed_sfx.wav", AudioGroup.EFFECTS, 1));
    Audio.preload("crash", new AudioClip2D("boom_sfx.wav", AudioGroup.EFFECTS, 1));
  }

  protected final Enclosure enclosure;
  private final Signal<Class<?>> landingIndicatorSignal;
  private final int totalCrafts;
  private final long craftDelayMs;
  private final long craftIntervalMs;
  private State state = State.IN_PROGRESS;
  private int spawnedCrafts = 0;
  private int landedCrafts = 0;

  public Level(String name, int totalCrafts, long craftDelayMs, long craftIntervalMs, Craft.Creator... craftCreators) {
    super(name);

    this.totalCrafts = totalCrafts;
    this.craftDelayMs = craftDelayMs;
    this.craftIntervalMs = craftIntervalMs;

    enclosure = new Enclosure(this, craftCreators);

    landingIndicatorSignal = new Signal<>();

    Craft.ENABLED = true;
  }

  public static Signal<Class<?>> getLandingIndicatorSignal() {
    return current.landingIndicatorSignal;
  }

  @Override
  protected void start() {
    Application.getInstance().scheduleTask(this::spawnCraft, craftDelayMs);
    Audio.get("bg").play(true);
    Level.current = this;
  }

  @Override
  protected Widget createUI() {
    return Theme.create(
      props -> props.textFont = DEFAULT_FONT,
      Stack.create(
        state == State.FAILED
          ? LevelFailedPopup.create()
          : state == State.SUCCESS
          ? LevelSuccessPopup.create()
          : Empty.create()
      )
    );
  }

  @Override
  public void onCraftNearMiss() {
    Audio.get("proximity").play();
  }

  @Override
  public void onCraftCrash(Vector location) {
    Craft.ENABLED = false;
    Audio.get("crash").play();
    addChild(new Explosion(location));

    state = State.FAILED;
    updateUI();
  }

  @Override
  public void onCraftLanded() {
    landedCrafts++;
    Audio.get("landed").play();

    if (landedCrafts == totalCrafts) {
      state = State.SUCCESS;
      updateUI();

      // TODO: End level
    }
  }

  @Override
  protected void dispose() {
    Level.current = null;
    Audio.get("bg").stop();
  }

  private void spawnCraft() {
    addChild(enclosure.spawnCraft());
    spawnedCrafts++;

    if (spawnedCrafts < totalCrafts) {
      Application.getInstance().scheduleTask(this::spawnCraft, craftIntervalMs);
    }
  }

  private enum State {
    IN_PROGRESS, SUCCESS, FAILED
  }
}
