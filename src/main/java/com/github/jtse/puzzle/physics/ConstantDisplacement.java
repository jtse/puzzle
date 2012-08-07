/**
 * 
 */
package com.github.jtse.puzzle.physics;

import com.github.jtse.puzzle.ogl.Region;

/**
 * Collision algorithm where moving region displaces other regions by same
 * change in x and y
 * 
 * @author jtse
 * 
 */
public class ConstantDisplacement implements Displacement {

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.binghamton.baby.puzzle.physics.Collision#apply(edu.binghamton.baby.
   * puzzle.ogl.Region, int, int, edu.binghamton.baby.puzzle.ogl.Region[])
   */
  public void apply(Region source, int dx, int dy, Region[] targets) {
    for (int i = 0; i < targets.length; i++) {
      if (targets[i] != source) {
        if (targets[i].intersects(source)) {
          targets[i].setDxDy(dx, dy);
          apply(targets[i], dx, dy, targets);
        }
      }
    }
  }

}
