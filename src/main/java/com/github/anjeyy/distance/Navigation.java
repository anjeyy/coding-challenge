package com.github.anjeyy.distance;

public class Navigation {

  public static void main(String[] args) {
    //doit cleanse user input - format?

    //doit main execution

    Vertex solarSystem = Vertex.with("Solar System");
    Vertex solarSystem2 = Vertex.with("Solar System");

    boolean isSame = solarSystem.equals(solarSystem2);
    System.out.println(isSame);
  }
}
