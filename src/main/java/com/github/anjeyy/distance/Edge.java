package com.github.anjeyy.distance;

import java.util.Objects;

class Edge {

  private Vertex source;
  private Vertex destination;
  private int weight;

  Edge(Vertex source, Vertex destination, int weight) {
    checkValidVertices(source, destination);
    this.source = source;
    this.destination = destination;
    this.weight = weight;
  }

  private void checkValidVertices(Vertex source, Vertex destination) {
    if (source == null || destination == null) {
      throw new NullPointerException(
        "Vertex source and destination has to be set."
      );
    }
  }

  public Vertex getSource() {
    return source;
  }

  public void setSource(Vertex source) {
    this.source = source;
  }

  public Vertex getDestination() {
    return destination;
  }

  public void setDestination(Vertex destination) {
    this.destination = destination;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Edge edge = (Edge) o;
    return source.equals(edge.source) && destination.equals(edge.destination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, destination);
  }
}
