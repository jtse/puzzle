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
package com.github.jtse.puzzle.ui;

import org.lwjgl.input.Mouse;

import com.google.common.base.Function;

/**
 * Mouse Poller
 * 
 * @author jtse
 */
public class MousePoller {
  private final Function<MouseEvent, MouseEvent> filter;

  public MousePoller(Function<MouseEvent, MouseEvent> filter) {
    this.filter = filter;
  }

  public MouseEvent poll() {
    return filter.apply(new MouseEvent(Mouse.getX(), Mouse.getY(), Mouse.isButtonDown(0)));
  }
}
