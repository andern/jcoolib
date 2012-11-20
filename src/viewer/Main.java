package viewer;

import java.awt.BasicStroke;
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
//        pack();
        
        CCSystem s = new CCSystem();
        add(s);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        s.add(new Line(4.0, 1.0, Color.blue));
        s.setNiceGraphics(true);
//        s.setAxisXPaint(Color.green);
//        s.setAxisYPaint(Color.cyan);
//        s.setUnitsPaint(Color.white);
//        s.setGridXPaint(Color.yellow);
//        s.setGridYPaint(Color.orange);
//        s.setGridStroke(new BasicStroke(1f));
//        s.setGridRatio(10);
//        s.setGridPaint(Color.pink);
//        s.setGridStroke(new BasicStroke(0.5f));
//        s.setGridVisible(true);
//        s.setZoomable(false);
//        s.setMovable(false);
        
//        s.add(new Line(-1/4.0, 2));
        
        
//        s.setVisibleUnitLines(true);
//        s.setVisibleAxes(true);
//        s.setNiceGraphics(false);
        
//        Random r = new Random();
//        
//        for (int i = 0; i < 100; i++) {
//            double a = r.nextDouble();
//            double b = r.nextDouble();
//            double c = r.nextDouble();
//            if (r.nextBoolean()) a = -a;
//            if (r.nextBoolean()) b = -b;
//            if (r.nextBoolean()) c = -c;
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
//            s.add(new Line(a,b,c,color));
//        }
    }
    public static void main(String[] args) {
        new Main();
    }
}
