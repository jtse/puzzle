/**
 * 
 */
package com.github.jtse.puzzle.physics;

import com.github.jtse.puzzle.ogl.Region;

/**
 * Interface for applying displacement as a result of a collisions
 * 
 */
public interface Displacement {
  /**
   * Applies displacement
   * 
   * @param source
   *          the region that just moved
   * @param dx
   * @param dy
   * @param targets
   *          the array of regions to check for collision and apply displacement
   * @return true if collision detected
   */
  boolean apply(Region source, int dx, int dy, Region[] targets);
}
