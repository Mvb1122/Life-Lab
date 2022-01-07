import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ControlPanel {
  private final String[] labels = {"Generation Number", "Number of Living Nodes", "Number of Living Nodes in Row 10", "Number of Living Nodes in Column 10"};
  private final JTextArea jta;
  private JButton[] buttons;
  private JFrame infoPanel;

  /**
  *  Starts up and displays the simulation's information.
  */
  public ControlPanel() {
    infoPanel = new JFrame("Statistics for the Game of Life");
    jta = new JTextArea("Waiting for Simulation to begin.");
    infoPanel.add(jta);
    infoPanel.setBounds(5, 250, 300, labels.length * 50);
    infoPanel.setVisible(true);
    infoPanel.setLayout(null);
    infoPanel.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    // Create the play/pause button.
      // Assign it.
    buttons = new JButton[3];
    buttons[0] =  new JButton("Play/Pause");

      // Give it the actions.
    buttons[0].addActionListener(e -> Main.paused = !Main.paused);

    // Create the load button.
    buttons[1] = new JButton("Load");
    buttons[1].addActionListener((e -> {
      // Create a window to have the user choose a file.
      JFileChooser fc = new JFileChooser();
      int returnedValue = fc.showOpenDialog(infoPanel);

      // Write that file when the value is returned.
      if (returnedValue == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fc.getSelectedFile();
        Node.load(selectedFile);
      }
    }));

    // Create the save button.
    buttons[2] = new JButton("Save");
    buttons[2].addActionListener((e -> {
      // Create a window to have the user choose a file.
      JFileChooser fc = new JFileChooser();
      int returnedValue = fc.showSaveDialog(infoPanel);

      // Write that file when the value is returned.
      if (returnedValue == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fc.getSelectedFile();
        System.out.println("Writing to... " + selectedFile.getAbsolutePath());

        // Create the data to write.
        String outputFile = String.valueOf(Node.getNumberOfLivingNodes());
          // Loop through all nodes.
        for (int i = 0; i < Node.world.length; i++) for (int j = 0; j < Node.world[i].length; j++) {
          // Only write the active nodes.
          if (Node.world[j][i].state)
            // Append to file-to-be-written.
            outputFile += "\n\t" + (Node.world[j][i].y + 1) + " " + (Node.world[j][i].x + 1);
        }

        System.out.println("Writing: " + outputFile);

        try {
          FileWriter fl = new FileWriter(selectedFile.getAbsolutePath());
          fl.write(outputFile);
          fl.close();
          System.out.println("Finished writing.");
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }));



    // Add the buttons to the screen.
    for (JButton j : buttons) infoPanel.add(j);

    // Set the text
    refresh();

    // Start up a thread to reset the text
    Thread t = new Thread(() -> {
      while (true) {
        try {
          Thread.sleep(16);
          refresh();
        } catch (InterruptedException ignored) {;}
      }
    }, "ControlPanel rendering thread");
    t.start();
  }

  int buttonHeight = 50;

  public void refresh() {
    int[] data = new int[]{Main.genNum - 1, Node.getNumberOfLivingNodes(), Node.getNumberOfLivingNodesByRow(11), Node.getNumberOfLivingNodesByColumn(11)};
    String text = "";
    for (int i = 0; i < data.length; i++) {
      text += labels[i] + ": " + data[i] + '\n';
    }
    jta.setText(text);

    // Set the bounds of the text area.
    int buttonWidth = (int) ((1d / buttons.length) * infoPanel.getWidth());
    jta.setBounds(0, 0, infoPanel.getWidth(), infoPanel.getHeight() - buttonHeight * 2);

    // Set up the buttons.
    for (int i = 0; i < buttons.length; i++) {
      buttons[i].setBounds(buttonWidth * i, infoPanel.getHeight() - buttonHeight * 2, buttonWidth, buttonHeight);
      buttons[i].repaint();
    }
  }
}