/*
 * DEER Core Library - DEER - RDF Dataset Enrichment Framework
 * Copyright © 2013 Data Science Group (DICE) (kevin.dressler@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.deer.learning;

import io.jenetics.prngine.MT19937_64Random;
import io.jenetics.prngine.Random64;

/**
 * Random utility class
 */
public class RandomUtil {

  private static Random64 rng = new MT19937_64Random.ThreadSafe();
  private static final Object lock = new Object();

  public static int get(int min, int max) {
    return rng.nextInt(min, max);
  }

  public static int get(int max) {
    return rng.nextInt(max);
  }

  public static double get() {
    return rng.nextDouble();
  }

  // not thread safe, do only use in serial computations, i.e. when there are no other threads accessing
  // RandomUtil for the time of the execution of temporaryWithSeed
  public static void temporaryWithSeed(long seed, Runnable function) {
    Random64 tmp = rng;
    rng = new MT19937_64Random.ThreadSafe(seed);
    function.run();
    rng = tmp;
  }

}