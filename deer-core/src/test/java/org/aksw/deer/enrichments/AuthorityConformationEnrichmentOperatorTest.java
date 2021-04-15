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
package org.aksw.deer.enrichments;

import org.aksw.faraday_cage.engine.ValidatableParameterMap;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class AuthorityConformationEnrichmentOperatorTest {

  private AuthorityConformationEnrichmentOperator op;
  private Model input, expected;
  private ValidatableParameterMap expectedParameters;

  @Before
  public void setUp() {
    input = ModelFactory.createDefaultModel();
    expected = ModelFactory.createDefaultModel();
    input.read(new StringReader(
      "@prefix ex: <http://example.org/> ." +
        "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> ." +
        "ex:subject rdfs:comment \"Goethe lived in Leipzig. Amazon has a parcel center in Leipzig.\" ." +
        "ex:subject2 rdfs:comment \"Goethe lived in Leipzig. Amazon has a parcel center in Leipzig.\" ." +
        "ex:subject3 rdfs:comment \"Goethe lived in Leipzig. Amazon has a parcel center in Leipzig.\" ." +
        "ex:subject4 rdfs:label \"Goethe lived in Leipzig. Amazon has a parcel center in Leipzig.\" ." +
        "ex:subject5 rdfs:label \"Goethe lived in Leipzig. Amazon has a parcel center in Leipzig.\" ."
    ), null, "TTL");
    expected.read(new StringReader(
      "@prefix ex2: <http://example.org/2/> ." +
        "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> ." +
        "ex2:subject rdfs:comment \"Goethe lived in Leipzig. Amazon has a parcel center in Leipzig.\" ." +
        "ex2:subject2 rdfs:comment \"Goethe lived in Leipzig. Amazon has a parcel center in Leipzig.\" ." +
        "ex2:subject3 rdfs:comment \"Goethe lived in Leipzig. Amazon has a parcel center in Leipzig.\" ." +
        "ex2:subject4 rdfs:label \"Goethe lived in Leipzig. Amazon has a parcel center in Leipzig.\" ." +
        "ex2:subject5 rdfs:label \"Goethe lived in Leipzig. Amazon has a parcel center in Leipzig.\" ."
    ), null, "TTL");
    op = new AuthorityConformationEnrichmentOperator();
    expectedParameters = op.createParameterMap();
    expectedParameters.add(AuthorityConformationEnrichmentOperator.OPERATION, expectedParameters.createResource()
      .addProperty(AuthorityConformationEnrichmentOperator.SOURCE_AUTHORITY, input.createResource(input.expandPrefix("ex:")))
      .addProperty(AuthorityConformationEnrichmentOperator.TARGET_AUTHORITY, expected.createResource(expected.expandPrefix("ex2:")))
    ).init();
    op.initDegrees(1,1);
    op.initPluginId(ResourceFactory.createResource("urn:ex/test/ac-test"));
  }

  @Test
  public void safeApply() {
    op.initParameters(expectedParameters);
    Model actual = op.apply(List.of(input)).get(0);
    assertTrue("It should conform ex: to ex2: authority.", expected.isIsomorphicWith(actual));
  }

  @Test
  public void predictApplicability() {
    double applicability = op.predictApplicability(List.of(input), expected);
    assertEquals("It should detect perfect applicability.", 1d, applicability, 0.01d);
  }

  @Test
  public void learnParameterMap() {
    ValidatableParameterMap actualLearned = op.learnParameterMap(List.of(input), expected, null);
    Resource id = op.getId();
    assertTrue("It should learn the appropriate configuration", actualLearned.parametrize(id).isIsomorphicWith(expectedParameters.parametrize(id)));
  }

  @Test
  public void reverseApply() {
    Model actual = op.reverseApply(List.of(input), expected).get(0);
    assertTrue("It should revert the operation.", input.isIsomorphicWith(actual));
  }

}