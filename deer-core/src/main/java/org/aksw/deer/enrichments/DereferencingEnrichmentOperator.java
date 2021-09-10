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
import org.aksw.deer.vocabulary.DBR;
import org.aksw.deer.vocabulary.DEER;
import org.aksw.faraday_cage.engine.ThreadlocalInheritingCompletableFuture;
import org.aksw.faraday_cage.engine.ValidatableParameterMap;
import org.aksw.jena_sparql_api.delay.core.QueryExecutionFactoryDelay;
import org.aksw.jena_sparql_api.http.QueryExecutionFactoryHttp;
import org.aksw.jena_sparql_api.pagination.core.QueryExecutionFactoryPaginated;
import org.apache.commons.collections15.MultiMap;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An EnrichmentOperator for dereferencing.
 * <p>
 * Dereferencing is a method of expanding knowledge of resources that belong to an
 * external knowledge base.
 * We define a dereferencing operation as the following sequence of operations:
 * <ol>
 *     <li>Query the local model for starting resources {@code ?x}
 *         that belong to an external knowledge base</li>
 *     <li>Query the external knowledge base for triples {@code (?x, d, ?y)},
 *         where d is a defined property path in the external knowledge base.</li>
 *     <li>Add all results to the local model in the form of {@code (?x, i, ?y)},
 *         where i is the property name under which they should be imported.</li>
 * </ol>
 * <h2>Configuration</h2>
 *
 * <h3>{@code :operations}</h3>
 *
 *
 *
 * Each operation corresponds to one dereferencing operation, allowing multiple
 * dereferencing operations being carried out by a single {@code DereferencingEnrichmentOperator}.
 * Each entry may contain the following properties:
 *
 * <blockquote>
 *     <b>{@code :lookUpProperty}</b>
 *     <i>range: resource</i>
 *     <br>
 *     Determines the starting resources {@code ?x} as all objects of triples having
 *     the value of {@code :lookUpProperty} as predicate.
 * </blockquote>
 *
 * <blockquote>
 *     <b>{@code :lookUpPrefix} [required]</b>
 *     <i>range: string</i>
 *     <br>
 *     Determines the starting resources {@code ?x} as all objects of triples having
 *     the value of {@code :lookUpProperty} as predicate.
 * </blockquote>
 *
 * <blockquote>
 *     <b>{@code :dereferencingProperty} [required]</b>
 *     <i>range: resource</i>
 *     <br>
 *     Look up the values to be imported to the local model in the external knowledge base
 *     using the property defined by the value of {@code :dereferencingProperty}.
 * </blockquote>
 *
 * <blockquote>
 *     <b>{@code :importProperty} [required]</b>
 *     <i>range: resource</i>
 *     <br>
 *     Add looked up values to starting resources using the value of :importProperty.
 * </blockquote>
 *
 *
 * <h2>Example</h2>
 *
 */
@Extension
public class DereferencingEnrichmentOperator extends AbstractParameterizedEnrichmentOperator implements ReverseLearnable, SelfConfigurable {

  private static final Logger logger = LoggerFactory.getLogger(DereferencingEnrichmentOperator.class);

  public static final Property LOOKUP_PROPERTY = DEER.property("lookUpProperty");

  public static final Property LOOKUP_PREFIX = DEER.property("lookUpPrefix");

  public static final Property DEREFERENCING_PROPERTY = DEER.property("dereferencingProperty");

  public static final Property IMPORT_PROPERTY = DEER.property("importProperty");

  public static final Property OPERATION = DEER.property("operation");

  //@todo implement this in SHACL
  public static final Property DEREFERENCING_MODE = DEER.property("dereferencingMode");

  public static final Property USE_SPARQL_ENDPOINT = DEER.property("useSparqlEndpoint");

  private static String DEFAULT_LOOKUP_PREFIX = DBR.getURI();

  private static String DEFAULT_SPARQL_ENDPOINT = "https://dbpedia.org/sparql";

  private static final ConcurrentMap<OperationInstance, CompletableFuture<List<RDFNode>>> sparqlCache = new ConcurrentHashMap<>();

  private String endpoint;

  private Model model;

  @Override
  public String getDescription() {
    return "Query additional triples from SPARQL endpoints or via content negotiation";
  }

  @Override
  public ValidatableParameterMap createParameterMap() {
    return ValidatableParameterMap.builder()
      .declareProperty(OPERATION)
      .declareProperty(USE_SPARQL_ENDPOINT)
      .declareValidationShape(getValidationModelFor(DereferencingEnrichmentOperator.class))
      .build();
  }

  private static class OperationGroup {

    private final String lookUpPrefix;
    private final Property lookUpProperty;

    private OperationGroup(Resource opRef) {
      lookUpProperty = opRef.getPropertyResourceValue(LOOKUP_PROPERTY) == null ?
        null : opRef.getPropertyResourceValue(LOOKUP_PROPERTY).as(Property.class);
      lookUpPrefix = opRef.getProperty(LOOKUP_PREFIX) == null ?
        DEFAULT_LOOKUP_PREFIX : opRef.getProperty(LOOKUP_PREFIX).getString();
    }

    String getLookUpPrefix() {
      return lookUpPrefix;
    }

    Optional<Property> getLookUpProperty() {
      return Optional.ofNullable(lookUpProperty);
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof  OperationGroup)) {
        return false;
      }
      OperationGroup other = (OperationGroup) obj;
      return (Objects.equals(lookUpPrefix, other.lookUpPrefix))
        && (Objects.equals(lookUpProperty, other.lookUpProperty));
    }

    @Override
    public int hashCode() {
      return lookUpPrefix.hashCode() + 13 * (lookUpProperty == null ? -1 : lookUpProperty.hashCode());
    }

    public String toString() {
      return "(lookupPrefix: " + lookUpPrefix + (lookUpProperty == null ? "" : ", lookupProperty: " + lookUpProperty) + ")";
    }

  }

  private static class Operation {

    private final Property dereferencingProperty;
    private final Property importProperty;

    private Operation(Resource opRef) {
      dereferencingProperty = opRef.getPropertyResourceValue(DEREFERENCING_PROPERTY) == null
        ? null : opRef.getPropertyResourceValue(DEREFERENCING_PROPERTY).as(Property.class);
      importProperty = opRef.getPropertyResourceValue(IMPORT_PROPERTY) == null
        ? dereferencingProperty : opRef.getPropertyResourceValue(IMPORT_PROPERTY).as(Property.class);
    }

    public Property getDereferencingProperty() {
      return dereferencingProperty;
    }

    public Property getImportProperty() {
      return importProperty;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Operation operation = (Operation) o;

      if (!dereferencingProperty.equals(operation.dereferencingProperty)) return false;
      return importProperty.equals(operation.importProperty);
    }

    @Override
    public int hashCode() {
      int result = dereferencingProperty.hashCode();
      result = 31 * result + importProperty.hashCode();
      return result;
    }
  }

  private static class OperationInstance {

    private final String endpoint;
    private final Resource candidate;
    private final Property dereferencingProperty;

    private OperationInstance(String endpoint, Resource candidate, Operation operation) {
      this.endpoint = endpoint;
      this.candidate = candidate;
      this.dereferencingProperty = operation.dereferencingProperty;
    }

    public String getEndpoint() {
      return endpoint;
    }

    public Resource getCandidate() {
      return candidate;
    }

    public Property getDereferencingProperty() {
      return dereferencingProperty;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      OperationInstance that = (OperationInstance) o;

      if (!endpoint.equals(that.endpoint)) return false;
      if (!candidate.equals(that.candidate)) return false;
      return dereferencingProperty.equals(that.dereferencingProperty);
    }

    @Override
    public int hashCode() {
      int result = endpoint.hashCode();
      result = 31 * result + candidate.hashCode();
      result = 31 * result + dereferencingProperty.hashCode();
      return result;
    }
  }

  @Override
  protected List<Model> safeApply(List<Model> models) {
    model = ModelFactory.createDefaultModel().add(models.get(0));
    endpoint = getParameterMap().get(USE_SPARQL_ENDPOINT).asResource().getURI();
    Stream<Statement> dereferencedStatements = getOperationGroups().entrySet().stream()
      .map(entry -> parallelRunOperations(entry.getKey(), entry.getValue()))
      .reduce(
        ThreadlocalInheritingCompletableFuture.completedFuture(Stream.empty()),
        (a, b) -> a.thenCombine(b, Stream::concat)
      ).join();
    return Lists.newArrayList(model.add(dereferencedStatements.collect(Collectors.toList())));
  }

  public Map<OperationGroup, List<Operation>> getOperationGroups() {
    var parameters = getParameterMap();
    var opGroups = new HashMap<OperationGroup, List<Operation>>();
    parameters.listPropertyObjects(OPERATION)
      .map(RDFNode::asResource)
      .forEach(opRef -> {
        var opGroup = new OperationGroup(opRef);
        opGroups.putIfAbsent(opGroup, new ArrayList<>());
        opGroups.get(opGroup).add(new Operation(opRef));
      });
    return opGroups;
  }

  private CompletableFuture<Stream<Statement>> parallelRunOperations(OperationGroup opGroup, List<Operation> operations) {
    List<CompletableFuture<Model>> queryFutureEffectivelyFinal = new ArrayList<>();
    queryFutureEffectivelyFinal.add(new ThreadlocalInheritingCompletableFuture<>());
    List<OperationInstance> currentlyQueuedInstances = new ArrayList<>();
    var i = new AtomicInteger(0);
    var result = getCandidates(opGroup).stream().flatMap(candidate ->
      operations.stream().map(operation -> {
        var instance = new OperationInstance(endpoint, candidate, operation);
        CompletableFuture<Model> queryFuture = queryFutureEffectivelyFinal.get(0);
        CompletableFuture<List<RDFNode>> valuesFuture =
          sparqlCache.computeIfAbsent(instance, in -> {
            currentlyQueuedInstances.add(in);
            if (i.incrementAndGet() % 100 == 0) {
              executeQueryAndCompleteModelFuture(currentlyQueuedInstances, queryFuture);
              currentlyQueuedInstances.clear();
              queryFutureEffectivelyFinal.clear();
              queryFutureEffectivelyFinal.add(new ThreadlocalInheritingCompletableFuture<>());
            }
            return queryFuture.thenApplyAsync(m ->
              m.listStatements(candidate, operation.getDereferencingProperty(), (RDFNode) null)
                .mapWith(Statement::getObject)
                .toList()
            );
          });
        // just add consumer and join
        return valuesFuture.thenApply(values -> values.stream().map(v ->
          //@todo refactor into method in order to enable different append modes
          ResourceFactory.createStatement(candidate, operation.getImportProperty(), v)));
      })
    ).reduce(
      ThreadlocalInheritingCompletableFuture.completedFuture(Stream.empty()),
      (a, b) -> a.thenCombine(b, Stream::concat)
    );
    if (!currentlyQueuedInstances.isEmpty()) {
      executeQueryAndCompleteModelFuture(currentlyQueuedInstances, queryFutureEffectivelyFinal.get(0));
    }
    return result;
  }

  private List<Resource> getCandidates(OperationGroup opGroup) {
    ExtendedIterator<Resource> candidates = model.listStatements()
      .filterKeep(statement ->
        statement.getObject().isURIResource() &&
          statement.getResource().getURI().startsWith(opGroup.getLookUpPrefix()) &&
          (opGroup.getLookUpProperty().isEmpty() ||
            statement.getPredicate().equals(opGroup.getLookUpProperty().get())
          )
      )
      .mapWith(Statement::getResource);
    if (opGroup.getLookUpProperty().isEmpty()) {
      ExtendedIterator<Resource> subjectCandidates = model.listStatements()
        .filterKeep(stmt ->
          stmt.getSubject().isURIResource() &&
            stmt.getSubject().getURI().startsWith(opGroup.getLookUpPrefix()))
        .mapWith(Statement::getSubject);
      candidates = candidates.andThen(subjectCandidates);
    }
    return candidates.toList();
  }

  private void executeQueryAndCompleteModelFuture(List<OperationInstance> currentlyQueuedInstances, CompletableFuture<Model> queryFuture) {
    final var sparqlQueryBuilder = new StringBuilder();
    sparqlQueryBuilder.append("SELECT ?source ?deref ?dereffed WHERE { VALUES ( ?source ?deref ) { ");
    for (OperationInstance opInstance : currentlyQueuedInstances) {
      sparqlQueryBuilder.append("(<");
      sparqlQueryBuilder.append(opInstance.getCandidate());
      sparqlQueryBuilder.append("> <");
      sparqlQueryBuilder.append(opInstance.getDereferencingProperty());
      sparqlQueryBuilder.append(">)\n");
    }
    sparqlQueryBuilder.append("} ?source ?deref ?dereffed . }");
    org.aksw.jena_sparql_api.core.QueryExecutionFactory qef = new QueryExecutionFactoryHttp(endpoint);
    qef = new QueryExecutionFactoryDelay(qef, 100);
    qef = new QueryExecutionFactoryPaginated(qef, 20000);
    final QueryExecution queryExecution = qef.createQueryExecution(sparqlQueryBuilder.toString());
    ResultSet resultSet = queryExecution.execSelect();
    Model m = ModelFactory.createDefaultModel();
    resultSet.forEachRemaining(result -> {
      Statement stmt = ResourceFactory.createStatement(
        result.getResource("?source"),
        ResourceFactory.createProperty(result.getResource("?deref").getURI()),
        result.getResource("?dereffed"));
      m.add(stmt);
    });
    //@todo test for deadlock on connection failure etc.
    queryExecution.close();
    queryFuture.complete(m);
  }

  @Override
  public double predictApplicability(List<Model> inputs, Model target) {
    return learnParameterMap(inputs, target, null).listPropertyObjects(OPERATION).findAny().isPresent() ? 1 : 0;
  }

  @Override
  public List<Model> reverseApply(List<Model> inputs, Model target) {
    Model result = ModelFactory.createDefaultModel();
    Set<Resource> predicatesForDeletion = learnParameterMap(inputs, target, null).listPropertyObjects(OPERATION)
      .map(n -> n.asResource().getPropertyResourceValue(DEREFERENCING_PROPERTY))
      .collect(Collectors.toSet());
    target.listStatements()
      .filterDrop(stmt -> stmt.getSubject().getURI().startsWith(DEFAULT_LOOKUP_PREFIX)
        && predicatesForDeletion.contains(stmt.getPredicate()))
      .forEachRemaining(result::add);
    return List.of(result);
  }

  @Override
  public ValidatableParameterMap learnParameterMap(List<Model> inputs, Model target, ValidatableParameterMap prototype) {
    Model in = inputs.get(0);
    ValidatableParameterMap result = createParameterMap();
    result.add(USE_SPARQL_ENDPOINT, result.createResource(DEFAULT_SPARQL_ENDPOINT));
    target.listStatements()
      .filterDrop(stmt -> stmt.getObject().isLiteral())
      .mapWith(Statement::getResource)
      .filterKeep(r -> r.getURI().startsWith(DEFAULT_LOOKUP_PREFIX))
      .forEachRemaining(r -> target.listStatements(r, null, (RDFNode) null)
        .filterDrop(stmt -> in.contains(r, stmt.getPredicate(), (RDFNode) null))
        .mapWith(Statement::getPredicate).toSet()
        .forEach(p -> result.add(OPERATION,
          result.createResource()
            .addProperty(LOOKUP_PREFIX, DEFAULT_LOOKUP_PREFIX)
            .addProperty(DEREFERENCING_PROPERTY, p)
        )));
    return result.init();
  }

  @Override
  public DegreeBounds getLearnableDegreeBounds() {
    return getDegreeBounds();
  }

}
