package Components.Buttons;

import javax.swing.JButton;

public class TopButtons {
    private JButton button;

    public TopButtons(String name, Integer x, Integer y){
        initialize(name, x, y);
    }
    private void initialize(String name, Integer x, Integer y){
        button = new JButton();
        button.setText(name);
        button.setBounds(x, y, 100, 60);

    }
}
