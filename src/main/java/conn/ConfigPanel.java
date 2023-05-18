package conn;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

public class ConfigPanel extends JPanel {
    final MainFrame frame;
    JLabel dotsLabel, linesLabel;
    static JSpinner dotsSpinner;
    static JComboBox linesCombo;
    JButton createButton;
    public static Integer numVertices;
    public static Double edgeProbability;

    public ConfigPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    public static Integer getNumVertices() {
        return numVertices;
    }

    public static Double getEdgeProbability() {
        return edgeProbability;
    }

    private void init() {
        dotsLabel = new JLabel("Number of dots:");
        dotsSpinner = new JSpinner(new SpinnerNumberModel(8, 3, 100, 1));

        linesLabel = new JLabel("Line probability:");
        String[] numbers = new String[101];
        IntStream.rangeClosed(1, 101).forEach(i -> numbers[i - 1] = Float.toString((float) (i - 1) / 100));
        Collections.reverse(Arrays.asList(numbers));
        linesCombo = new JComboBox(numbers);
        createButton = new JButton("Create new game");

        updateData();
        add(dotsLabel);
        add(dotsSpinner);
        add(linesLabel);
        add(linesCombo);
        add(createButton);
        createButton.addActionListener(this::createButton);
    }
    public static void updateData()
    {
        numVertices = (Integer) dotsSpinner.getValue();
        edgeProbability = Double.parseDouble((String) linesCombo.getSelectedItem());
    }
    private void createButton(ActionEvent e) {
        updateData();
        //MainFrame.canvas.createBoard(numVertices,edgeProbability);

    }
}

