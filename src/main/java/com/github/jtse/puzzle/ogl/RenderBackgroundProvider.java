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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;

import com.github.jtse.puzzle.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * @author jtse
 */
public class RenderBackgroundProvider implements Provider<Runnable> {
  @Inject @Named("@base-path")
  private File basePath;

  @Inject @Named("background-image")
  private String imageFile;

  @Override
  public Runnable get() {
    return imageFile.isEmpty()
        ? new Runnable() {
            @Override
            public void run() {
            }}
        : new Runnable() {
            private final Texture texture;
            {
              try {
                texture = Textures.createImage(new File(basePath, imageFile));
              } catch (FileNotFoundException e) {
                throw new ConfigurationException("Image not file found: " + imageFile, e);
              } catch (IOException e) {
                throw new ConfigurationException("Unable to load image file: " + imageFile, e);
              }
            }
            @Override
            public void run() {
              Textures.renderImage(texture, 0, 0);
            }};
  }
}
