import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
*  @author mb1122
*  @author RaphaelLandau 
*/
class Main {
    public static void main(String[] args) {

        Node[][] entireThing = new Node[20][20];

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
            entireThing[--x][--y] = new Node(x, y);
        }

        // Fill the empty spots in the array with dead nodes.
        for (int i = 0; i < entireThing.length; i++) for (int j = 0; j < entireThing[i].length; j++) {
            if (entireThing[i][j] == null) {
                entireThing[i][j] = new Node(i, j);
                entireThing[i][j].setState(false);
            }
        }

        // Show array on screen.
        TwoDimensionalArrayDisplay.display(entireThing);
        Node.setWorld(entireThing);

        // Update the display, five times at a rate of once per second.
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
                System.out.printf("%-50s Number of Living Nodes: %2d%n", "Ticked for generation #" + (1 + i) + ".", Node.getNumberOfLivingNodes());
                Node.tick();
            } catch (InterruptedException ignored) {;}
        }
    }
}