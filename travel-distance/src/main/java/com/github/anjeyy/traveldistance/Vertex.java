package com.github.anjeyy.traveldistance;

import java.util.Locale;
import java.util.Objects;

/**
 * Represents a so called node or vertex inside a graph data structure.<br>
 * For a detailed explanation please have a look at referenced links below.
 *
 * @see <a href="https://www.geeksforgeeks.org/graph-data-structure-and-algorithms/">Graph data structure</a>
 * @see <a href="https://en.wikipedia.org/wiki/Graph_(abstract_data_type)">Wiki: Graph data type</a>
 * @author Andjelko Perisic
 */
class Vertex {

  private final String label;

  /**
   * Static factory method for an easier use and initialization of {@link Vertex}.
   *
   * @param label name of the vertex
   * @return initialized vertex
   */
  static Vertex with(String label) {
    if (label == null) {
      throw new NullPointerException("'null' as vertex label is NOT allowed.");
    }
    return new Vertex(label.toLowerCase(Locale.ROOT));
  }

  private Vertex(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public int hashCode() {
    return Objects.hash(label);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Vertex other = (Vertex) o;
    return this.label.equals(other.label);
  }

  @Override
  public String toString() {
    return String.format("[Vertex: %s]", label);
  }
}
