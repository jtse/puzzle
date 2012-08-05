/**
 * 
 */
package com.github.jtse.puzzle.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author jtse
 */
public class ScriptUtilsTest {
  @Test
  public void testRead() {
    @SuppressWarnings("unchecked")
    Map<String, String>[] maps = new Map[3];
    maps[0] = ScriptUtils.toMap("key1", "val1", "key2", "val2", "key3", "val3");

    maps[1] = ScriptUtils.toMap("key1", "val1b", "key2", "val2b", "key3", "val3b");

    maps[2] = ScriptUtils.toMap("key1", "val1c", "key2", "val2c", "key3", "val3c");

    String input = "key1=val1\n key2=val2\nkey3=val3\n" + "key1=val1b\n key2=val2b\nkey3=val3b\n"
        + "key1=val1c\n key2=val2c\nkey3=val3c\n";

    InputStream in = new ByteArrayInputStream(input.getBytes());

    Map<String, String>[] actual = ScriptUtils.read(in, "key1", "key2", "key3");

    for (int i = 0; i < actual.length; i++) {
      Assert.assertEquals(maps[i], actual[i]);
    }
  }

  @Test
  public void testMapHasKeysReturnsTrue() {
    Map<String, String> map = ScriptUtils.toMap("key1", "val1", "key2", "val2", "key3", "val3");

    Assert.assertEquals(true, ScriptUtils.mapHasKeys(map, "key1", "key2", "key3"));
  }

  @Test
  public void testMapHasKeysReturnsFalse() {
    Map<String, String> map = ScriptUtils.toMap("key1", "val1", "key3", "val3");

    Assert.assertEquals(false, ScriptUtils.mapHasKeys(map, "key1", "key2", "key3"));
  }

  @Test
  public void testMap() {
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("key1", "value1");
    expected.put("key2", "value2");
    expected.put("key3", "value3");

    Map<String, String> actual = ScriptUtils.toMap("key1", "value1", "key2", "value2", "key3",
        "value3");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testMapWithNull() {
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("key1", "value1");
    expected.put("key2", "value2");
    expected.put("key3", null);

    Map<String, String> actual = ScriptUtils.toMap("key1", "value1", "key2", "value2", "key3");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testParseColor() {
    float[] expected = { 1.0f, 0.5f, 0.9f, 1.0f };
    assertArrayEquals(expected, ScriptUtils.parseColor("1.0, 0.5 0.9"));
  }

  @Test
  public void testParseColorWith4Values() {
    float[] expected = { 1.0f, 0.5f, 0.9f, 0.5f };
    assertArrayEquals(expected, ScriptUtils.parseColor("1.0, 0.5 0.9 	0.5"));
  }

  public static final void assertArrayEquals(float[] expected, float[] actual) {
    Assert.assertEquals(expected.length, actual.length);

    for (int i = 0; i < expected.length; i++) {
      Assert.assertEquals(expected[i], actual[i], 0.000001);
    }
  }
}
