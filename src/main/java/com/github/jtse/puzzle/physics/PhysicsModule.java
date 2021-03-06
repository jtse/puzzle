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
package com.github.jtse.puzzle.physics;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.ProvisionException;
import com.google.inject.name.Named;

/**
 * @author jtse
 */
public class PhysicsModule extends AbstractModule {
  @Override
  protected void configure() {
    // no-op
  }

  @Provides
  Displacement newDisplacement(@Named("displacement") String displacement) {
    String className = getClass().getPackage().getName() + "."
        + displacement.substring(0, 1).toUpperCase()
        + displacement.substring(1).toLowerCase() + "Displacement";
    try {
      Class<?> clazz = Class.forName(className);
      return (Displacement) clazz.newInstance();
    } catch (ClassNotFoundException e) {
      throw new ProvisionException("No corresponding displacement class for " + displacement, e);
    } catch (InstantiationException e) {
      throw new ProvisionException("Unable to instantiate " + className, e);
    } catch (IllegalAccessException e) {
      throw new ProvisionException("Unable to access " + className, e);
    }
  }
}
