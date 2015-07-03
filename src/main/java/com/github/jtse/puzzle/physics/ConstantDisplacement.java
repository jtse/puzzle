/**
 * 
 */
package com.github.jtse.puzzle.physics;

import com.github.jtse.puzzle.ogl.Region;

/**
 * Collision algorithm where moving region displaces other regions by same
 * change in x and y
 */
class ConstantDisplacement implements Displacement {
  public boolean apply(Region source, int dx, int dy, Region[] targets) {
    boolean collided = false;
    for (int i = 0; i < targets.length; i++) {
      if (targets[i] != source) {
        if (targets[i].intersects(source)) {
          collided = true;
          targets[i].setDxDy(dx, dy);
          apply(targets[i], dx, dy, targets);
        }
      }
    }
    return collided;
  }
}
