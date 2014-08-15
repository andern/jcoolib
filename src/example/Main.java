package example;

import java.awt.Color;
import java.util.Random;

import javax.swing.JFrame;

import cartesian.coordinate.CCPoint;
import cartesian.coordinate.CCPolygon;
import cartesian.coordinate.CCSystem;
import cartesian.coordinate.CCLine;

public class Main extends JFrame {
    private static final long serialVersionUID = 1L;
    
    Main() {
        super("Viewer");
        setTitle("Viewer");
        
        setVisible(true);
        setSize(800, 600);
        setLocationRelativeTo(null);
        CCSystem s = new CCSystem(0.0, 0.0, 10.0, 10.0);
        add(s);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        double[] x = new double[]{-1,2,0};
        double[] y = new double[]{-1,-2,3};
        CCPolygon ccp = new CCPolygon(x, y);
        
        s.add(new CCLine(1.0, 0.0, Color.blue));
        s.add(new CCLine(1.0, 5.0, Color.red));
        s.add(new CCLine(-1.0, 5.0, Color.cyan));
        s.add(new CCLine(-1.0, 15.0, Color.yellow));
        s.add(new CCLine(1.0, -5.0, Color.green));
        s.add(new CCLine(1.0, 0.0, 5.0, Color.orange));
        s.add(new CCLine(0.0, 1.0, 5.0, Color.pink));
        s.add(new CCPoint(4, 5));
        s.add(ccp);
        
        /*
        Random r = new Random();
        for (int i = 0; i < 300; i++) {
            double a = r.nextDouble();
            double b = r.nextDouble();
            double c = r.nextDouble();
            if (r.nextBoolean()) a = -a;
            if (r.nextBoolean()) b = -b;
            if (r.nextBoolean()) c = -c;
            
            int cint = r.nextInt(13);
            Color color;
            
            switch (cint) {
            case 0: color = Color.black; break;
            case 1: color = Color.blue; break;
            case 2: color = Color.cyan; break;
            case 3: color = Color.darkGray; break;
            case 4: color = Color.gray; break;
            case 5: color = Color.green; break;
            case 6: color = Color.lightGray; break;
            case 7: color = Color.magenta; break;
            case 8: color = Color.orange; break;
            case 9: color = Color.pink; break;
            case 10: color = Color.red; break;
            case 11: color = Color.white; break;
            default: color = Color.yellow;
            }
            
            s.add(new CCLine(a,b,c,color));
        }
        */
    }
    public static void main(String[] args) {
        new Main();
    }
}
