package com.github.anjeyy.traveldistance;

import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

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

  @ParameterizedTest
  @ValueSource(ints = { -1, 0 })
  void givenGraph_withInvalidStop_throwsException(int maxStops) {
    DirectedWeightedGraph graph = constructSpaceHighways();

    // when
    ThrowableAssert.ThrowingCallable expectedThrow = () ->
      graph.determineAndDisplayRoutesWithMaxStops(
        Vertex.with("Vega"),
        Vertex.with("Alpha Centauri"),
        maxStops
      );

    // then
    Assertions
      .assertThatThrownBy(expectedThrow)
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Please provide max stops >0.");
  }

  @ParameterizedTest
  @MethodSource("fakeRoutes")
  void givenGraph_withForeignVertices_findsNoRoute(
    Vertex source,
    Vertex destination
  ) {
    DirectedWeightedGraph graph = constructSpaceHighways();

    // when
    String actual = graph.determineAndDisplayRoutesWithMaxStops(
      source,
      destination,
      5
    );
    String expected = "NO SUCH ROUTE";

    // then
    Assertions.assertThat(actual).isNotBlank().isEqualTo(expected);
  }

  private static Stream<Arguments> fakeRoutes() {
    return Stream.of(
      Arguments.of(Vertex.with("fakeOne"), Vertex.with("fakeTwo")),
      Arguments.of(Vertex.with("fakeOne"), Vertex.with("fakeOne")),
      Arguments.of(Vertex.with("Sirius"), Vertex.with("Solar Systems"))
    );
  }

  @Test
  void givenGraph_withThreeMaxStop_findsRoute() {
    // given
    DirectedWeightedGraph graph = constructSpaceHighways();

    // when
    String actual = graph.determineAndDisplayRoutesWithMaxStops(
      Vertex.with("Sirius"),
      Vertex.with("Sirius"),
      3
    );
    String expected =
      "2 routes: \n" +
      "[[Vertex: sirius], [Vertex: betelgeuse], [Vertex: sirius]]\n" +
      "[[Vertex: sirius], [Vertex: vega], [Vertex: alpha centauri], [Vertex: sirius]]";

    // then
    Assertions.assertThat(actual).isNotBlank().isEqualTo(expected);
  }

  @Test
  void givenGraph_withOneMaxStop_findsRoute() {
    // given
    DirectedWeightedGraph graph = constructSpaceHighways();

    // when
    String actual = graph.determineAndDisplayRoutesWithMaxStops(
      Vertex.with("Sirius"),
      Vertex.with("Vega"),
      1
    );
    String expected = "1 routes: \n" + "[[Vertex: sirius], [Vertex: vega]]";

    // then
    Assertions.assertThat(actual).isNotBlank().isEqualTo(expected);
  }

  @ParameterizedTest
  @ValueSource(ints = { -2, -1 })
  void givenGraph_withInvalidExactStop_throwsException(int exactStop) {
    DirectedWeightedGraph graph = constructSpaceHighways();

    // when
    ThrowableAssert.ThrowingCallable expectedThrow = () ->
      graph.determineAndDisplayRoutesWithExactlyStops(
        Vertex.with("Vega"),
        Vertex.with("Alpha Centauri"),
        exactStop
      );

    // then
    Assertions
      .assertThatThrownBy(expectedThrow)
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Please provide in between stop count >=0.");
  }

  @Test
  void givenGraph_withThreeInBetweenStops_findsRoutes() {
    // given
    DirectedWeightedGraph graph = constructSpaceHighways();

    // when
    String actual = graph.determineAndDisplayRoutesWithExactlyStops(
      Vertex.with("Solar System"),
      Vertex.with("Sirius"),
      3
    );
    String expected =
      "3 routes: \n" +
      "[[Vertex: solar system], [Vertex: alpha centauri], [Vertex: sirius], [Vertex: betelgeuse], [Vertex: sirius]]\n" +
      "[[Vertex: solar system], [Vertex: betelgeuse], [Vertex: sirius], [Vertex: betelgeuse], [Vertex: sirius]]\n" +
      "[[Vertex: solar system], [Vertex: betelgeuse], [Vertex: vega], [Vertex: alpha centauri], [Vertex: sirius]]";

    // then
    Assertions.assertThat(actual).isNotBlank().isEqualTo(expected);
  }

  @Test
  void givenGraph_withZeroInBetweenStops_findsRoutes() {
    // given
    DirectedWeightedGraph graph = constructSpaceHighways();

    // when
    String actual = graph.determineAndDisplayRoutesWithExactlyStops(
      Vertex.with("Solar System"),
      Vertex.with("Alpha Centauri"),
      0
    );
    String expected =
      "1 routes: \n[[Vertex: solar system], [Vertex: alpha centauri]]";

    // then
    Assertions.assertThat(actual).isNotBlank().isEqualTo(expected);
  }

  @Test
  void givenGraph_withInvalidExactRoutes_findsNoRoutes() {
    // given
    DirectedWeightedGraph graph = constructSpaceHighways();

    // when
    String actual = graph.determineAndDisplayRoutesWithExactlyStops(
      Vertex.with("Solar System"),
      Vertex.with("Sirius"),
      0
    );
    String expected = "NO SUCH ROUTE";

    // then
    Assertions.assertThat(actual).isNotBlank().isEqualTo(expected);
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
