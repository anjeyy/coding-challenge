package com.github.anjeyy.traveldistance;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

class DirectedWeightedGraph {

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
  //doit equals, hashcode, toString method

}
