/**
 * 
 */
package com.github.jtse.puzzle.physics;

import com.github.jtse.puzzle.ogl.Region;

/**
 * Collision algorithm where moving region cannot displace other regions
 * 
 * @author jtse
 * 
 */
class NoneDisplacement implements Displacement {

  public boolean apply(Region source, int dx, int dy, Region[] targets) {
    boolean collided = false;
    for (int i = 0; i < targets.length; i++) {
      if (targets[i] != source) {
        if (targets[i].intersects(source)) {
          source.setDxDy(-dx, -dy);
          collided = true;
        }
      }
    }
    return collided;
  }

}
