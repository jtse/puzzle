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

import static org.lwjgl.opengl.GL11.glClearColor;

import java.nio.IntBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import com.github.jtse.puzzle.util.Scripts;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

/**
 * @author jtse
 */
public class SceneModule extends AbstractModule {

  @Override
  protected void configure() {
  }

  @Provides @Named("@configure-scene")
  Runnable getConfigureScene(
      @Named("hide-mouse") final boolean hideMouse,
      @Named("background-color") final String backgroundColor
      ) {
    return new Runnable() {
        @Override public void run() {
          float[] colors = Scripts.parseColor(backgroundColor);
          glClearColor(colors[0], colors[1], colors[2], colors[3]);

          if (hideMouse) {
            try {
              Cursor transparentCursor = new Cursor(1, 1, 0, 0, 1, IntBuffer.allocate(1), null);
              Mouse.setNativeCursor(transparentCursor);
            } catch (LWJGLException e) {
              throw new RuntimeException(e);
            }
          }
        }};
  }
}
