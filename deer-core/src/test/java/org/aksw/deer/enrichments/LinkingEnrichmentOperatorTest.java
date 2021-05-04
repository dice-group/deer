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
public class LinkingEnrichmentOperatorTest {

  private Model source, target, expected;

  @Before
  public void setUp() {
    source = ModelFactory.createDefaultModel();
    target = ModelFactory.createDefaultModel();
    expected = ModelFactory.createDefaultModel();
    source.read(new StringReader(
      "@prefix ex: <http://example.org/> ." +
        "ex:subject1 ex:name \"Barack Obama\" ." +
        "ex:subject1 ex:age \"57\" ." +
        "ex:subject2 ex:name \"Donald Trump\" ." +
        "ex:subject2 ex:age \"73\" ."
    ), null, "TTL");
    target.read(new StringReader(
      "@prefix ex: <http://example.org/> ." +
        "ex:subject3 ex:name \"Berack Obama\" ." +
        "ex:subject3 ex:age \"56\" ." +
        "ex:subject4 ex:name \"Donald Drump\" ." +
        "ex:subject4 ex:age \"73\" ."
    ), null, "TTL");

    expected.add(source);
    expected.add(target);
    expected.read(new StringReader(
      "@prefix ex: <http://example.org/> ." +
        "@prefix owl: <http://www.w3.org/2002/07/owl#>." +
        "ex:subject1 owl:sameAs ex:subject3 ." +
        "ex:subject2 owl:sameAs ex:subject4 ."
    ), null, "TTL");
  }

  @Test
  public void safeApply() {
    LinkingEnrichmentOperator op = new LinkingEnrichmentOperator();
    op.initDegrees(2,1);
    op.initPluginId(ResourceFactory.createResource("urn:ex/test/linking-test"));
    op.initParameters(
      op.createParameterMap()
        .add(LinkingEnrichmentOperator.USE_ML, source.createTypedLiteral(true))
        .add(LinkingEnrichmentOperator.LINKING_PREDICATE, expected.createResource(expected.expandPrefix("owl:sameAs")))
        .init()
    );
    Model actual = op.apply(List.of(source, target)).get(0);
    assertTrue("It should detect all types.", expected.isIsomorphicWith(actual));
  }


  @Test
  public void predictApplicability() {
    LinkingEnrichmentOperator op = new LinkingEnrichmentOperator();
    double applicability = op.predictApplicability(List.of(source, target), expected);
    assertEquals("It should detect perfect applicability.", 1d, applicability, 0.01d);
  }

  @Test
  public void learn() {
    LinkingEnrichmentOperator op = new LinkingEnrichmentOperator();
    ValidatableParameterMap expectedLearned = op.createParameterMap()
      .add(LinkingEnrichmentOperator.USE_ML, source.createTypedLiteral(true))
      .add(LinkingEnrichmentOperator.LINKING_PREDICATE, expected.createResource(expected.expandPrefix("owl:sameAs")))
      .init();
    ValidatableParameterMap actualLearned = op.learnParameterMap(List.of(source, target), expected, null);
    Resource id = ResourceFactory.createResource("urn:ex/test/linking-test");
    assertTrue("It should learnParameterMap the standard configuration in all cases", actualLearned.parametrize(id).isIsomorphicWith(expectedLearned.parametrize(id)));
  }

  @Test
  public void reverseApply() {
    LinkingEnrichmentOperator op = new LinkingEnrichmentOperator();
    List<Model> reconstructed = op.reverseApply(List.of(source, target), expected);
    assertTrue("The reconstructed source should equal the actual source", reconstructed.get(0).isIsomorphicWith(source));
    assertTrue("The reconstructed target should equal the actual target", reconstructed.get(1).isIsomorphicWith(target));
  }
}