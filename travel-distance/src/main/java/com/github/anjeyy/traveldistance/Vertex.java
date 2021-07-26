package com.github.anjeyy.traveldistance;

import java.util.Locale;
import java.util.Objects;

class Vertex {

  private final String label;

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
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (getClass() != obj.getClass()) {
      return false;
    }
    Vertex other = (Vertex) obj;
    return this.label.equals(other.label);
  }

  @Override
  public String toString() {
    return String.format("[Vertex: %s]", label);
  }
}
