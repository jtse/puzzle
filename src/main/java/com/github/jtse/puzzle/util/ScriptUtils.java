/**
 *
 */
package com.github.jtse.puzzle.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CharMatcher;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;


/**
 * Utilities for parsing script file
 *
 * @author jtse
 */
public class ScriptUtils {
  public static class ScriptException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ScriptException(String message) {
      super(message);
    }
  }

  /**
   * Reads a script file and creates an array of key-value map
   *
   * @param file the file to load
   * @param repeatableKeys the repeatable keys
   * @return list of maps where the first map contains the non-repeatable keys and
   * subsequent maps are repeatable keys
   */
  public static final List<Map<String, String>> read(File file, Set<String> repeatableKeys) {
    FileInputStream in = null;
    try {
      in = new FileInputStream(file);
      return read(in, ImmutableSet.copyOf(repeatableKeys));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (IOException e) {
      }
    }
  }

  @VisibleForTesting
  static final List<Map<String, String>> read(InputStream in, Set<String> repeatableKeys) {
    int n = 1;

    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    String line = "";

    ArrayListMultimap<String, String> tokens = ArrayListMultimap.create();
    try {
      while (line != null) {
        line = reader.readLine();
        if ( line != null && !CharMatcher.INVISIBLE.matchesAllOf(line) && !line.startsWith("#")) {
          String[] pair = line.split("=");

          if (pair.length != 2) {
            if (n > 1) {
              throw new ScriptException("Missing '=' on line #" + n);
            } else {
              throw new ScriptException(
                  "Missing '=' on line the first line. Are you sure this is a plain-text file?");
            }
          }

          String key = pair[0].trim();
          String value = pair[1].trim();

          tokens.put(key, value);
        }
        n++;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    tokens.trimToSize();

    Map<String, String> nonRepeatable = new HashMap<String, String>();
    for(String key : tokens.keySet()) {
      if ( !repeatableKeys.contains(key)) {
        List<String> values = tokens.get(key);
        if (values.size() == 1) {
          nonRepeatable.put(key, values.get(0));
        } else {
          throw new ScriptException("Multiple entries for '" + key + "' not allowed.");
        }
      }
    }

    Iterator<String> iterator = repeatableKeys.iterator();
    String firstKey = iterator.next();
    int firstSize = tokens.get(firstKey).size();

    while (iterator.hasNext()) {
      String key = iterator.next();
      if (tokens.get(key).size() != firstSize) {
        throw new ScriptException(
            "'" + firstKey + "' and '" + key + "' must have the same number of entries");
      }
    }

    ArrayList<Map<String, String>> repeatables = Lists.newArrayListWithCapacity(firstSize);
    for(String key : repeatableKeys) {
      int i = 0;
      for(String value : tokens.get(key)) {
        if (repeatables.size() == i) {
          repeatables.add(new HashMap<String, String>());
        }
        Map<String, String> map = repeatables.get(i);
        map.put(key, value);
        i++;
      }
    }


    return ImmutableList.<Map<String, String>>builder()
        .add(nonRepeatable)
        .addAll(repeatables)
        .build();
  }

  /**
   * Parses color string into RGBA floating point values
   *
   * @param color string
   * @return array of 4 floats representing RGBA
   */
  public static final float[] parseColor(String color) {
    String[] values = color.split("[^0-9\\.]+");

    float[] f = new float[4];

    for (int i = 0; i < values.length; i++) {
      f[i] = Float.parseFloat(values[i]);
    }

    if (values.length < 4) {
      f[3] = 1.0f;
    }

    return f;
  }
}
