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

import java.util.LinkedList;
import java.util.Queue;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;

/**
 * @author jtse
 */
public class MedianMouseEventFilter implements Function<MouseEvent, MouseEvent> {
  private final Queue<Integer> xs;
  private final Queue<Integer> ys;
  private final Queue<Boolean> booleans;
  private final int capacity;
  private final int middle;
  private int size;

  public MedianMouseEventFilter(int capacity) {
    Preconditions.checkArgument(capacity > 0, "Capacity must be geater than zero.");
    Preconditions.checkArgument(capacity % 2 == 1, "Capacity must be an odd number.");
    this.xs = new LinkedList<Integer>();
    this.ys = new LinkedList<Integer>();
    this.booleans = new LinkedList<Boolean>();
    this.capacity = capacity;
    this.size = 0;
    this.middle = capacity / 2;
  }

  public MouseEvent apply(MouseEvent event) {
    this.add(event);
    size++;
    if (size >= capacity) {
      event = median();
      poll();
    }

    return event;
  }

  void add(MouseEvent event) {
    xs.add(event.getX());
    ys.add(event.getY());
    booleans.add(event.isButtonDown());
  }

  void poll() {
    xs.poll();
    ys.poll();
    booleans.poll();
  }

  MouseEvent median() {
    return new MouseEvent(Ordering.natural().sortedCopy(xs).get(middle), Ordering.natural()
        .sortedCopy(ys).get(middle), Ordering.natural().sortedCopy(booleans).get(middle));
  }
}
