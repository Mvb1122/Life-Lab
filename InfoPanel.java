import javax.swing.*;

public class InfoPanel {
  private int[] data;
  private final String[] labels = {"Generation Number", "Number of Living Nodes", "Number of Living Nodes in Row 10", "Number of Living Nodes in Column 10"};
  private final JFrame infoPanel;
  private final JTextArea jta;

  /**
  *  Starts up and displays the simulation's information.
  */
  public InfoPanel() {
    this.data = new int[]{Main.genNum, Node.getNumberOfLivingNodes(), Node.getNumberOfLivingNodesByRow(11), Integer.MAX_VALUE};

    infoPanel = new JFrame();
    jta = new JTextArea("TEST.");
    infoPanel.add(jta);
    infoPanel.setBounds(5, 500, 300, 500);
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
    data = new int[]{Main.genNum, Node.getNumberOfLivingNodes(), Node.getNumberOfLivingNodesByRow(11), Integer.MAX_VALUE};
    String text = "";
    for (int i = 0; i < data.length; i++) {
      text += labels[i] + ": " + data[i] + '\n';
    }
    jta.setText(text);
  }
}