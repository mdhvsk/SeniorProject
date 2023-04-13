package Components.Panels;
import javax.swing.*;
import java.text.DateFormat;
import java.text.Format;
import java.util.Date;

public class Inputs extends JPanel {

    public Inputs(){
        int currFreq = 100;
        SpinnerModel model = new SpinnerNumberModel(currFreq, //initial value
                        currFreq - 100, //min
                        currFreq + 10000, //max
                        1);//step
        JLabel label = new JLabel("Frequency of Vibration");

        JSpinner spinner = new JSpinner(model);
        add(label);
        add(spinner);
        setBounds(100, 100, 200, 200);

        Format shortTime = DateFormat.getTimeInstance(DateFormat.SHORT);
        JLabel timeLabel = new JLabel("Short time:");
        JFormattedTextField input = new JFormattedTextField(shortTime);
        input.setValue(new Date());
        input.setColumns(20);
        add(timeLabel);
        add(input);
//        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm:ss");
//        add(editor);

    }

}
