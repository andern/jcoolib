package viewer;

import java.awt.Color;
import javax.swing.JFrame;
import cartesian.coordinate.CCSystem;
import cartesian.coordinate.Line;

public class Main extends JFrame {
    private static final long serialVersionUID = 1L;
    
    Main() {
        super("Viewer");
        setTitle("Viewer");
        
        setVisible(true);
        setSize(800, 600);
        setLocationRelativeTo(null);
        pack();
        
        CCSystem s = new CCSystem();
        add(s);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        s.setVisibleUnitLines(false);
        s.setVisibleAxes(true);
//        s.setNiceGraphics(false);
        
        s.add(new Line(3, 8, Color.blue));
    }
    public static void main(String[] args) {
        new Main();
    }
}
