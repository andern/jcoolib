package viewer;

import java.awt.Color;
import java.util.Random;

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
        
        s.add(new Line(4.0, 1.0));
        s.add(new Line(-1/4.0, 2));
        
        
//        s.setVisibleUnitLines(true);
//        s.setVisibleAxes(true);
//        s.setNiceGraphics(false);
        
        Random r = new Random();
        
//        for (int i = 0; i < 100; i++) {
//            double slope = r.nextDouble();
//            if (r.nextBoolean()) slope = -slope;
//            double yintercept = r.nextDouble()*100;
//            if (r.nextBoolean()) yintercept = -yintercept;
//            
//            int cint = r.nextInt(13);
//            Color color;
//            
//            switch (cint) {
//            case 0: color = Color.black; break;
//            case 1: color = Color.blue; break;
//            case 2: color = Color.cyan; break;
//            case 3: color = Color.darkGray; break;
//            case 4: color = Color.gray; break;
//            case 5: color = Color.green; break;
//            case 6: color = Color.lightGray; break;
//            case 7: color = Color.magenta; break;
//            case 8: color = Color.orange; break;
//            case 9: color = Color.pink; break;
//            case 10: color = Color.red; break;
//            case 11: color = Color.white; break;
//            default: color = Color.yellow;
//            }
//            
////            System.out.printf("Adding line: y = %-2fx + %-2f%n", slope, yintercept);
//            s.add(new Line(slope, yintercept, color));
//        }
        
//        s.add(new Line(3, 8, Color.blue));
//        s.add(new Line(-3, 5, Color.red));
//        s.add(new Line(-5, 3, Color.orange));
    }
    public static void main(String[] args) {
        new Main();
    }
}
