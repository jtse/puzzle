/**
 *
 */
package com.github.jtse.puzzle.physics;

import com.github.jtse.puzzle.ogl.Region;

/**
 * Collision algorithm where moving region cannot displace other regions
 */
class DisappearDisplacement implements Displacement {

  @Override
  public boolean apply(Region region, int dx, int dy, Region[] targets) {
    region.setHidden(true);
    return false;
  }
}
