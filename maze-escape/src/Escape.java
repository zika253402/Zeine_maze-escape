/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int rows = w.rows();
        int cols = w.cols();
        int n = w.cells();
        if (n == 0) {
            return new Result(new int[0], 0, 0);
        }

        int[][] open = Graphs.open(w, broken);
        int deadEnds = 0;
        for (int[] neighbours : open) {
            if (neighbours.length == 1) {
                deadEnds++;
            }
        }

        int exit = n - 1;
        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        java.util.Arrays.fill(parent, -1);
        int[] stack = new int[n];
        int[] nextDirection = new int[n];
        int visitedCount = 1;
        int top = 0;
        stack[0] = 0;
        visited[0] = true;

        while (top >= 0 && stack[top] != exit) {
            int cell = stack[top];
            int row = cell / cols;
            int col = cell % cols;
            boolean advanced = false;
            while (nextDirection[top] < 4) {
                int direction = nextDirection[top]++;
                int candidate;
                switch (direction) {
                    case 0 -> { if (row == 0) continue; candidate = cell - cols; }
                    case 1 -> { if (col + 1 >= cols) continue; candidate = cell + 1; }
                    case 2 -> { if (row + 1 >= rows) continue; candidate = cell + cols; }
                    default -> { if (col == 0) continue; candidate = cell - 1; }
                }

                boolean isOpen = false;
                for (int neighbour : open[cell]) {
                    if (neighbour == candidate) {
                        isOpen = true;
                        break;
                    }
                }
                if (isOpen && !visited[candidate]) {
                    visited[candidate] = true;
                    visitedCount++;
                    parent[candidate] = cell;
                    stack[++top] = candidate;
                    nextDirection[top] = 0;
                    advanced = true;
                    break;
                }
            }
            if (!advanced && nextDirection[top] == 4) {
                top--;
            }
        }

        if (!visited[exit]) {
            return new Result(new int[0], visitedCount, deadEnds);
        }
        int pathLength = 1;
        for (int cell = exit; cell != 0; cell = parent[cell]) {
            pathLength++;
        }
        int[] path = new int[pathLength];
        int cell = exit;
        for (int i = pathLength - 1; i >= 0; i--) {
            path[i] = cell;
            cell = parent[cell];
        }
        return new Result(path, visitedCount, deadEnds);
    }
}
