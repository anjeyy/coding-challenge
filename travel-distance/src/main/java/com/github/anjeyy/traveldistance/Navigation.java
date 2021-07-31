package com.github.anjeyy.traveldistance;

import com.github.anjeyy.traveldistance.util.StringConstant;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class Navigation {

  private static final String HORIZONTAL_LINE =
    StringConstant.NEW_LINE.getValue() +
    "------------------------------------" +
    StringConstant.NEW_LINE.getValue();

  private static final Scanner input = new Scanner(System.in);
  private static final DirectedWeightedGraph graph = DirectedWeightedGraph.create();

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      throw new IllegalArgumentException(
        "Please provide ONLY a path to the graph file as an argument."
      );
    }
    System.out.println(HORIZONTAL_LINE);
    System.out.println(
      "Starting Navigation..." + StringConstant.NEW_LINE.getValue()
    );
    initializeGraph(args[0]);
    System.out.println(HORIZONTAL_LINE);

    Mode currentMode = Mode.START_PROGRAM;
    while (currentMode != Mode.EXIT_PROGRAM) {
      System.out.println(
        "Please enter your mode with corresponding and appropriate parameters: "
      );
      int rawMode = input.nextInt();
      currentMode = Mode.from(rawMode);

      if (currentMode != Mode.EXIT_PROGRAM) {
        String userInput = input.nextLine().trim();
        checkUserInput(userInput, currentMode);
        String result = currentMode.compute(graph, userInput);
        System.out.println(
          "MODE: " + currentMode + StringConstant.NEW_LINE.getValue()
        );
        System.out.println(result);
        System.out.println(HORIZONTAL_LINE);
      }
    }
    System.out.println("Navigation closed...");
  }

  private static void initializeGraph(String filePathForGraph)
    throws IOException {
    File graphFile = new File(filePathForGraph);
    try (
      Stream<String> fileStream = Files.lines(
        graphFile.getAbsoluteFile().toPath()
      )
    ) {
      fileStream
        .map(String::trim)
        .filter(str -> !str.isBlank())
        .map(Navigation::transform)
        .forEach(graph::addEdge);
    }
    System.out.println(graph);
  }

  private static Edge transform(String rawLine) {
    String[] rawEdgeInput = rawLine.split(StringConstant.COMMA.getValue());
    if (rawEdgeInput.length != 3) {
      throw new IllegalArgumentException(
        "Graph file is not appropriately formatted like 'source, destination, X' - but instead it was: " +
        Arrays.toString(rawEdgeInput)
      );
    }
    Vertex source = Vertex.with(rawEdgeInput[0].trim());
    Vertex destination = Vertex.with(rawEdgeInput[1].trim());
    int weight = Integer.parseInt(rawEdgeInput[2].trim());
    return new Edge(source, destination, weight);
  }

  private static void checkUserInput(String userInput, Mode currentMode) {
    if (userInput.isBlank()) {
      throw new IllegalArgumentException(
        String.format(
          "Please provide valid parameters for corresponding mode '%s'.",
          currentMode
        )
      );
    }
  }
}
