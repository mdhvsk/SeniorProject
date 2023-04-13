package Components;

import Components.Buttons.BigThreeButtons;
import Components.Buttons.TopButtons;
import Components.Panels.Inputs;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;

public class MainFrame {
    private JFrame frame;

    public MainFrame(){
        initialize();
    }

    private void initialize(){

        // Frame Layout
        frame = new JFrame();
        frame.setLayout(new BorderLayout(10,5));
        frame.setTitle("Vibration Table GUI");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Button Layout
        BigThreeButtons start = new BigThreeButtons("Start", Color.green, 625, 50);
        BigThreeButtons stop = new BigThreeButtons("Stop", Color.red, 750, 50);
        BigThreeButtons pause = new BigThreeButtons("Pause", Color.yellow, 875, 50);

        frame.add(start);
        frame.add(stop);
        frame.add(pause);

        Inputs input = new Inputs();
        frame.add(input);

        frame.setLayout(null);
        frame.setVisible(true);


    }
}
