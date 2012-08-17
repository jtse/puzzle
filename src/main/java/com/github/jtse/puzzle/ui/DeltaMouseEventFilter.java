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

import com.google.common.base.Function;

/**
 * @author jtse
 */
public class DeltaMouseEventFilter implements Function<MouseEvent, MouseEvent> {
  public static final MouseEvent NO_CHANGE = new MouseEvent(0, 0, false);
  private MouseEvent previous;

  @Override
  public MouseEvent apply(MouseEvent current) {
    MouseEvent change = previous == null
        ? NO_CHANGE
        : new MouseEvent(
            current.getX() - previous.getX(),
            current.getY() - previous.getY(),
            current.isButtonDown() != previous.isButtonDown());

    previous = current;
    return change;
  }
}
