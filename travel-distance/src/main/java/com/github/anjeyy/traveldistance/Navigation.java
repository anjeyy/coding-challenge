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
    "\n------------------------------------\n";

  private static final Scanner input = new Scanner(System.in);
  private static final DirectedWeightedGraph graph = DirectedWeightedGraph.create();

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      throw new IllegalArgumentException(
        "Please provide ONLY a path to the graph file as an argument."
      );
    }
    System.out.println(HORIZONTAL_LINE);
    System.out.println("Starting Navigation...");
    initializeGraph(args[0]);
    System.out.println(HORIZONTAL_LINE);

    Mode currentMode = Mode.START_PROGRAM;
    while (currentMode != Mode.EXIT_PROGRAM) {
      System.out.println(
        "Please enter your mode with corresponding and appropriate parameters: "
      );
      int rawMode = input.nextInt();
      System.out.println("######## -> " + rawMode); //doit remove
      currentMode = Mode.from(rawMode);
      String userInput = input.nextLine();
      if (userInput.isBlank()) {
        throw new IllegalArgumentException(
          "Please provide valid parameters for corresponding mode."
        );
      }
      System.out.println("######## -> " + userInput); //doit remove
      //doit interpret different modes

      System.out.println(HORIZONTAL_LINE);
    }
  }

  private static void initializeGraph(String filePathForGraph)
    throws IOException {
    File graphFile = new File(filePathForGraph);
    try (
      Stream<String> fileStream = Files.lines(
        graphFile.getAbsoluteFile().toPath()
      )
    ) {
      fileStream.map(Navigation::transform).forEach(graph::addEdge);
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

  private enum Mode {
    START_PROGRAM(0),
    TRAVEL_TIME_CERTAIN_ROUTE(1),
    FIND_ROUTES_WITH_MAX_STOPS(2),
    FIND_ROUTES_WITH_EXACT_STOPS(3),
    TRAVEL_TIME_SHORTEST_PATH(4),
    TRAVEL_TIME_WITH_MAX_TIME(5),
    EXIT_PROGRAM(6);

    private final int number;

    Mode(int number) {
      this.number = number;
    }

    public int getNumber() {
      return number;
    }

    private static Mode from(int number) {
      return Arrays
        .stream(values())
        .filter(m -> m.getNumber() == number)
        .filter(m -> m != START_PROGRAM)
        .findAny()
        .orElseThrow(
          () ->
            new IllegalArgumentException(
              "Please provide valid number from 1-6, yours was: " + number
            )
        );
    }
    //    abstract void interpret(String rawInput); doit either this or other solution
  }
}
