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

import com.google.common.collect.Lists;
import org.aksw.deer.learning.ReverseLearnable;
import org.aksw.deer.learning.SelfConfigurable;
import org.aksw.deer.vocabulary.DEER;
import org.aksw.deer.vocabulary.FOXO;
import org.aksw.faraday_cage.engine.ThreadlocalInheritingCompletableFuture;
import org.aksw.faraday_cage.engine.ValidatableParameterMap;
import org.aksw.fox.binding.FoxApi;
import org.aksw.fox.binding.FoxParameter;
import org.aksw.fox.binding.IFoxApi;
import org.aksw.fox.data.Entity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.jena.rdf.model.*;
import org.apache.jena.shared.Lock;
import org.apache.jena.vocabulary.RDFS;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 *
 */
@Extension
public class NEREnrichmentOperator extends AbstractParameterizedEnrichmentOperator implements SelfConfigurable, ReverseLearnable {

  public static final Property LITERAL_PROPERTY = DEER.property("literalProperty");

  public static final Property IMPORT_PROPERTY = DEER.property("importProperty");

  public static final Property FOX_URL = DEER.property("foxUrl");

  public static final Property NE_TYPE = DEER.property("neType");

  public static final Property FOX_LANG = DEER.property("foxLang");

  public static final Property PARALLELISM = DEER.property("parallelism");

//  private static final String DEFAULT_FOX_URL = "http://localhost:4444/fox";
  private static final String DEFAULT_FOX_URL = "https://fox.demos.dice-research.org/fox";


  private static final Property DEFAULT_IMPORT_PROPERTY
    = ResourceFactory.createProperty("http://geoknow.org/ontology/relatedTo");

  private static final int DEFAULT_PARALLELISM = 1;

  private static final Logger logger = LoggerFactory.getLogger(NEREnrichmentOperator.class);

  private static final ConcurrentMap<NEROperationID, CompletableFuture<List<String>>> cache = new ConcurrentHashMap<>();

  /**
   * Defines the possible (sub)types of named entities to be discovered
   */
  private enum NET {
    ORGANIZATION, LOCATION, PERSON, ALL
  }

  private Property literalProperty;
  private Property importProperty;
  private URL foxUri;
  private int parallelism;
  private NET neType;
  private FoxParameter.LANG foxLang;


  @Override
  public String getDescription() {
    return "Apply NER to targeted string literals using FOX";
  }

  @Override
  public ValidatableParameterMap createParameterMap() {
    return ValidatableParameterMap.builder()
      .declareProperty(LITERAL_PROPERTY)
      .declareProperty(IMPORT_PROPERTY)
      .declareProperty(FOX_URL)
      .declareProperty(FOX_LANG)
      .declareProperty(NE_TYPE)
      .declareValidationShape(getValidationModelFor(NEREnrichmentOperator.class))
      .declareValidationShape(getValidationModelFor(NEREnrichmentOperator.class))
      .build();
  }


  private void initializeFields() {
    final ValidatableParameterMap parameters = getParameterMap();
    // mandatory parameter literalProperty
    literalProperty = parameters.getOptional(LITERAL_PROPERTY)
      .map(n -> n.as(Property.class)).orElse(null);
    // optional parameter importProperty
    importProperty = parameters.getOptional(IMPORT_PROPERTY)
      .map(n -> n.as(Property.class)).orElse(DEFAULT_IMPORT_PROPERTY);
    try {
      // optional parameter foxUrl
      String urlString = parameters.getOptional(FOX_URL)
        .map(RDFNode::asResource).map(Resource::getURI).orElse(DEFAULT_FOX_URL);
      foxUri = new URL(urlString);
    } catch (MalformedURLException e) {
      throw new RuntimeException("Encountered bad URL in " + getId() + "!", e);
    }
    // optional parameter neType
    neType = parameters.getOptional(NE_TYPE)
      .map(RDFNode::asLiteral).map(l -> l.getString().toUpperCase()).map(NET::valueOf).orElse(NET.ALL);
    // optional parameter parallelism
    parallelism = parameters.getOptional(PARALLELISM)
      .map(RDFNode::asLiteral).map(Literal::getInt).orElse(DEFAULT_PARALLELISM);
    // fox detection language
    foxLang = parameters.getOptional(FOX_LANG)
      .map(RDFNode::asLiteral).map(l -> l.getString().toUpperCase()).map(FoxParameter.LANG::valueOf).orElse(FoxParameter.LANG.EN);
  }

//  /**
//   * Self configuration
//   * Set all parameters to default values, also extract all NEs
//   *
//   * @return Map of (key, value) pairs of self configured parameters
//   */
//  @Override
//  public ParameterMap learnParameterMap(Model source, Model target) {
//    return ParameterMap.EMPTY_INSTANCE;
//  }

  @Override
  protected List<Model> safeApply(List<Model> models) {
    initializeFields();
    final Model model = models.get(0);
    final Model resultModel = ModelFactory.createDefaultModel();
    resultModel.add(model);
    if (literalProperty == null) {
      literalProperty = LiteralPropertyRanker.getTopRankedProperty(model);
    }
    CompletableFuture<Void> future = new ThreadlocalInheritingCompletableFuture<>();
    List<CompletableFuture<Void>> pool = new ArrayList<>(parallelism);
    for (int z = 0; z < parallelism; z++) {
      pool.add(z == 0 ? future.thenApply(x->x) : future.thenApplyAsync(x->x));
    }
    AtomicInteger i = new AtomicInteger(0);
    model.listStatements(null, literalProperty, (RDFNode) null)
      .filterKeep(statement -> statement.getObject().isLiteral())
      .forEachRemaining(statement -> {
        i.compareAndSet(parallelism, 0);
        int j = i.getAndIncrement();
        final Resource subject = statement.getSubject();
        pool.set(j, pool.get(j).thenRun(() -> {
          Model result = null;
            switch (neType) {
              case ALL:
                result = runFOX(subject, statement.getObject().asLiteral().getString(), null);
                break;
              case LOCATION:
                result = runFOX(subject, statement.getObject().asLiteral().toString(), FOXO.LOCATION.getURI());
                break;
              case PERSON:
                result = runFOX(subject, statement.getObject().asLiteral().toString(), FOXO.PERSON.getURI());
                break;
              case ORGANIZATION:
                result = runFOX(subject, statement.getObject().asLiteral().toString(), FOXO.ORGANIZATION.getURI());
                break;
            }
          resultModel.enterCriticalSection(Lock.WRITE);
          try {
            resultModel.add(result);
          } finally {
            resultModel.leaveCriticalSection();
          }
        }));
      });
    future.complete(null);
    for (int z = 0; z < parallelism; z++) {
      future = future.thenCombine(pool.get(z), (a, b) -> null);
    }
    future.join();
    return Lists.newArrayList(resultModel);
  }

  private Model runFOX(Resource subject, String input, String type) {
    NEROperationID key = new NEROperationID(foxUri, input, type);
    List<String> result;
    CompletableFuture<List<String>> future = new ThreadlocalInheritingCompletableFuture<>();
    cache.putIfAbsent(key, future);
    if (cache.get(key) != future) {
      try {
        result = cache.get(key).get();
      } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
    } else {
      final IFoxApi fox = new FoxApi()
        .setApiURL(foxUri)
        .setTask(FoxParameter.TASK.NER)
        .setOutputFormat(FoxParameter.OUTPUT.TURTLE)
        .setLang(foxLang)
        .setInput(input)
        //@todo: parameterize
        .setLightVersion(FoxParameter.FOXLIGHT.ENBalie)
        .send();
      result = fox.responseAsClasses().getEntities().stream()
        .filter(e -> Optional.ofNullable(type).isEmpty() || e.getType().equals(type))
        .map(Entity::getUri)
        .collect(Collectors.toList());
      future.complete(result);
    }
    Model namedEntityModel = ModelFactory.createDefaultModel();
    result.forEach(e -> namedEntityModel.add(subject, importProperty, namedEntityModel.createResource(e)));
    return namedEntityModel;
  }

  private static class NEROperationID {
    private URL foxURL;
    private String input;
    private String type;

    private NEROperationID(URL foxURL, String input, String type) {
      this.foxURL = foxURL;
      this.input = input;
      this.type = type;
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder()
        .append(foxURL.toString())
        .append(input)
        .append(type)
        .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof NEROperationID) {
        NEROperationID o = (NEROperationID) obj;
        return new EqualsBuilder()
          .append(foxURL, o.foxURL)
          .append(input, o.input)
          .append(type, o.type)
          .isEquals();
      }
      return false;
    }
  }

  private static class LiteralPropertyRanker {

    static SortedMap<Double, Property> rank(Model model) {
      SortedMap<Double, Property> propertyRanks = new TreeMap<>(Collections.reverseOrder());
      model.listStatements()
        .mapWith(Statement::getPredicate)
        .forEachRemaining((Property property) -> {
          AtomicLong totalLitSize = new AtomicLong(1);
          AtomicLong totalLitCount = new AtomicLong(0);
          model.listObjectsOfProperty(property)
            .filterKeep(RDFNode::isLiteral)
            .mapWith(RDFNode::asLiteral)
            .forEachRemaining(l -> {
              totalLitCount.getAndIncrement();
              totalLitSize.addAndGet(l.toString().length());
            });
          double avgLitSize = (double) totalLitSize.get() / (double) totalLitCount.get();
          propertyRanks.put(avgLitSize, property);
        });
      return propertyRanks;
    }

    static Property getTopRankedProperty(Model model) {
      SortedMap<Double, Property> ranks = rank(model);
      return ranks.get(ranks.firstKey());
    }
  }

  @Override
  public double predictApplicability(List<Model> inputs, Model target) {
    if (target.contains(null, FOXO.RELATED_TO) && !inputs.get(0).contains(null, FOXO.RELATED_TO)) {
      return 1.0;
    }
    return 0;
  }

  @Override
  public List<Model> reverseApply(List<Model> inputs, Model target) {
    Model result = ModelFactory.createDefaultModel().add(target);
    result.removeAll(null, FOXO.RELATED_TO, null);
    return List.of(result);
  }

  @Override
  public ValidatableParameterMap learnParameterMap(List<Model> inputs, Model target, ValidatableParameterMap prototype) {
    return createParameterMap()
      .add(NEREnrichmentOperator.LITERAL_PROPERTY, RDFS.comment)
      .add(NEREnrichmentOperator.IMPORT_PROPERTY, FOXO.RELATED_TO)
      .add(NEREnrichmentOperator.NE_TYPE, ResourceFactory.createStringLiteral("all"))
      .add(NEREnrichmentOperator.FOX_URL, ResourceFactory.createResource(DEFAULT_FOX_URL))
      .add(NEREnrichmentOperator.PARALLELISM, ResourceFactory.createTypedLiteral(1))
      .init();
  }

  @Override
  public DegreeBounds getLearnableDegreeBounds() {
    return getDegreeBounds();
  }

}
