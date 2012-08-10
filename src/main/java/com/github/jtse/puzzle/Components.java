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

import java.util.List;
import java.util.Map;

import com.github.jtse.puzzle.ui.MouseEvent;
import com.github.jtse.puzzle.ui.MouseMedianFilter;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * @author jtse
 */
public class Components {
  private final Map<String, String> config;
  private final List<Map<String, String>> trials;

  public Components(Map<String, String> config, List<Map<String, String>> trials) {
    this.config = ImmutableMap.copyOf(config);
    this.trials = ImmutableList.copyOf(trials);
  }

  public Function<MouseEvent, MouseEvent> createMouseFilter() {
    return new MouseMedianFilter(5);
  }
}
