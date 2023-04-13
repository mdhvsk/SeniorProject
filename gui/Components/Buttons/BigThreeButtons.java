package Components.Buttons;

import javax.swing.JButton;
import java.awt.Color;

public class BigThreeButtons extends JButton{

    public BigThreeButtons(String name, Color color, int x, int y){
        setText(name);
        setName(name);
//        setForeground(color);
        setBackground(Color.green);
        setBounds(x, y, 100, 60);
    }
}
