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


import org.aksw.deer.ParameterizedDeerExecutionNode;
import org.aksw.deer.vocabulary.DEER;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 * @author sherif
 */
public class RefinementNodeOld implements Comparable<RefinementNodeOld> {

  public ParameterizedDeerExecutionNode module = null;
  public double fitness = -Double.MAX_VALUE;
  public Model inputModel = ModelFactory.createDefaultModel();
  public Model outputModel = ModelFactory.createDefaultModel();
  public Model configModel = ModelFactory.createDefaultModel();
  public Resource inputDataset = ResourceFactory.createResource();
  public Resource outputDataset = ResourceFactory.createResource();
  public NodeStatus status;

  /**
   * @author sherif
   */
  public RefinementNodeOld() {
    super();
    configModel.setNsPrefix("gl", DEER.NS);
  }

  public RefinementNodeOld(double fitness) {
    this();
    this.fitness = fitness;
  }

  /**
   * @author sherif
   */
  public RefinementNodeOld(ParameterizedDeerExecutionNode module, double fitness, Model inputModel, Model outputModel,
                           Resource inputDataset, Resource outputDataset, Model configModel) {
    super();
    this.module = module;
    this.fitness = fitness;
    if (fitness == -2) {
      status = NodeStatus.DEAD;
    }
    this.inputModel = inputModel;
    this.outputModel = outputModel;
    this.configModel = configModel;
    this.inputDataset = inputDataset;
    this.outputDataset = outputDataset;
    if (configModel != null) {
      configModel.setNsPrefix("gl", DEER.NS);
    }
  }


  public RefinementNodeOld(ParameterizedDeerExecutionNode operator, Model inputModel, Model outputModel,
                           Resource inputDataset, Resource outputDataset, Model configModel) {
    super();
    if (fitness == -2) {
      status = NodeStatus.DEAD;
    }
    this.inputModel = inputModel;
    this.outputModel = outputModel;
    this.configModel = configModel;
    this.inputDataset = inputDataset;
    this.outputDataset = outputDataset;
    if (configModel != null) {
      configModel.setNsPrefix("gl", DEER.NS);
    }
  }

  /**
   * @author sherif
   */
  public static void main(String[] args) {

  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return module.getClass().getSimpleName() + "(" + fitness + ")";
//				"\n fitness=" + fitness +
//				"\n outputModel(" + output.size() + ")=" +
//				outputModel.write(System.out,"TTL") +
//				"\n configModel(" + config.size() + ")=";
//+
//				configModel.write(System.out,"TTL") +
//				",\n childNr=" + childNr + "]";
  }

  /* (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(RefinementNodeOld o) {
    return (int) (fitness - o.fitness);
//		if(fitness > o.fitness){
//			return 1;
//		} else if(fitness < o.fitness){
//			return -1;
//		}else 
//			return 0;
  }
}

//class ExecutionNodeComp implements Comparator<ExecutionNode>{
//	/* (non-Javadoc)
//	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
//	 */
//	@Override
//	public int compare(ExecutionNode e1, ExecutionNode e2) {
//		if(e1.fitness > e2.fitness){
//			return 1;
//		} else if(e1.fitness < e2.fitness){
//			return -1;
//		}else 
//			return 0;
//	}
//}