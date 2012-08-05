/**
 * 
 */
package com.github.jtse.puzzle;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jtse.puzzle.ogl.Region;
import com.github.jtse.puzzle.physics.Displacement;
import com.github.jtse.puzzle.physics.ZeroDisplacement;
import com.github.jtse.puzzle.util.ScriptUtils;
import com.github.jtse.puzzle.util.ScriptUtils.ScriptException;
import com.github.jtse.puzzle.util.UI;

/**
 * @author jtse
 * 
 */
public class Puzzle {
  private static final Logger log = LoggerFactory.getLogger(Puzzle.class);

  /**
   * @param args
   */
  public static void main(String[] args) {
    File scriptFile = args.length > 0 ? new File(args[0]) : UI.filePrompt(System
        .getProperty("user.dir") + "/puzzle");

    if (scriptFile == null) {
      return;
    }

    run(scriptFile, new ZeroDisplacement());
  }

  public static void run(File scriptFile, Displacement displacement) {
    try {
      Map<String, String>[] images = ScriptUtils.read(scriptFile, "image", "x", "y");

      int width = Display.getDisplayMode().getWidth();
      int height = Display.getDisplayMode().getHeight();

      Display.create();
      // Display.setDisplayMode(new DisplayMode(width, height));

      Display.setFullscreen(true);

      Display.setVSyncEnabled(true);

      // First "image" contains the header configuration
      configure(images[0]);

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
      Texture[] textures = new Texture[images.length];
      Point[] points = new Point[images.length];
      Region[] regions = new Region[images.length + 4]; // 4 is for walls

      for (int i = 0; i < images.length; i++) {
        InputStream in = new FileInputStream(new File(scriptFile.getParent(),
            images[i].get("image")));
        textures[i] = TextureLoader.getTexture("PNG", in);
        regions[i] = Region.createRegion(textures[i]);
        points[i] = new Point(Integer.parseInt(images[i].get("x")), Integer.parseInt(images[i]
            .get("y")));
      }

      setRegionPositions(regions, points);

      // Define the wall
      regions[images.length + 0] = Region.createBlock(width, 1, 0, -1);
      regions[images.length + 1] = Region.createBlock(1, height, width, 0);
      regions[images.length + 2] = Region.createBlock(width, 1, 0, height);
      regions[images.length + 3] = Region.createBlock(1, height, -1, 0);

      boolean quit = false;

      boolean wasMouseDown = false;
      while (!Display.isCloseRequested() && !quit) {
        Display.sync(60);

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
          quit = true;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
          setRegionPositions(regions, points);
        }

        if (Mouse.isButtonDown(0)) {

          if (wasMouseDown) {
            int x = Mouse.getX();
            int y = height - Mouse.getY();
            int dx = Mouse.getDX();
            int dy = Mouse.getDY();

            for (int i = 0; i < regions.length; i++) {
              if (regions[i].contains(x, y)) {
                regions[i].setDxDy(dx, -dy);

                displacement.apply(regions[i], dx, -dy, regions);

                i = regions.length;
              }
            }
          }

          wasMouseDown = true;
        } else {

          wasMouseDown = false;
        }

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        StringBuilder s = new StringBuilder("Regions ({}): ");
        // Move regions
        for (int i = 0; i < textures.length; i++) {
          int x = regions[i].getX();
          int y = regions[i].getY();

          s.append(x).append(",").append(y);

          if (i != (textures.length - 1)) {
            s.append(",");
          }

          renderImage(textures[i], x, y);
        }
        log.info(s.toString(), textures.length);

        Display.update();
      }
    } catch (ScriptException e) {
      UI.confirm("Script file contains errors:\n" + e.getMessage());
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      Display.destroy();
    }
  }

  private static void configure(final Map<String, String> config) {
    if (config.containsKey("background-color")) {
      float[] colors = ScriptUtils.parseColor(config.get("background-color"));
      glClearColor(colors[0], colors[1], colors[2], colors[3]);
    } else {
      glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    if (config.containsKey("hide-mouse")) {
      boolean hide = BooleanUtils.toBoolean(config.get("hide-mouse"));
      if (hide) {
        try {
          // Create transparent cursor
          Cursor transparentCursor = new Cursor(1, 1, 0, 0, 1, IntBuffer.allocate(1), null);
          Mouse.setNativeCursor(transparentCursor);
        } catch (LWJGLException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  /**
   * Renders a single image
   * 
   * @param texture
   * @param x
   * @param y
   */
  private static void renderImage(Texture texture, int x, int y) {
    Color.white.bind();
    // texture.bind(); // or GL11.glBind(texture.getTextureID());

    GL11.glBindTexture(GL_TEXTURE_2D, texture.getTextureID());

    // GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE,
    // GL11.GL_MODULATE );

    // when texture area is small, bilinear filter the closest mipmap

    // GL11.glTexParameterf( GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
    // GL11.GL_LINEAR_MIPMAP_NEAREST );
    // when texture area is large, bilinear filter the first mipmap
    // GL11.glTexParameterf( GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
    // GL11.GL_LINEAR );

    // GL11.glTexParameterf( GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
    // GL11.GL_CLAMP );
    // GL11.glTexParameterf( GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
    // GL11.GL_CLAMP );

    glBegin(GL11.GL_QUADS);

    GL11.glTexCoord2d(0.0d, 0.0d);
    GL11.glVertex2d(x, y);

    // It's 0.99 instead of 1.0 because 1.0 creates edge artifacts
    GL11.glTexCoord2d(0.99d, 0.0d);
    GL11.glVertex2d(x + texture.getTextureWidth(), y);

    GL11.glTexCoord2d(0.99d, 0.99d);
    GL11.glVertex2d(x + texture.getTextureWidth(), y + texture.getTextureHeight());

    GL11.glTexCoord2d(0.0d, 0.99d);
    GL11.glVertex2d(x, y + texture.getTextureHeight());

    glEnd();
  }

  private static void setRegionPositions(Region[] regions, Point[] points) {
    for (int i = 0; i < points.length; i++) {
      regions[i].setXY(points[i].x, points[i].y);
    }
  }
}