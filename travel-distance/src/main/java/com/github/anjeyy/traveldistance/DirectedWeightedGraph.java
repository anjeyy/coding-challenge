package com.github.anjeyy.traveldistance;

import com.github.anjeyy.traveldistance.util.CollectionUtil;
import com.github.anjeyy.traveldistance.util.StringConstant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

  String determineAndDisplayRoutesWithMaxTime(
    Vertex source,
    Vertex destination,
    int maxTime
  ) {
    if (maxTime < 1) {
      throw new IllegalArgumentException("Please provide max time >0.");
    }
    List<List<Vertex>> paths = determineRoutesWithMaxWeights(
      source,
      destination,
      maxTime
    );
    return printFoundRoutes(paths);
  }

  private List<List<Vertex>> determineRoutesWithMaxWeights(
    Vertex source,
    Vertex destination,
    int maxTravelTime
  ) {
    if (preconditionFailed(source, destination)) {
      return Collections.emptyList();
    }

    // starting point evaluation
    Set<Edge> edgeSet = adjacencyList.get(source);
    boolean maxTravelTimeExceeded = edgeSet
      .stream()
      .map(Edge::getWeight)
      .noneMatch(i -> i < maxTravelTime);
    if (maxTravelTimeExceeded) {
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
      // visit neighbors
      List<Vertex> neighborVertices = getNeighborVertices(lastVertex);
      for (Vertex neighbor : neighborVertices) {
        int newPathTime = calculatePathTimeWithPotentialNeighbor(
          path,
          neighbor
        );
        if (newPathTime < maxTravelTime) {
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

  private int calculatePathTimeWithPotentialNeighbor(
    List<Vertex> path,
    Vertex neighbor
  ) {
    List<Vertex> potentialNewPath = new ArrayList<>(path);
    potentialNewPath.add(neighbor);

    int time = 0;
    for (int i = 0; i < potentialNewPath.size() - 1; i++) {
      Vertex tmpStart = potentialNewPath.get(i);
      Set<Edge> tmpStartNeighbors = adjacencyList.get(tmpStart);
      Vertex tmpEnd = potentialNewPath.get(i + 1);
      int currentTime = tmpStartNeighbors
        .stream()
        .filter(e -> e.getDestination().equals(tmpEnd))
        .findAny()
        .map(Edge::getWeight)
        .orElseThrow();
      time = time + currentTime;
    }
    return time;
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
      " routes: " +
      StringConstant.NEW_LINE.getValue() +
      paths
        .stream()
        .map(Object::toString)
        .collect(Collectors.joining(StringConstant.NEW_LINE.getValue()));
  }

  private List<List<Vertex>> determineRoutesWithMaxStops(
    Vertex source,
    Vertex destination,
    int maxStops
  ) {
    if (preconditionFailed(source, destination)) {
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

  private List<Vertex> getNeighborVertices(Vertex vertex) {
    return Optional
      .of(vertex)
      .map(adjacencyList::get)
      .orElse(Collections.emptySet())
      .stream()
      .map(Edge::getDestination)
      .collect(Collectors.toList());
  }

  /**
   * A variation of the well-known <b>Dijkstra algorithm</b>, for further and detailed information
   * please visit referenced link.
   * <p>
   * <b>Note:</b> This implementation has a slightly modified version, since it is possible to set the
   * <b>SAME</b> vertex as {@code source} and {@code destination}.
   *
   * @throws IllegalStateException if there is no connection from source or destination is not reachable
   *
   * @see <a href="https://www.freecodecamp.org/news/dijkstras-shortest-path-algorithm-visual-introduction/">Dijkstra algorithm</a>
   *
   * @param source starting vertex
   * @param destination ending vertex
   * @return shortest route, if not present 'NO SUCH ROUTE'
   */
  String calculateShortestRoute(Vertex source, Vertex destination) {
    if (preconditionFailed(source, destination)) {
      return NO_SUCH_ROUTE;
    }
    boolean negativeWeightPresent = adjacencyList
      .values()
      .stream()
      .flatMap(Collection::stream)
      .map(Edge::getWeight)
      .anyMatch(v -> v < 0);
    if (negativeWeightPresent) {
      throw new IllegalStateException(
        "This Graph is in a non-appropriate state." +
        StringConstant.NEW_LINE.getValue() +
        "No shortest path findings can be determined due to negative weights."
      );
    }
    Vertex tmpDestination = createTemporaryVertexForEqualStartAndEndVertex(
      source,
      destination
    );
    if (tmpDestination != null) {
      destination = tmpDestination;
    }

    // initialize distance
    Map<Vertex, Integer> distance = adjacencyList
      .keySet()
      .stream()
      .collect(Collectors.toMap(Function.identity(), k -> Integer.MAX_VALUE));
    distance.put(source, 0); //starting vertex
    Set<Vertex> path = new HashSet<>();
    path.add(source);

    while (CollectionUtil.doesNotContain(path, destination)) {
      // determine neighbors from already selected path set
      Set<Edge> connectingEdges = path
        .stream()
        .map(adjacencyList::get)
        .flatMap(Collection::stream)
        .filter(Objects::nonNull)
        .filter(e -> CollectionUtil.doesNotContain(path, e.getDestination())) // exclude vertices inside path
        .collect(Collectors.toSet());

      // update distance for vertices outside path
      for (Edge neighbor : connectingEdges) {
        Vertex vertexInsidePath = neighbor.getSource();
        int currentPathDistance = distance.get(vertexInsidePath);
        int neighborDistance = neighbor.getWeight();

        Vertex vertexOutsidePath = neighbor.getDestination();
        int newPathDistance = currentPathDistance + neighborDistance;
        int oldUpdatedDistance = distance.get(vertexOutsidePath);
        if (newPathDistance < oldUpdatedDistance) {
          distance.put(vertexOutsidePath, newPathDistance);
        }
      }

      // decide which node taking to the path
      distance
        .entrySet()
        .stream()
        .filter(e -> CollectionUtil.doesNotContain(path, e.getKey())) // only unvisited vertices
        .min(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .ifPresent(path::add);
    }

    if (tmpDestination != null) {
      removeVertex(tmpDestination);
    }
    return distance.get(destination) + " hours";
  }

  private boolean preconditionFailed(Vertex source, Vertex destination) {
    Set<Edge> startingEdges = adjacencyList.get(source);
    boolean sourceIsInvalid = startingEdges == null || startingEdges.isEmpty();
    boolean destinationIsInvalid = isDestinationVertexValid(destination);
    return sourceIsInvalid || destinationIsInvalid;
  }

  private boolean isDestinationVertexValid(Vertex destination) {
    return getAllEdgesAsStream()
      .map(Edge::getDestination)
      .noneMatch(v -> v.equals(destination));
  }

  /**
   * Logically this is the same vertex, just for computation purpose it's being split into two different ones.<br>
   * Specifically used to distinguish between <i>starting</i> and <i>ending</i> vertex, when they are the <b>SAME</b>.
   *
   * @param source starting vertex
   * @param destination ending vertex
   * @return duplicated and manipulated vertex for computation purpose
   */
  private Vertex createTemporaryVertexForEqualStartAndEndVertex(
    Vertex source,
    Vertex destination
  ) {
    Vertex tmpDestination = null;
    if (source.equals(destination)) {
      String temporaryName = findUniqueName(destination);
      tmpDestination = Vertex.with(temporaryName);
      Set<Edge> edgeSet = getAllEdgesAsStream()
        .filter(e -> e.getDestination().equals(destination))
        .collect(Collectors.toSet());
      for (Edge edge : edgeSet) {
        edge.setDestination(tmpDestination);
      }
      adjacencyList.put(tmpDestination, edgeSet);
    }
    return tmpDestination;
  }

  /**
   * Helping method with a very simple and naive heuristic to duplicate a vertex. <br>
   * Already provided name of a vertex is being added several times at the end, till there is no existing vertex.
   *
   * @param vertex vertex to duplicate
   * @return unique name
   */
  private String findUniqueName(Vertex vertex) {
    final String rawName = vertex.getLabel();
    String tmpName = rawName + StringConstant.WHITESPACE.getValue() + rawName;
    boolean nameNotFound = true;
    while (nameNotFound) {
      boolean nameExistsAlready = adjacencyList
        .keySet()
        .stream()
        .map(Vertex::getLabel)
        .collect(Collectors.toList())
        .contains(tmpName);
      if (nameExistsAlready) {
        tmpName = tmpName + StringConstant.WHITESPACE.getValue() + rawName;
      } else {
        nameNotFound = false;
      }
    }
    return tmpName;
  }

  /**
   * Retrieve a stream of all edges, enabling for further transformation or extracting on the returned container.
   *
   * @return all edges as stream
   */
  private Stream<Edge> getAllEdgesAsStream() {
    return adjacencyList.values().stream().flatMap(Collection::stream);
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
