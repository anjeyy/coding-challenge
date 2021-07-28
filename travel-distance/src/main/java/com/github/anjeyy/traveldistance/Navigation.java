package com.github.anjeyy.traveldistance;

import java.util.List;

public class Navigation {

  public static void main(String[] args) {
    List<Edge> edgeList = List.of(
      new Edge(Vertex.with("Solar System"), Vertex.with("Alpha Centauri"), 5),
      new Edge(Vertex.with("Solar System"), Vertex.with("Betelgeuse"), 5),
      new Edge(Vertex.with("Solar System"), Vertex.with("Vega"), 7),
      new Edge(Vertex.with("Alpha Centauri"), Vertex.with("Sirius"), 4),
      new Edge(Vertex.with("Betelgeuse"), Vertex.with("Sirius"), 8),
      new Edge(Vertex.with("Betelgeuse"), Vertex.with("Vega"), 6),
      new Edge(Vertex.with("Sirius"), Vertex.with("Betelgeuse"), 8),
      new Edge(Vertex.with("Sirius"), Vertex.with("Vega"), 2),
      new Edge(Vertex.with("Vega"), Vertex.with("Alpha Centauri"), 3)
    );

    DirectedWeightedGraph graph = DirectedWeightedGraph.create();
    edgeList.forEach(graph::addEdge);

    System.out.println(graph);
  }
}
