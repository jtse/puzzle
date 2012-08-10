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

import org.junit.Assert;
import org.junit.Test;

/**
 * @author jtse
 */
public class MouseMedianFilterTest {
  @Test
  public void apply() {
    MouseMedianFilter filter = new MouseMedianFilter(5);

    Assert.assertEquals(new MouseEvent(1, 1, false), filter.apply(new MouseEvent(1, 1, false)));
    Assert.assertEquals(new MouseEvent(2, 2, true), filter.apply(new MouseEvent(2, 2, true)));
    Assert.assertEquals(new MouseEvent(3, 4, false), filter.apply(new MouseEvent(3, 4, false)));
    Assert.assertEquals(new MouseEvent(4, 3, true), filter.apply(new MouseEvent(4, 3, true)));
    Assert.assertEquals(new MouseEvent(3, 3, false), filter.apply(new MouseEvent(5, 5, false)));
    Assert.assertEquals(new MouseEvent(4, 4, true), filter.apply(new MouseEvent(5, 5, true)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorThrowsExceptionOnNegatives() {
    new MouseMedianFilter(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorThrowsExceptionOnZero() {
    new MouseMedianFilter(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorThrowsExceptionOnFour() {
    new MouseMedianFilter(4);
  }

  public void constructor() {
    new MouseMedianFilter(5);
  }
}
