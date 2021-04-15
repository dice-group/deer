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
/**
 *
 */
package org.aksw.deer.old_learning;

import org.aksw.deer.util.Tree;

import java.util.List;
import java.util.Set;

/**
 * @author sherif
 */
public class RefinementTree extends Tree<RefinementNodeOld> {

  public static final double CHILDREN_MULTIPLIER = 1;

  /**
   * @author sherif
   */
  public RefinementTree() {
    super();
  }

  /**
   * @author sherif
   */
  public RefinementTree(RefinementNodeOld value) {
    super(value);
  }

  /**
   * @author sherif
   */
  public RefinementTree(Tree<RefinementNodeOld> parent, RefinementNodeOld value,
    List<Tree<RefinementNodeOld>> childrenlist) {
    super(parent, value, childrenlist);
  }

  @SuppressWarnings("unused")
  private void setFitness(Tree<RefinementNodeOld> root, double fitness) {
    long rootChildrenCount = root.size() - 1;
    root.getValue().fitness += fitness + CHILDREN_MULTIPLIER * rootChildrenCount;
    root = root.getParent();
    while (root != null) {
      root.getValue().fitness += CHILDREN_MULTIPLIER * rootChildrenCount;
      root = root.getParent();
    }

  }

  /* (non-Javadoc)
   * @see org.aksw.geolift.execution.specslearner.Tree#getLeaves()
   */
  @Override
  public Set<Tree<RefinementNodeOld>> getLeaves() {
    return super.getLeaves();
  }

  /* (non-Javadoc)
   * @see org.aksw.geolift.execution.specslearner.Tree#addChild(org.aksw.geolift.execution.specslearner.Tree)
   */
  @Override
  public void addChild(Tree<RefinementNodeOld> child) {
    super.addChild(child);
  }

  /* (non-Javadoc)
   * @see org.aksw.geolift.execution.specslearner.Tree#removeChild(org.aksw.geolift.execution.specslearner.Tree)
   */
  @Override
  public void removeChild(Tree<RefinementNodeOld> child) {
    super.removeChild(child);
  }

  /* (non-Javadoc)
   * @see org.aksw.geolift.execution.specslearner.Tree#getParent()
   */
  @Override
  public Tree<RefinementNodeOld> getParent() {
    return super.getParent();
  }

  /* (non-Javadoc)
   * @see org.aksw.geolift.execution.specslearner.Tree#getchildren()
   */
  @Override
  public List<Tree<RefinementNodeOld>> getchildren() {
    return super.getchildren();
  }

  /* (non-Javadoc)
   * @see org.aksw.geolift.execution.specslearner.Tree#getValue()
   */
  @Override
  public RefinementNodeOld getValue() {
    return super.getValue();
  }

//	/* (non-Javadoc)
//	 * @see org.aksw.geolift.execution.specslearner.Tree#print(org.aksw.geolift.execution.specslearner.Tree)
//	 */
//	@Override
//	public void print(Tree<RefinementNode> root) {
//		super.print(root);
//	}

  /* (non-Javadoc)
   * @see org.aksw.geolift.execution.specslearner.Tree#size()
   */
  @Override
  public long size() {
    return super.size();
  }

}
