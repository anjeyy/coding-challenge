package com.github.anjeyy.traveldistance;

import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

  @ParameterizedTest
  @MethodSource("differentRoutes")
  void givenSeveralRoutes_computeTravelTime_correctly(
    List<Vertex> route,
    String expected
  ) {
    // given
    DirectedWeightedGraph graph = constructSpaceHighways();

    // when
    String actual = graph.travelTimeForGivenRoute(route);

    // then
    Assertions.assertThat(actual).isNotBlank().isEqualTo(expected);
  }

  private static Stream<Arguments> differentRoutes() {
    return Stream.of(
      Arguments.of(
        List.of(
          Vertex.with("Solar System"),
          Vertex.with("Alpha Centauri"),
          Vertex.with("Sirius")
        ),
        "9 hours"
      ),
      Arguments.of(
        List.of(Vertex.with("Solar System"), Vertex.with("Betelgeuse")),
        "5 hours"
      ),
      Arguments.of(
        List.of(
          Vertex.with("Solar System"),
          Vertex.with("Betelgeuse"),
          Vertex.with("Sirius")
        ),
        "13 hours"
      ),
      Arguments.of(
        List.of(
          Vertex.with("Solar System"),
          Vertex.with("Vega"),
          Vertex.with("Alpha Centauri"),
          Vertex.with("Sirius"),
          Vertex.with("Betelgeuse")
        ),
        "22 hours"
      ),
      Arguments.of(
        List.of(
          Vertex.with("Solar System"),
          Vertex.with("Vega"),
          Vertex.with("Betelgeuse")
        ),
        "NO SUCH ROUTE"
      )
    );
  }

  // ### H E L P E R ###

  private DirectedWeightedGraph constructSpaceHighways() {
    DirectedWeightedGraph graph = DirectedWeightedGraph.create();
    createEdges().forEach(graph::addEdge);
    return graph;
  }

  private List<Edge> createEdges() {
    return List.of(
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
  }
}
