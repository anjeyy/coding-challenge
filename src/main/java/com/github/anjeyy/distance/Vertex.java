package com.github.anjeyy.distance;

class Vertex {

  private final String label;

  static Vertex with(String label) {
    if (label == null) {
      throw new NullPointerException("'null' as vertex label is NOT allowed.");
    }
    return new Vertex(label);
  }

  private Vertex(String label) {
    this.label = label;
  }

  @Override
  public int hashCode() {
    //doit
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    //doit
    return super.equals(obj);
  }

  @Override
  public String toString() {
    return String.format("[Vertex: %s]", label);
  }
}
