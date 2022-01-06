import java.awt.*;

/**
 *  @author mb1122
 *  @author RaphaelLandau
 */
public class Node {
    /**
     *  True -- Alive, False -- Dead
     */
    boolean state, nextState;
    static Node[][] world;
    final int x;
    final int y;

    public Node(int x, int y) {
        this.x = x; this.y = y;
        state = true;
    } // Set the node to be alive by default.

    /** This constructor just makes a dead node at 0, 0 without putting it in the world. **/
    public Node() { x = 0; y = 0; state = false; }

    /**
     *  This method just something for the renderer to have in order to display alive and dead nodes.
     * It shows GRAY for an alive cell and BLACK for a dead one.
     */
    public Color getColor() {
        if (state)
            return Color.GRAY;
        else return Color.BLACK;
    }

    /**
     *  This method only queues the state change up for the next tick--
     *  it's not applied until then.
     */
    private void setStateOnNextTick(boolean state) {
        this.nextState = state;
    }

    /**
     * Sets the state of the specified node.
     * @param state True for alive, false for dead.
     */
    public void setState(boolean state) { this.state = state; }
    public boolean getState() { return state; }

    /** This method just moves the state forward-- it pushes the nextState to the node. */
    private void pushToNextState() { state = nextState; }

    // This is where the actual math gets done and the cells change state.
    public void process() {
        // Get the neighbours.
        Node[] neighbours = getNeighbors();
        // Count up the number of living neighbours.
        int neighboursNumber = 0;
        for (int i = 0; i < 8; i++) {
            if (neighbours[i].getState()) {
                neighboursNumber++;
            }
        }

        // Crunch the numbers and figure out if the cell should be alive or not.
        // (Note: Comment descriptions by Wikipedia.)
            // Any live cell with fewer than two live neighbours dies, as if by underpopulation.
        if (neighboursNumber < 2) {
            setStateOnNextTick(false);

            // Any live cell with two or three live neighbours lives on to the next generation.
        } else if ((neighboursNumber == 2 | neighboursNumber == 3) & state) {
            setStateOnNextTick(true);

            // Any live cell with more than three live neighbours dies, as if by overpopulation.
        } else if (neighboursNumber > 3 & state) {
            setStateOnNextTick(false);

            // Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
                // Note, we've already done a check for cases where we've got 3 numbers and the cell is alive, we *only* have to check
                // that this cell has exactly three neighbours, since it's guaranteed to be false at this point.
        } else if (neighboursNumber == 3) {
            setStateOnNextTick(true);

            // Otherwise, stay the same.
        } else {
            // Stay the same.
            setStateOnNextTick(state);
        }
    }

    /**
     * Returns a list of the neighbors-- Organized like so: <html><br>
     * 0 1 2<br>
     * 3 x 4<br>
     * 5 6 7<br>
     * </html>
     */
    public Node[] getNeighbors() {
        Node[] neighbours = new Node[8];

        // Note: We have to try to get every single one, separately, or you might end up with a case
        // where the cells in the corners, which may have neighbors, end up thinking that they have
        // NO neighbors (and dying.)
        int reps = 0;
        for (int xpos = -1; xpos <= 1; xpos++) {
          for (int ypos = -1; ypos <= 1; ypos++) {
            if (!(xpos == 0 && ypos == 0)) {
              try {
                  neighbours[reps] = world[x + xpos][x + ypos];
              } catch (ArrayIndexOutOfBoundsException ignored) {
                  // Fill up any nodes outside the screen with dead spots.
                  neighbours[reps] = new Node();
              }
              reps++;
            }
          }
        }
      
      

        return neighbours;
    }

    /**
     *  This method sets the world data of the nodes -- it lets them find their neighbors.
     */
    public static void setWorld(Node[][] world) {
        Node.world = world;
    }

    /**
     *  This method calculates the next frame and pushes it to the array once, when called.
     */
    public static void tick() {
        // Process all the state changes, then push all the changes to the array.
        for (Node[] nodes : world) for (Node node : nodes)
            node.process();

        for (Node[] nodes : world) for (Node node : nodes)
            node.pushToNextState();

    }

    /**
     * This method returns the total number of living nodes in the world.
     */
    public static int getNumberOfLivingNodes() {
        // Loop through the world, count up the number of living nodes.
        int numberOfLivingNodes = 0;
        for (Node[] n : world) for (Node no : n)
            if (no.getState())
                numberOfLivingNodes++;

        return numberOfLivingNodes;
    }

    /**
     * This method returns the number of living nodes in a row.
     * @param Row The row to total up.
     * @return The total number of nodes that are alive in said row.
     */
    public static int getNumberOfLivingNodesByRow(int Row) {
        int numberOfLivingNodes = 0;
        Node[] row = world[Row];
        for (Node node : row) if (node.getState()) numberOfLivingNodes++;
        return numberOfLivingNodes;
    }
}