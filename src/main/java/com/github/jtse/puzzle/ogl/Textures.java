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
package com.github.jtse.puzzle.ogl;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 * @author jtse
 */
public class Textures {

  public static void renderImage(Texture texture, int x, int y) {
    // Color.white.bind();
    // texture.bind(); // or GL11.glBind(texture.getTextureID());
    //GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
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

  public static Texture createImage(InputStream in) throws IOException {
    return TextureLoader.getTexture("PNG", in);
  }

  public static Texture createImage(File imageFile) throws IOException {
    InputStream in = new FileInputStream(imageFile);
    try {
      return createImage(in);
    } finally {
      in.close();
    }
  }
}
