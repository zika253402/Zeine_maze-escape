# Assignment 3. Maze Escape

Student ID: 253402
Name: Zeine Kambarbek
Group: SE-2510
Maze size (rows x columns): 10 x 12

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment3.pdf`.

## Program output

The 13 lines your program prints on your data (`java -cp out Main`):

    Broken: 119
    Kept:99
    Last broken wall: 187
    MST cost: 31713
    Heaviest broken wall:714
    Path length: 22
    DFS visited: 114
    Dead ends: 37
    Zones: 11
    Largest zone: 18
    Is DAG: 0
    Flow stages: 6
    Fastest route cost: 21

My maze:

![maze](output/maze.png)

## Questions

Mission 0. `find` returns the representative of the set containing an element, while `union` joins two different sets. Path compression shortens paths during `find`, and union by size attaches the smaller tree under the larger one; together these make operations nearly constant time in practice.

Mission 1. The broken walls connect all R*C cells without making cycles, so they form a spanning tree and contain R*C - 1 walls. The remaining walls number 2RC - R - C - (RC - 1) = (R - 1)(C - 1), since a rectangular grid has 2RC - R - C internal walls.

Mission 2. Kruskal and Prim both give an MST cost of 31713. They choose edges in different orders, but both produce a minimum spanning tree, so their total costs are equal.

Mission 3. Mission 1 leaves a spanning tree, and a tree has exactly one path between any two cells. The search uses an explicit stack with a saved next-direction index instead of recursive calls, so a large maze does not overflow the call stack.

Mission 4. Kosaraju's second pass uses the reversed graph so that each search from the next unassigned cell collects exactly one strongly connected component. There are no self-slides in this assignment, so any directed cycle containing multiple cells puts them in the same zone; therefore the graph is acyclic exactly when every cell has its own zone.

Mission 5. The zone numbers are in topological order, so when a zone is processed, all earlier zones that can reach it have already updated its values. One forward dynamic-programming pass computes the longest chain and cheapest route; after building the zone edges, it takes O(Z + E_z) time and O(Z + E_z) space, where Z is the number of zones and E_z is the number of edges between zones.

## AI use

ChatGPT helped explain the assignment and implement Missions 0–5. No external code or text sources were used.

## Timing (Mission 7)

Milliseconds, 5 runs each, from `java -cp out Bench`.

| maze         | Kruskal                             | Prim                                |
|--------------|-------------------------------------|-------------------------------------|
| 100 x 100    | 13 7 5 16 17  avg 12                | 21 5 4 6 5  avg 8                   |
| 300 x 300    | 103 85 57 64 46  avg 71             | 60 62 57 56 60  avg 59              |
| 1000 x 1000  | 1085 1060 1157 1497 1567  avg 1273  | 1984 2078 2712 2992 2912  avg 2535  |

What do the numbers say about how the time grows? Two or three sentences.

## Complexity

n is the number of cells, m the number of walls (or slides). Fill in every algorithm you wrote.

| algorithm               | time                            | space       | assumptions                                                |
|-------------------------|---------------------------------|-------------|------------------------------------------------------------|
| DSU find and union      | amortized O(α(n)) per operation | O(n)        | path compression and union by size                         |
| Kruskal                 | O(m log m)                      | O(n + m)    | walls sorted by cost; connected graph                      |
| Prim                    | O((n + m) log m)                | O(n + m)    | priority queue; connected graph                            |
| DFS                     | O(n + m)                        | O(n + m)    | maze adjacency built from the broken walls                 |
| Kosaraju                | O(n + m)                        | O(n + m)    | directed slide graph and its reverse                       |
| Route in the zone graph | expected O(m + Z + E_z)         | O(Z + E_z)  | hash maps for zone edges; zones are topologically numbered |

## Before you submit

- data/walls.txt, data/slides.txt and output/maze.png are in the repository. 
- `java tools/Check.java` shows all examples right.
- This README is filled in, the program output and the section AI use included.
- The work is pushed to main (at least one commit per mission) and the repository is public.
- The link is attached in Moodle.
