/**
 *    Copyright 2011-2012 Jim Tse
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jtse.puzzle;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.Point;
import java.io.File;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.Texture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jtse.puzzle.ogl.Region;
import com.github.jtse.puzzle.ogl.RenderBackgroundProvider;
import com.github.jtse.puzzle.ogl.SceneModule;
import com.github.jtse.puzzle.ogl.Textures;
import com.github.jtse.puzzle.physics.Displacement;
import com.github.jtse.puzzle.physics.PhysicsModule;
import com.github.jtse.puzzle.ui.DeltaMouseEventFilter;
import com.github.jtse.puzzle.ui.MouseEvent;
import com.github.jtse.puzzle.ui.MouseModule;
import com.github.jtse.puzzle.ui.MousePoller;
import com.github.jtse.puzzle.ui.UI;
import com.github.jtse.puzzle.util.ScriptModule;
import com.github.jtse.puzzle.util.Scripts.ScriptException;
import com.google.common.base.Supplier;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.ProvisionException;
import com.google.inject.name.Named;

/**
 * @author jtse
 */
public class Puzzle {
  private final Logger log = LoggerFactory.getLogger(Puzzle.class);

  @Inject @Named("@base-path")
  private File basePath;

  @Inject
  private Displacement displacement;

  @Inject
  private MousePoller mousePoller;

  @Inject
  private DeltaMouseEventFilter deltaMouseEventFilter;

  @Inject @Named("@configure-scene")
  private Runnable configureScene;

  @Inject
  private RenderBackgroundProvider renderBackgroundProvider;

  @Inject @Named("@script-repeatable")
  private List<Map<String, String>> images;

  @Inject
  private Supplier<Audio> collisionAudioSupplier;

  /**
   * @param args
   */
  public static void main(String[] args) {
    Logger log = LoggerFactory.getLogger(Puzzle.class);

    File scriptFile = args.length > 0
        ? new File(args[0])
        : UI.filePrompt(System.getProperty("user.dir") + "/puzzle");

    if (scriptFile == null) {
      return;
    }

    Puzzle puzzle = null;
    try {
      puzzle = Guice.createInjector(
              new PuzzleModule(scriptFile.getParentFile()),
              new AudioModule(),
              new SceneModule(),
              new PhysicsModule(),
              new MouseModule(),
              new ScriptModule(scriptFile, "image", "x", "y"))
          .getInstance(Puzzle.class);
    } catch (ProvisionException e) {
      log.error(e.getMessage(), e);
      UI.confirm(e.getCause().getMessage());
      return;
    }

    puzzle.run();
  }

  public void run() {
    int width = Display.getDisplayMode().getWidth();
    int height = Display.getDisplayMode().getHeight();

    try {

      Display.create();
      Audio collisionAudio = collisionAudioSupplier.get();
      // Display.setDisplayMode(new DisplayMode(width, height));
      Runnable renderBackground = renderBackgroundProvider.get();

      Display.setFullscreen(true);

      Display.setVSyncEnabled(true);

      configureScene.run();

      glEnable(GL_TEXTURE_2D);
      glEnable(GL_BLEND);
      glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
      glViewport(0, 0, width, height);
      glMatrixMode(GL11.GL_MODELVIEW);
      glMatrixMode(GL11.GL_PROJECTION);
      glLoadIdentity();
      glOrtho(0, width, height, 0, 1, -1);
      glMatrixMode(GL11.GL_MODELVIEW);

      // init resources
      Texture[] textures = new Texture[images.size()];
      Point[] points = new Point[images.size()];
      Region[] regions = new Region[images.size() + 4]; // 4 is for walls

      for (int i = 0; i < images.size(); i++) {
        textures[i] = Textures.createImage(new File(basePath, images.get(i).get("image")));
        regions[i] = Region.createRegion(textures[i]);
        points[i] = new Point(Integer.parseInt(images.get(i).get("x")), Integer.parseInt(
            images.get(i).get("y")));
      }

      reset(regions, points);

      // Define the wall
      regions[images.size() + 0] = Region.createBlock(width, 1, 0, -1);
      regions[images.size() + 1] = Region.createBlock(1, height, width, 0);
      regions[images.size() + 2] = Region.createBlock(width, 1, 0, height);
      regions[images.size() + 3] = Region.createBlock(1, height, -1, 0);

      boolean quit = false;

      // TODO(jtse): Move collision* to standalone class
      int COLLISION_BITMASK = 1 << 31;
      int COLLISION_VALUE = 3 << 30;
      int collisionBits = 0;
      while (!Display.isCloseRequested() && !quit) {
        collisionBits = collisionBits >>> 1;

        MouseEvent mouseEvent = mousePoller.poll();
        MouseEvent mouseDelta = deltaMouseEventFilter.apply(mouseEvent);

        Display.sync(60);

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
          quit = true;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
          reset(regions, points);
        }

        if (mouseEvent.isButtonDown() && !mouseDelta.isButtonDown()) {
          int x = mouseEvent.getX();
          int y = height - mouseEvent.getY();
          int dx = mouseDelta.getX();
          int dy = mouseDelta.getY();

          for (int i = 0; i < regions.length; i++) {
            if (regions[i].contains(x, y)) {
              regions[i].setDxDy(dx, -dy);

              if (displacement.apply(regions[i], dx, -dy, regions)) {
                collisionBits = collisionBits | COLLISION_BITMASK;
              }
              i = regions.length;
            }
          }
        }
        if (!collisionAudio.isPlaying() && COLLISION_VALUE == collisionBits) {
          collisionAudio.playAsSoundEffect(1.0f, 1.0f, false);
        }

        SoundStore.get().poll(0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        renderBackground.run();

        StringBuilder s = new StringBuilder("Regions ({}): ");
        // Move regions
        for (int i = 0; i < textures.length; i++) {
          int x = regions[i].getX();
          int y = regions[i].getY();

          s.append(x).append(",").append(y);

          if (i != (textures.length - 1)) {
            s.append(",");
          }

          if (!regions[i].isHidden()) {
            Textures.renderImage(textures[i], x, y);
          }
        }
        log.info(s.toString(), textures.length);

        Display.update();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      UI.confirm(e.getClass() == ScriptException.class
          ? "Script file contains errors:\n" + e.getMessage()
          : e.getMessage());
      return;
    } finally {
      if (AL.isCreated()) {
        AL.destroy();
      }
      Display.destroy();
    }
  }

  private static void reset(Region[] regions, Point[] points) {
    for (int i = 0; i < points.length; i++) {
      regions[i].setXY(points[i].x, points[i].y);
      regions[i].setHidden(false);
    }
  }
}