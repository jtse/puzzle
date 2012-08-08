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

/**
 * @author jtse
 */
public final class MouseEvent {
  private final int x;
  private final int y;
  private final boolean isButtonDown; // first button

  public MouseEvent(int x, int y, boolean isMouseDown) {
    this.x = x;
    this.y = y;
    this.isButtonDown = isMouseDown;
  }

  public final int getX() {
    return x;
  }

  public final int getY() {
    return y;
  }

  public final boolean isButtonDown() {
    return isButtonDown;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (isButtonDown ? 1231 : 1237);
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MouseEvent other = (MouseEvent) obj;
    if (isButtonDown != other.isButtonDown)
      return false;
    if (x != other.x)
      return false;
    if (y != other.y)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "MouseEvent [x=" + x + ", y=" + y + ", isMouseDown=" + isButtonDown + "]";
  }
}
