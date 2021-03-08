package org.aksw.deer;

import junit.framework.TestCase;
import org.apache.jena.rdf.model.Model;

/**
 *
 */
public class DeerControllerTest extends TestCase {

public void testGetShapes() {
    Model shapes = DeerController.getShapes();
    System.out.println(shapes);
  }
}