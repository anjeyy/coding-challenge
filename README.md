# coding-challenge

Personal solutions to various coding challenges

## table of content

- [space highways](#space-highways)
  - [assignment of tasks](#assignment-of-tasks)
    - [general assumptions](#general-assumptions)
    - [sample input](#sample-input)
    - [sample queries](#sample-queries)

## space highways

In the distant future, humankind develops space highways between star systems.
But due to various reasons (like solar winds, asteroid belts and so on), these highways are not always bidirectional,
or sometimes require different travel times in each direction.
For example, travelling from Alpha Centauri to Vega takes 7 hours while the return journey only takes 3.

### assignment of tasks

The purpose of the program you’ll develop is to help spaceship navigators to find their way through space.
Tasks are to compute the travel time of a given route, the number of different routes between two-star systems,
and the shortest route between two-star systems.

You can think of star systems and routes as a directed graph:
star systems are nodes, and the routes between them are _(weighted)_ edges.

#### general assumptions

- There will be no more than one way per direction between any two-star systems.
- Space highways always connect different star systems. That is, no space highway will connect a star system to itself.
- For exercises 1—5 [below](): if there is no route, output `NO SUCH ROUTE`.

#### sample input

_Star systems_

- Solar System
- Alpha Centauri
- Sirius
- Betelgeuse
- Vega

_Space highways_

- Solar System -> Alpha Centauri: 5 Hours
- Alpha Centauri -> Sirius: 4 Hours
- Sirius -> Betelgeuse: 8 hours
- Betelgeuse -> Sirius: 8 hours
- Betelgeuse -> Vega: 6 hours
- Solar System -> Betelgeuse: 5 hours
- Sirius -> Vega: 2 hours
- Vega -> Alpha Centauri: 3 hours
- Solar System -> Vega: 7 hours

#### sample graph

![sample graph](travel-distance/docs/sample-graph.png)

#### sample queries

> todo

### design explanation

- adjacency list

> todo
