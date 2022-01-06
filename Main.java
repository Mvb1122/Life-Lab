import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *  @author mb1122
 *  @author RaphaelLandau
 */
class Main {
    public static void main(String[] args) {

        Node[][] world = new Node[20][20];

        Scanner s;
        try {
            s = new Scanner(new File("life100.txt"));
        } catch (FileNotFoundException e) {return;} // Won't happen, but we need this for insurance reasons.

        // Remove first number.
        s.nextLine();

        while (s.hasNextLine()) {
            int x = s.nextInt();
            int y = s.nextInt();

            // Note: The text file is 1-indexed, so we have to shift it down and over by one.
            // Note the use of decrement operators-- that's why the indexes on the start of the line are decremented, but the ones on the later half (which have already been decremented,) don't have them.
            world[--x][--y] = new Node(x, y);
        }

        // Fill the empty spots in the array with dead nodes.
        for (int i = 0; i < world.length; i++) for (int j = 0; j < world[i].length; j++) {
            if (world[i][j] == null) {
                world[i][j] = new Node(i, j);
                world[i][j].setState(false);
            }
        }

        // Show array on screen.
        TwoDimensionalArrayDisplay.display(world);
            // Tie the array into the simulation
        Node.setWorld(world);

        // Update the display, five times at a rate of once per second.
        System.out.printf("Started, number of Living Nodes: %2d.%n", Node.getNumberOfLivingNodes());
        for (int i = 1; i <= 5; i++) {
            try {
                Thread.sleep(1000);
                Node.tick();
                System.out.printf("%-50s Number of Living Nodes: %2d%n", "Ticked for generation #" + (i) + ".", Node.getNumberOfLivingNodes());
            } catch (InterruptedException ignored) {;}
        }
    }
}