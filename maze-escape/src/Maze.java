/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        int n = w.cells();
        int[] broken = new int[Math.max(0, n - 1)];
        int used = 0;
        DisjointSet sets = new DisjointSet(n);

        for (int k = 0; k < w.count() && used < broken.length; k++) {
            if (sets.union(w.a()[k], w.b()[k])) {
                broken[used++] = k;
            }
        }
        return java.util.Arrays.copyOf(broken, used);
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        int n = w.cells();
        int[] broken = new int[Math.max(0, n - 1)];
        Integer[] order = new Integer[w.count()];
        for (int k = 0; k < order.length; k++) {
            order[k] = k;
        }
        java.util.Arrays.sort(order, (x, y) -> {
            int byCost = Integer.compare(w.cost()[x], w.cost()[y]);
            return byCost != 0 ? byCost : Integer.compare(x, y);
        });

        int used = 0;
        DisjointSet sets = new DisjointSet(n);
        for (int k : order) {
            if (used == broken.length) {
                break;
            }
            if (sets.union(w.a()[k], w.b()[k])) {
                broken[used++] = k;
            }
        }
        return java.util.Arrays.copyOf(broken, used);
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int n = w.cells();
        if (n == 0) {
            return 0;
        }

        int[] degree = new int[n];
        for (int k = 0; k < w.count(); k++) {
            degree[w.a()[k]]++;
            degree[w.b()[k]]++;
        }
        int[] start = new int[n + 1];
        for (int cell = 0; cell < n; cell++) {
            start[cell + 1] = start[cell] + degree[cell];
        }
        int[] next = start.clone();
        int[] incidentWalls = new int[start[n]];
        for (int k = 0; k < w.count(); k++) {
            incidentWalls[next[w.a()[k]]++] = k;
            incidentWalls[next[w.b()[k]]++] = k;
        }

        java.util.PriorityQueue<Integer> frontier = new java.util.PriorityQueue<>((x, y) -> {
            int byCost = Integer.compare(w.cost()[x], w.cost()[y]);
            return byCost != 0 ? byCost : Integer.compare(x, y);
        });
        boolean[] visited = new boolean[n];
        visited[0] = true;
        int visitedCount = 1;
        long totalCost = 0;
        addFrontier(0, w, start, incidentWalls, visited, frontier);

        while (!frontier.isEmpty() && visitedCount < n) {
            int k = frontier.remove();
            int a = w.a()[k], b = w.b()[k];
            if (visited[a] && visited[b]) {
                continue;
            }
            int cell = visited[a] ? b : a;
            visited[cell] = true;
            visitedCount++;
            totalCost += w.cost()[k];
            addFrontier(cell, w, start, incidentWalls, visited, frontier);
        }
        return totalCost;
    }

    private static void addFrontier(int cell, Input.Walls w, int[] start, int[] incidentWalls,
                                    boolean[] visited, java.util.PriorityQueue<Integer> frontier) {
        for (int i = start[cell]; i < start[cell + 1]; i++) {
            int k = incidentWalls[i];
            int other = w.a()[k] == cell ? w.b()[k] : w.a()[k];
            if (!visited[other]) {
                frontier.add(k);
            }
        }
    }
}
