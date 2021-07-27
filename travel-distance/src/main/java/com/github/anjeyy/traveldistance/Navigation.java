package com.github.anjeyy.traveldistance;

public class Navigation {

  public static void main(String[] args) {
    //doit cleanse user input - format?

    //doit main execution

    Vertex solarSystem = Vertex.with("Solar System");
    Vertex betelgeuse = Vertex.with("Betelgeuse");
    Vertex sirius = Vertex.with("Sirius");
    Vertex vega = Vertex.with("Vega");

    Edge solarSystemToBetelgeuse = new Edge(solarSystem, betelgeuse, 5);
    Edge betelgeuseToVega = new Edge(betelgeuse, vega, 6);
    Edge betelgeuseToSirius = new Edge(betelgeuse, sirius, 8);

    DirectedWeightedGraph firstGraph = DirectedWeightedGraph.create();
    firstGraph.addEdge(solarSystemToBetelgeuse);
    firstGraph.addEdge(betelgeuseToVega);
    firstGraph.addEdge(betelgeuseToSirius);

    System.out.println(firstGraph);
  }
}
