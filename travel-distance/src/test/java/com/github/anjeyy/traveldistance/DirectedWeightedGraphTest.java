package com.github.anjeyy.traveldistance;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class DirectedWeightedGraphTest {

  @Test
  void givenGraph_withDifferentVertex_isNotEqual() {
    // given
    Vertex sirius = Vertex.with("Sirius");
    Vertex vega = Vertex.with("Vega");
    DirectedWeightedGraph firstGraph = DirectedWeightedGraph.create();
    DirectedWeightedGraph secondGraph = DirectedWeightedGraph.create();

    // when
    firstGraph.addVertex(sirius);
    secondGraph.addVertex(vega);
    boolean isEqual = firstGraph.equals(secondGraph);

    // then
    Assertions.assertThat(isEqual).isFalse();
  }

  @Test
  void givenGraph_withSameVertex_isEqual() {
    // given
    Vertex sirius = Vertex.with("Sirius");
    DirectedWeightedGraph firstGraph = DirectedWeightedGraph.create();
    DirectedWeightedGraph secondGraph = DirectedWeightedGraph.create();

    // when
    firstGraph.addVertex(sirius);
    secondGraph.addVertex(sirius);
    boolean isEqual = firstGraph.equals(secondGraph);

    // then
    Assertions.assertThat(isEqual).isTrue();
  }

  @Test
  void givenGraph_withSameEdge_isEqual() {
    // given
    Vertex sirius = Vertex.with("Sirius");
    Vertex vega = Vertex.with("Vega");
    Edge siriusToVega = new Edge(sirius, vega, 2);
    DirectedWeightedGraph firstGraph = DirectedWeightedGraph.create();
    DirectedWeightedGraph secondGraph = DirectedWeightedGraph.create();

    // when
    firstGraph.addVertex(sirius);
    firstGraph.addEdge(siriusToVega);
    secondGraph.addEdge(siriusToVega);
    boolean isEqual = firstGraph.equals(secondGraph);

    // then
    Assertions.assertThat(isEqual).isTrue();
  }

  @Test
  void givenGraph_withSameEdgeAndDifferentVertex_isNotEqual() {
    // given
    Vertex sirius = Vertex.with("Sirius");
    Vertex vega = Vertex.with("Vega");
    Edge siriusToVega = new Edge(sirius, vega, 2);
    DirectedWeightedGraph firstGraph = DirectedWeightedGraph.create();
    DirectedWeightedGraph secondGraph = DirectedWeightedGraph.create();

    // when
    firstGraph.addEdge(siriusToVega);
    secondGraph.addEdge(siriusToVega);
    firstGraph.addVertex(sirius);
    secondGraph.addVertex(vega);
    boolean isEqual = firstGraph.equals(secondGraph);

    // then
    Assertions.assertThat(isEqual).isFalse();
  }
  //doit travel time parameterized test
}
