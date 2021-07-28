package com.github.anjeyy.traveldistance;

import com.github.anjeyy.traveldistance.util.StringConstant;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class DirectedWeightedGraph {

  private static final String NO_SUCH_ROUTE = "NO SUCH ROUTE";

  private final Map<Vertex, Set<Edge>> adjacencyList;

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
