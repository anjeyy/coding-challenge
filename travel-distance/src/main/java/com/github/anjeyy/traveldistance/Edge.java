package com.github.anjeyy.traveldistance;

import java.util.Objects;

/**
 * Represents a so called edge inside a graph data structure.<br>
 * For a detailed explanation please have a look at referenced links below.
 * <p>
 * <b>Note: </b> Self-reference is NOT allowed.
 *
 * @see <a href="https://www.geeksforgeeks.org/graph-data-structure-and-algorithms/">Graph data structure</a>
 * @see <a href="https://en.wikipedia.org/wiki/Graph_(abstract_data_type)">Wiki: Graph data type</a>
 * @author Andjelko Perisic
 */
class Edge {

  private Vertex source;
  private Vertex destination;
  private int weight;

  /**
   * Initialized the edge and establishes a connection between two vertices, they are then so called neighbors.
   *
   * @param source starting vertex
   * @param destination ending vertex
   * @param weight value of the edge
   */
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
    checkForSelfReference(source, destination);
  }

  public Vertex getSource() {
    return source;
  }

  public void setSource(Vertex source) {
    if (source == null) {
      throw new NullPointerException("Vertex source has to be set.");
    }
    checkForSelfReference(source, destination);
    this.source = source;
  }

  public Vertex getDestination() {
    return destination;
  }

  public void setDestination(Vertex destination) {
    if (destination == null) {
      throw new NullPointerException("Vertex destination has to be set.");
    }
    checkForSelfReference(source, destination);
    this.destination = destination;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  private void checkForSelfReference(Vertex source, Vertex destination) {
    if (source.equals(destination)) {
      throw new IllegalArgumentException("No self reference allowed.");
    }
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

  @Override
  public String toString() {
    return String.format(
      "[Edge: '%s' --(%d)-> '%s']",
      source.getLabel(),
      weight,
      destination.getLabel()
    );
  }
}
