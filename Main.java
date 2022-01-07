import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *  @author mb1122
 *  @author RaphaelLandau
 */
class Main {
    public static int genNum = 1;
    public static boolean paused;

    public static void main(String[] args) {
        Node.load(new File("life100.txt"));


            // Open infoPanel
        ControlPanel ip1 = new ControlPanel();

        // Update the display, five times at a rate of once per second.
        System.out.printf("Started, number of Living Nodes: %2d.%n", Node.getNumberOfLivingNodes());
        while (true) {
            try {
                Thread.sleep(1000);
                if (!paused) {
                    Node.tick();
                    System.out.printf("Ticked for generation #%d:  Number of Living Nodes: %-2d Number of Nodes in Row 10: %-2d %n", genNum, Node.getNumberOfLivingNodes(), Node.getNumberOfLivingNodesByRow(11));
                    genNum++;
                }
            } catch (InterruptedException ignored) {;}
        }
    }
}