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

import java.io.File;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * @author jtse
 */
public class PuzzleModule extends AbstractModule {
  private final File basePath;

  public PuzzleModule(File basePath) {
    this.basePath = basePath;
  }

  /* (non-Javadoc)
   * @see com.google.inject.AbstractModule#configure()
   */
  @Override
  protected void configure() {
    bind(File.class).annotatedWith(Names.named("@base-path")).toInstance(basePath);
  }
}
