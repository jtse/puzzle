/**
 * 
 */
package com.github.jtse.puzzle.ogl;

import java.io.IOException;
import java.io.InputStream;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 * @author jtse
 */
public class RegionTest {
  private static Texture texture;
  private Region region;

  @BeforeClass
  public static void setupClass() throws IOException, LWJGLException {
    Display.create();
    /**
     * This is a 32x16 RGB image with alpha where 0,0 and 31,0 and 0,15 have
     * alpha values
     */
    InputStream in = RegionTest.class.getResourceAsStream("/32x16-rgb.png");
    texture = TextureLoader.getTexture("PNG", in);
  }

  @Before
  public void setUp() {
    this.region = Region.createRegion(texture);
  }

  @Test
  public void testContainsReturnsTrueWhenPointInRegion() {
    Assert.assertTrue(region.contains(0, 0));
    Assert.assertTrue(region.contains(31, 0));
    Assert.assertTrue(region.contains(0, 15));
  }

  @Test
  public void testContainsReturnsFalseWhenPointNotInRegionAfterMove() {
    region.setXY(1, 1);
    Assert.assertFalse(region.contains(0, 0));
    Assert.assertFalse(region.contains(31, 0));
    Assert.assertFalse(region.contains(0, 15));
  }

  @Test
  public void testContainsReturnsFalseWhenPointNotInRegion() {
    region.setXY(0, 1);
    Assert.assertTrue(region.contains(0, 1));
    Assert.assertTrue(region.contains(31, 1));

    region.setXY(1, 0);
    Assert.assertTrue(region.contains(1, 15));
    Assert.assertTrue(region.contains(1, 0));
  }

  @AfterClass
  public static void tearDownClass() {
    texture.release();
    Display.destroy();
  }
}
