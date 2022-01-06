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
    int x, y;

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

    /*
     *  This method only queues the state change up for the next tick--
     *  it's not applied until then.
     **/
    private void setStateOnNextTick(boolean state) {
        this.nextState = state;
    }

    public void setState(boolean state) { this.state = state; }
    public boolean getState() { return state; }

    /** This method just moves the state forward-- it pushes the nextState to the node. **/
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
        if (neighboursNumber < 2) setStateOnNextTick(false);

            // Any live cell with two or three live neighbours lives on to the next generation.
        else if ((neighboursNumber == 2 || neighboursNumber == 3) && state)
            setStateOnNextTick(true);

            // Any live cell with more than three live neighbours dies, as if by overpopulation.
        else if (neighboursNumber > 3 && state)
          setStateOnNextTick(false);
          
            // Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
        else if (neighboursNumber == 3 && !state)
          setStateOnNextTick(true);
    }

    /**
     *  Returns a list of the neighbors-- Organized like so:
     * 0 1 2
     * 3 x 4
     * 5 6 7
     **/
    public Node[] getNeighbors() {
        Node[] neighbours = new Node[8];

        // Note: We have to try to get every single one, seperately, or you might end up with a case
        // where the cells in the corners, which may have neighbors, end up thinking that they have
        // NO neighbors (and dying.)

        try {
            neighbours[0] = world[x-1][y-1];
        } catch (ArrayIndexOutOfBoundsException ignored) { ; }

        try {
            neighbours[1] = world[x  ][y-1];
        } catch (ArrayIndexOutOfBoundsException ignored) { ; }

        try {
            neighbours[2] = world[x+1][y-1];
        } catch (ArrayIndexOutOfBoundsException ignored) { ; }

        try {
            neighbours[3] = world[x-1][y  ];
        } catch (ArrayIndexOutOfBoundsException ignored) { ; }

        try {
            neighbours[4] = world[x+1][y  ];
        } catch (ArrayIndexOutOfBoundsException ignored) { ; }

        try {
            neighbours[5] = world[x-1][y+1];
        } catch (ArrayIndexOutOfBoundsException ignored) { ; }

        try {
            neighbours[6] = world[x  ][y+1];
        } catch (ArrayIndexOutOfBoundsException ignored) { ; }

        try {
            neighbours[7] = world[x+1][y+1];
        } catch (ArrayIndexOutOfBoundsException ignored) { ; }


        // Fill up any null nodes with dead nodes.
        for (int i = 0; i < neighbours.length; i++) if (neighbours[i] == null) {
            neighbours[i] = new Node();
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
        for (int i = 0; i < world.length; i++) for (int j = 0; j < world[i].length; j++)
            world[i][j].process();

        for (int i = 0; i < world.length; i++) for (int j = 0; j < world[i].length; j++)
            world[i][j].pushToNextState();

    }

    /**
    * This method returns the total number of living nodes in the world.
    **/
    public static int getNumberOfLivingNodes() {
      // Loop through the world, count up the number of living nodes.
      int numberOfLivingNodes = 0;
      for (Node[] n : world) for (Node no : n) 
        if (no.getState()) 
          numberOfLivingNodes++;

      return numberOfLivingNodes;
    }
}