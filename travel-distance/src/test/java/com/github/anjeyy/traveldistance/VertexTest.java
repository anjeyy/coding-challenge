package com.github.anjeyy.traveldistance;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class VertexTest {

  @Test
  void givenVertex_withIdenticalObject_isEqual() {
    // given
    Vertex sirius = Vertex.with("Sirius");

    // when
    boolean actualResult = sirius.equals(sirius);

    // then
    Assertions.assertThat(actualResult).isTrue();
  }

  @Test
  void givenVertex_withSameLabelAndAnotherCase_isEqual() {
    // given
    Vertex sirius = Vertex.with("Sirius");
    Vertex otherSirius = Vertex.with("sirIUS");

    // when
    boolean actualResult = sirius.equals(otherSirius);

    // then
    Assertions.assertThat(actualResult).isTrue();
  }

  @ParameterizedTest
  @EmptySource
  @ValueSource(strings = { "Solar System", "Vega", " " })
  void givenVertex_withSameLabel_isEqual(String input) {
    // given
    Vertex solarSystem = Vertex.with(input);
    Vertex otherSolarSystem = Vertex.with(input);

    // when
    boolean actualResult = solarSystem.equals(otherSolarSystem);

    // then
    Assertions.assertThat(actualResult).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(classes = { String.class, Integer.class, Double.class })
  void givenVertex_withAnotherType_isNotEqual(Class<?> input) {
    // given
    Vertex vega = Vertex.with("Vega");

    // when
    boolean actualResult = vega.equals(input);

    // then
    Assertions.assertThat(actualResult).isFalse();
  }

  @Test
  void givenVertex_withSameLabelAndAnotherCase_isHashCodeEqual() {
    // given
    Vertex sirius = Vertex.with("Sirius");
    Vertex otherSirius = Vertex.with("sirIUS");

    // when
    int actualResult = sirius.hashCode();
    int expectedResult = otherSirius.hashCode();

    // then
    Assertions.assertThat(actualResult).isEqualTo(expectedResult);
  }
}
