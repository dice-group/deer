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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.aksw.deer.vocabulary.FOXO;
import org.aksw.faraday_cage.engine.ValidatableParameterMap;
import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDFS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
*
*/
public class NEREnrichmentOperatorTest implements HttpHandler {

  private HttpServer foxMockService;
  private NEREnrichmentOperator op;
  private Model input, expected;
  private ValidatableParameterMap expectedParameters;

  @Before
  public void setUp() throws Exception {
    input = ModelFactory.createDefaultModel();
    expected = ModelFactory.createDefaultModel();
    input.read(this.getClass().getClassLoader().getResourceAsStream("fox/input.ttl"), null, "TTL");
    expected.add(input);
    expected.read(this.getClass().getClassLoader().getResourceAsStream("fox/expected.ttl"), null, "TTL");
    op = new NEREnrichmentOperator();
    expectedParameters = op.createParameterMap()
      .add(NEREnrichmentOperator.LITERAL_PROPERTY, RDFS.comment)
      .add(NEREnrichmentOperator.IMPORT_PROPERTY, FOXO.RELATED_TO)
      .add(NEREnrichmentOperator.NE_TYPE, expected.createLiteral("all"))
      .add(NEREnrichmentOperator.FOX_URL, expected.createResource("http://localhost:4444/fox"))
      .add(NEREnrichmentOperator.PARALLELISM, expected.createTypedLiteral(1))
      .init();
    op.initDegrees(1,1);
    op.initPluginId(ResourceFactory.createResource("urn:ex/test/ner-test"));

    foxMockService = HttpServer.create(new InetSocketAddress(4444), 0);
    foxMockService.createContext("/fox", this);
    foxMockService.start();
  }

  @After
  public void tearDown() {
    if (foxMockService != null) {
      foxMockService.stop(0);
      foxMockService = null;
    }
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    byte[] response = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("fox/response.ttl"), StandardCharsets.UTF_8).getBytes();
    exchange.sendResponseHeaders(200, response.length);
    exchange.getResponseBody().write(response);
  }

  @Test
  public void safeApply() {
    op.initParameters(expectedParameters);
    Model actual = op.apply(List.of(input)).get(0);
    assertTrue("It should detect all types.", expected.isIsomorphicWith(actual));
  }

  @Test
  public void predictApplicability() {
    double applicability = op.predictApplicability(List.of(input), expected);
    assertEquals("It should detect perfect applicability.", 1d, applicability, 0.01d);
  }

  @Test
  public void learn() {
    ValidatableParameterMap actualLearned = op.learnParameterMap(List.of(input), expected, null);
    Resource id = op.getId();
    assertIsomorphism("It should learnParameterMap the standard configuration in all cases", expectedParameters.parametrize(id), actualLearned.parametrize(id));
  }

  private void assertIsomorphism(String message, Model expected, Model actual) {
    StringWriter writer = new StringWriter();
    writer.write(message);
    writer.write("\n");
    writer.write("Expected:\n");
    expected.write(writer, "TTL");
    writer.write("\n");
    writer.write("Actual:\n");
    actual.write(writer, "TTL");
    assertTrue(writer.toString(), actual.isIsomorphicWith(expected));
    //assertTrue(message + "\nExpected: " + expected.write + "\nActual: " + actual, actual.isIsomorphicWith(expected));
  }

}