package Components;
import Components.Buttons.BigThreeButtons;
import Components.Panels.Inputs;
import jdk.internal.util.xml.impl.Input;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class Frame extends JFrame{

    public Frame(){

        BigThreeButtons start = new BigThreeButtons("Start", Color.green, 625, 50);
        BigThreeButtons stop = new BigThreeButtons("Stop", Color.red, 750, 50);
        BigThreeButtons pause = new BigThreeButtons("Pause", Color.yellow, 875, 50);

        add(start);
        add(stop);
        add(pause);

        Inputs inputs = new Inputs();
        add(inputs);

        JPanel freqInput = new JPanel();

        int currentYear = 2023;
        SpinnerModel model = new SpinnerNumberModel(currentYear, //initial value
                currentYear - 100, //min
                currentYear + 100, //max
                1);//step
        JSpinner spinner = new JSpinner(model);

        freqInput.add(spinner);

        add(freqInput);

        setSize(new Dimension(1000,600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
    }

}
