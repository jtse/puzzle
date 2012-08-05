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
public class ZeroDisplacement implements Displacement {

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.binghamton.baby.puzzle.physics.Collision#apply(edu.binghamton.baby.
   * puzzle.ogl.Region, int, int, edu.binghamton.baby.puzzle.ogl.Region[])
   */
  @Override
  public void apply(Region source, int dx, int dy, Region[] targets) {
    for (int i = 0; i < targets.length; i++) {
      if (targets[i] != source) {
        if (targets[i].intersects(source)) {
          source.setDxDy(-dx, -dy);
        }
      }
    }
  }

}
