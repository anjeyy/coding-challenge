package com.github.anjeyy.traveldistance;

public class Navigation {

  public static void main(String[] args) {
    //doit cleanse user input - format?

    //doit main execution

    Vertex solarSystem = Vertex.with("Solar System");
    Vertex vega = Vertex.with("Vega");
    Edge solarSystemToVega = new Edge(solarSystem, vega, 7);

    System.out.println(solarSystem);
    System.out.println(vega);
    System.out.println(solarSystemToVega);
  }
}
