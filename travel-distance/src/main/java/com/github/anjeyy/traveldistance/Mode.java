package com.github.anjeyy.traveldistance;

import com.github.anjeyy.traveldistance.util.StringConstant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

enum Mode {
  START_PROGRAM(0) {
    @Override
    String compute(DirectedWeightedGraph graph, String rawInput) {
      throw new UnsupportedOperationException("Not allowed for program start.");
    }
  },
  TRAVEL_TIME_CERTAIN_ROUTE(1) {
    @Override
    String compute(DirectedWeightedGraph graph, String rawInput) {
      String[] splitInput = rawInput.split(StringConstant.COMMA.getValue());
      List<Vertex> routeInput = Arrays
        .stream(splitInput)
        .map(String::trim)
        .filter(str -> !str.isBlank())
        .map(Vertex::with)
        .collect(Collectors.toList());
      return graph.travelTimeForGivenRoute(routeInput);
    }
  },
  FIND_ROUTES_WITH_MAX_STOPS(2) {
    @Override
    String compute(DirectedWeightedGraph graph, String rawInput) {
      String[] rawEdgeInput = rawInput.split(StringConstant.COMMA.getValue());
      if (rawEdgeInput.length != 3) {
        throw new IllegalArgumentException(
          String.format(
            "MODE '%s' need source, destination and maxStop parameters. Yours was: '%s'.",
            FIND_ROUTES_WITH_MAX_STOPS,
            rawInput
          )
        );
      }
      Vertex source = Vertex.with(rawEdgeInput[0].trim());
      Vertex destination = Vertex.with(rawEdgeInput[1].trim());
      int maxStop = Integer.parseInt(rawEdgeInput[2].trim());
      return graph.determineAndDisplayRoutesWithMaxStops(
        source,
        destination,
        maxStop
      );
    }
  },
  FIND_ROUTES_WITH_EXACT_STOPS(3) {
    @Override
    String compute(DirectedWeightedGraph graph, String rawInput) {
      String[] rawEdgeInput = rawInput.split(StringConstant.COMMA.getValue());
      if (rawEdgeInput.length != 3) {
        throw new IllegalArgumentException(
          String.format(
            "MODE '%s' need source, destination and exactStops parameters. Yours was: '%s'.",
            FIND_ROUTES_WITH_EXACT_STOPS,
            rawInput
          )
        );
      }
      Vertex source = Vertex.with(rawEdgeInput[0].trim());
      Vertex destination = Vertex.with(rawEdgeInput[1].trim());
      int exactStop = Integer.parseInt(rawEdgeInput[2].trim());
      return graph.determineAndDisplayRoutesWithExactlyStops(
        source,
        destination,
        exactStop
      );
    }
  },
  TRAVEL_TIME_SHORTEST_PATH(4) {
    @Override
    String compute(DirectedWeightedGraph graph, String rawInput) {
      String[] rawEdgeInput = rawInput.split(StringConstant.COMMA.getValue());
      if (rawEdgeInput.length != 2) {
        throw new IllegalArgumentException(
          String.format(
            "MODE '%s' need source and destination. Yours was: '%s'.",
            TRAVEL_TIME_SHORTEST_PATH,
            rawInput
          )
        );
      }
      Vertex source = Vertex.with(rawEdgeInput[0].trim());
      Vertex destination = Vertex.with(rawEdgeInput[1].trim());
      return graph.calculateShortestRoute(source, destination);
    }
  },
  TRAVEL_TIME_WITH_MAX_TIME(5) {
    @Override
    String compute(DirectedWeightedGraph graph, String rawInput) {
      String[] rawEdgeInput = rawInput.split(StringConstant.COMMA.getValue());
      if (rawEdgeInput.length != 3) {
        throw new IllegalArgumentException(
          String.format(
            "MODE '%s' need source, destination and maxTime parameters. Yours was: '%s'.",
            TRAVEL_TIME_WITH_MAX_TIME,
            rawInput
          )
        );
      }
      Vertex source = Vertex.with(rawEdgeInput[0].trim());
      Vertex destination = Vertex.with(rawEdgeInput[1].trim());
      int maxTime = Integer.parseInt(rawEdgeInput[2].trim());
      return graph.determineAndDisplayRoutesWithMaxTime(
        source,
        destination,
        maxTime
      );
    }
  },
  EXIT_PROGRAM(6) {
    @Override
    String compute(DirectedWeightedGraph graph, String rawInput) {
      throw new UnsupportedOperationException(
        "Not allowed for exiting the program."
      );
    }
  };

  private final int number;

  Mode(int number) {
    this.number = number;
  }

  final int getNumber() {
    return number;
  }

  static Mode from(int number) {
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

  abstract String compute(DirectedWeightedGraph graph, String rawInput);
}
