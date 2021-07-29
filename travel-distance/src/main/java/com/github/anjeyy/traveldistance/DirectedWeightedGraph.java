package com.github.anjeyy.traveldistance;

import com.github.anjeyy.traveldistance.util.CollectionUtil;
import com.github.anjeyy.traveldistance.util.StringConstant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of a <i>directed</i>, <i>weighted</i> and possibly <i>cyclic</i> graph.
 * For a detailed explanation please have a look at referenced links below.
 *
 * @see <a href="https://www.geeksforgeeks.org/graph-data-structure-and-algorithms/">Graph data structure</a>
 * @see <a href="https://en.wikipedia.org/wiki/Graph_(abstract_data_type)">Wiki: Graph data type</a>
 * @author Andjelko Perisic
 */
class DirectedWeightedGraph {

  private static final String NO_SUCH_ROUTE = "NO SUCH ROUTE";

  private final Map<Vertex, Set<Edge>> adjacencyList;

  /**
   * Static factory method for an easier use and initialization of {@link DirectedWeightedGraph}.
   * @return initialized empty graph
   */
  static DirectedWeightedGraph create() {
    return new DirectedWeightedGraph();
  }

  private DirectedWeightedGraph() {
    adjacencyList = new HashMap<>();
  }

  void addVertex(Vertex vertex) {
    adjacencyList.putIfAbsent(vertex, new LinkedHashSet<>());
  }

  void removeVertex(Vertex vertex) {
    adjacencyList.remove(vertex);
  }

  void addEdge(Edge edge) {
    Vertex sourceVertex = edge.getSource();
    boolean vertexNotExistent = !adjacencyList.containsKey(sourceVertex);
    if (vertexNotExistent) {
      addVertex(sourceVertex);
    }
    adjacencyList.computeIfPresent(
      sourceVertex,
      (key, val) -> addEdgeAsNeighbour(val, edge)
    );
  }

  private static Set<Edge> addEdgeAsNeighbour(Set<Edge> edgeSet, Edge newEdge) {
    edgeSet.add(newEdge);
    return edgeSet;
  }

  void removeEdge(Edge edge) {
    Vertex sourceVertex = edge.getSource();
    boolean vertexExists = adjacencyList.containsKey(sourceVertex);
    if (vertexExists) {
      adjacencyList.values().removeIf(e -> e.contains(edge));
    }
  }

  /**
   * For a given route, expected as input parameter, calculate the amount of hours traveled.
   * <p>
   * <b>Note:</b> This is simply the sum of all {@link Edge#weight weights} from the corresponding {@link Edge edges}.
   * Traversing through a linked list, represented by a {@link java.util.HashSet}.
   *
   * @param vertices given route to calculate travel time for
   * @return calculated travel time in format 'x hours', where x is a number
   */
  String travelTimeForGivenRoute(List<Vertex> vertices) {
    int distance = 0;
    for (int i = 0; i < vertices.size() - 1; i++) {
      Vertex currVertex = vertices.get(i);
      Set<Edge> edgeSet = adjacencyList.get(currVertex);
      if (edgeSet == null) {
        return NO_SUCH_ROUTE;
      }
      Vertex destination = vertices.get(i + 1);
      boolean destinationVertexIsNotPresent = edgeSet
        .stream()
        .map(Edge::getDestination)
        .noneMatch(v -> v.equals(destination));
      if (destinationVertexIsNotPresent) {
        return NO_SUCH_ROUTE;
      }
      distance =
        distance +
        edgeSet
          .stream()
          .filter(e -> e.getDestination().equals(destination))
          .findAny()
          .map(Edge::getWeight)
          .orElseThrow(
            () ->
              new IllegalStateException(
                String.format(
                  "Edge '%s' from '%s' has to be present, due to prior check.",
                  destination,
                  edgeSet
                )
              )
          );
    }
    return distance + " hours";
  }

  String determineAndDisplayRoutesWithExactlyStops(
    Vertex source,
    Vertex destination,
    int exactInBetweenStops
  ) {
    if (exactInBetweenStops < 0) {
      throw new IllegalArgumentException(
        "Please provide in between stop count >=0."
      );
    }
    int totalMaxStops = exactInBetweenStops + 2; // exactInBetweenStops + (source + destination)
    List<List<Vertex>> paths = determineRoutesWithMaxStops(
      source,
      destination,
      totalMaxStops
    )
      .stream()
      .filter(p -> p.size() == totalMaxStops)
      .collect(Collectors.toList());
    return printFoundRoutes(paths);
  }

  /**
   * Given a source and destination as {@link Vertex vertex} for a
   * {@link DirectedWeightedGraph directed weighted graph}, this method performs a <i>breadth first search</i>
   * algorithm. Additionally all paths with matching source and destination are returned formatted.<br>
   * <b>Note: </b> Self-reference is not allowed, so the trivial routes are excluded.
   *
   * @param source starting point
   * @param destination ending point
   * @param maxStops maximal stops or depth in a BFS
   * @return proper formatted routes
   */
  String determineAndDisplayRoutesWithMaxStops(
    Vertex source,
    Vertex destination,
    int maxStops
  ) {
    if (maxStops < 1) {
      throw new IllegalArgumentException("Please provide max stops >0.");
    }
    List<List<Vertex>> paths = determineRoutesWithMaxStops(
      source,
      destination,
      maxStops
    );
    return printFoundRoutes(paths);
  }

  /**
   * Expected found paths from previously calculated methods. Displays is as a proper {@link String}.
   *
   * @param paths collected routes
   * @return formatted found routes
   */
  private static String printFoundRoutes(List<List<Vertex>> paths) {
    return paths.isEmpty()
      ? NO_SUCH_ROUTE
      : paths.size() +
      " routes: \n" +
      paths.stream().map(Object::toString).collect(Collectors.joining("\n"));
  }

  private List<List<Vertex>> determineRoutesWithMaxStops(
    Vertex source,
    Vertex destination,
    int maxStops
  ) {
    // precondition check
    Set<Edge> startingEdges = adjacencyList.get(source);
    boolean sourceIsInvalid = startingEdges == null || startingEdges.isEmpty();
    boolean destinationIsInvalid = isDestinationVertexValid(destination);
    if (sourceIsInvalid || destinationIsInvalid) {
      return Collections.emptyList();
    }

    // initialization
    List<List<Vertex>> result = new ArrayList<>();
    Queue<List<Vertex>> queue = new ArrayDeque<>();
    List<Vertex> path = new ArrayList<>();
    path.add(source);
    queue.offer(path);

    // iterative traversing
    while (CollectionUtil.isNotEmpty(queue)) {
      path = queue.poll();
      Vertex lastVertex = CollectionUtil.retrieveLastElement(path);
      if (lastVertex.equals(destination)) {
        result.add(path);
      }
      boolean maxDepthNotReached = path.size() != (maxStops + 1);
      if (maxDepthNotReached) {
        // visit neighbors
        List<Vertex> neighborVertices = getNeighborVertices(lastVertex);
        for (Vertex neighbor : neighborVertices) {
          List<Vertex> newPath = new ArrayList<>(path);
          newPath.add(neighbor);
          queue.offer(newPath);
        }
      }
    }
    return result
      .stream()
      .filter(e -> e.size() > 1) // remove self-reference
      .collect(Collectors.toList());
  }

  private boolean isDestinationVertexValid(Vertex destination) {
    return adjacencyList
      .values()
      .stream()
      .flatMap(Collection::stream)
      .map(Edge::getDestination)
      .noneMatch(v -> v.equals(destination));
  }

  private List<Vertex> getNeighborVertices(Vertex vertex) {
    return Optional
      .of(vertex)
      .map(adjacencyList::get)
      .orElse(Collections.emptySet())
      .stream()
      .map(Edge::getDestination)
      .collect(Collectors.toList());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DirectedWeightedGraph that = (DirectedWeightedGraph) o;
    return adjacencyList.equals(that.adjacencyList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adjacencyList);
  }

  @Override
  public String toString() {
    String graphString = adjacencyList
      .entrySet()
      .stream()
      .map(e -> e.getKey() + " -- " + e.getValue())
      .collect(Collectors.joining(StringConstant.NEW_LINE.getValue()));
    return (
      "[Directed weighted graph: " +
      StringConstant.NEW_LINE.getValue() +
      graphString
    );
  }
}
