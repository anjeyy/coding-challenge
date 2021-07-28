package com.github.anjeyy.traveldistance;

import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EdgeTest {

  @ParameterizedTest
  @MethodSource("provideAnInvalidEdge")
  void givenEdge_withNullVertex_throwsException(
    Vertex firstVertex,
    Vertex secondVertex
  ) {
    // given - custom arguments

    // when
    ThrowableAssert.ThrowingCallable expectedThrow = () ->
      new Edge(firstVertex, secondVertex, 2);

    // then
    Assertions
      .assertThatThrownBy(expectedThrow)
      .isNotNull()
      .isInstanceOf(NullPointerException.class)
      .hasMessage("Vertex source and destination has to be set.");
  }

  private static Stream<Arguments> provideAnInvalidEdge() {
    return Stream.of(
      Arguments.of(null, Vertex.with("Vega")),
      Arguments.of(Vertex.with("Sirius"), null)
    );
  }

  @Test
  void givenSameVertices_creatingEdgeWithSelfReference_throwsException() {
    // given
    Vertex sirius = Vertex.with("Sirius");
    Vertex otherSirius = Vertex.with("sirIUS");

    // when
    ThrowableAssert.ThrowingCallable expectedThrow = () ->
      new Edge(sirius, otherSirius, 2);

    // then
    Assertions
      .assertThatThrownBy(expectedThrow)
      .isNotNull()
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("No self reference allowed.");
  }

  @Test
  void givenEdge_withSameVertices_isEqual() {
    // given
    Vertex sirius = Vertex.with("Sirius");
    Vertex vega = Vertex.with("Vega");

    // when
    Edge firstEdge = new Edge(sirius, vega, 2);
    Edge secondEdge = new Edge(sirius, vega, 2);
    boolean actualResult = firstEdge.equals(secondEdge);

    // then
    Assertions.assertThat(actualResult).isTrue();
  }
}
