package com.github.anjeyy.traveldistance;

import com.github.anjeyy.traveldistance.util.StringConstant;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NavigationTest {

  @BeforeAll
  static void setup() {
    // simulating userInput
    String rawUserInput = //order 1 is ignored - no user input necessary
      "1\n2\n3\n4\n5" + // order 2
      "\n6 "; // order 3

    ByteArrayInputStream userInput = new ByteArrayInputStream(
      rawUserInput.getBytes()
    );
    System.setIn(userInput);
  }

  @Order(1)
  @ParameterizedTest
  @ValueSource(strings = { "a,b", "a,b,c" })
  void givenTooManyParameters_startingProgram_throwsException(String input) {
    // given
    String[] args = input.split(StringConstant.COMMA.getValue());

    // when
    ThrowableAssert.ThrowingCallable expectedThrow = () ->
      Navigation.main(args);

    // then
    Assertions
      .assertThatThrownBy(expectedThrow)
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage(
        "Please provide ONLY a path to the graph file as an argument."
      );
  }

  @Order(3)
  @Test
  void givenGraphFile_startingProgram_initializesCorrectly()
    throws IOException {
    // given
    String filePath = "src/main/resources/space-highway.graph";
    String existProgramValue = String.valueOf(Mode.EXIT_PROGRAM.getNumber());
    ByteArrayInputStream userExitProgram = new ByteArrayInputStream(
      existProgramValue.getBytes()
    );

    // when-then
    System.setIn(userExitProgram);
    Navigation.main(new String[] { filePath });
  }

  @Order(2)
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  void givenParametersWithOnlyMode_startingProgram_throwsException(int mode) {
    // given
    String filePath = "src/main/resources/space-highway.graph";

    // when
    ThrowableAssert.ThrowingCallable expectedThrow = () ->
      Navigation.main(new String[] { filePath });

    // then
    Assertions
      .assertThatThrownBy(expectedThrow)
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage(
        String.format(
          "Please provide valid parameters for corresponding mode '%s'.",
          Mode.from(mode)
        )
      );
  }
}
