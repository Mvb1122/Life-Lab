import javax.swing.*;

public class InfoPanel {
  private final String[] labels = {"Generation Number", "Number of Living Nodes", "Number of Living Nodes in Row 10", "Number of Living Nodes in Column 10"};
  private final JTextArea jta;

  /**
  *  Starts up and displays the simulation's information.
  */
  public InfoPanel() {
    JFrame infoPanel = new JFrame("Statistics for the Game of Life");
    jta = new JTextArea("Waiting for Simulation to begin.");
    infoPanel.add(jta);
    infoPanel.setBounds(5, 250, 300, labels.length * 50);
    infoPanel.setVisible(true);

    // Set the text
    refreshText();

    // Start up a thread to reset the text
    Thread t = new Thread(() -> {
      while (true) {
        try {
          Thread.sleep(1000);
          refreshText();
        } catch (InterruptedException ignored) {;}
      }
    }, "InfoPanel rendering thread");
    t.start();
  }

  public void refreshText() {
    int[] data = new int[]{Main.genNum - 1, Node.getNumberOfLivingNodes(), Node.getNumberOfLivingNodesByRow(11), Node.getNumberOfLivingNodesByColumn(11)};
    String text = "";
    for (int i = 0; i < data.length; i++) {
      text += labels[i] + ": " + data[i] + '\n';
    }
    jta.setText(text);
  }
}