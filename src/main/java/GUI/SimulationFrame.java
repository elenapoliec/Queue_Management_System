package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SimulationFrame extends JFrame {
    private JTextField nInput = new JTextField(10);
    private JTextField qInput = new JTextField(10);
    private JTextField tSimInput = new JTextField(10);
    private JTextField minArrivalInput = new JTextField(10);
    private JTextField maxArrivalInput = new JTextField(10);
    private JTextField minServiceInput = new JTextField(10);
    private JTextField maxServiceInput = new JTextField(10);

    private JButton startButton = new JButton("Start Simulation");
    private JButton clearButton = new JButton("Clear Simulation");

    private JTextArea displayArea = new JTextArea();

    public SimulationFrame() {
        this.setTitle("Queues Management Simulator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(950, 700);
        this.setLayout(new BorderLayout());


        JPanel controlPanel = new JPanel(new GridLayout(11, 2, 5, 5));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Simulation Settings"));

        controlPanel.add(new JLabel("Number of clients (N):"));
        controlPanel.add(nInput);

        controlPanel.add(new JLabel("Number of queues (Q):"));
        controlPanel.add(qInput);

        controlPanel.add(new JLabel("Max Simulation time:"));
        controlPanel.add(tSimInput);

        controlPanel.add(new JLabel("Min Arrival time:"));
        controlPanel.add(minArrivalInput);

        controlPanel.add(new JLabel("Max Arrival time:"));
        controlPanel.add(maxArrivalInput);

        controlPanel.add(new JLabel("Min Service time:"));
        controlPanel.add(minServiceInput);

        controlPanel.add(new JLabel("Max Service time:"));
        controlPanel.add(maxServiceInput);


        controlPanel.add(new JLabel("Action:"));
        controlPanel.add(startButton);

        controlPanel.add(new JLabel("")); // spacer
        controlPanel.add(clearButton);

        clearButton.addActionListener(e -> {
            displayArea.setText("");
        });


        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(displayArea);


        this.add(controlPanel, BorderLayout.WEST);
        this.add(scroll, BorderLayout.CENTER);

        this.setVisible(true);
    }

    public String getN() { return nInput.getText(); }
    public String getQ() { return qInput.getText(); }
    public String getTSim() { return tSimInput.getText(); }
    public String getMinArrival() { return minArrivalInput.getText(); }
    public String getMaxArrival() { return maxArrivalInput.getText(); }
    public String getMinService() { return minServiceInput.getText(); }
    public String getMaxService() { return maxServiceInput.getText(); }

    public void addStartListener(ActionListener al) {
        startButton.addActionListener(al);
    }

    public void addClearListener(ActionListener al) {
        clearButton.addActionListener(al);
    }

    public void updateDisplay(String text) {
        SwingUtilities.invokeLater(() -> {
            displayArea.append(text + "\n");
            displayArea.setCaretPosition(displayArea.getDocument().getLength());
        });
    }

}