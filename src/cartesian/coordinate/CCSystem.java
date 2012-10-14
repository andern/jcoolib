/*
 * Copyright (C) 2012 Andreas Halle
 *
 * This file is part of jcoolib
 *
 * jcoolib is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jcoolib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public license
 * along with jcoolib. If not, see <http://www.gnu.org/licenses/>.
 */
package cartesian.coordinate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * A class representing a visible Cartesian coordinate system.
 * <p>
 * The system contains (in)visible x- and y-axes that range from one double
 * precision number to another.
 * <p>
 * The system can contain objects such as lines, points and polygons.
 * 
 * @author Andreas Halle
 * @see    Line
 */
public class CCSystem extends JPanel {
    /*
     * In this class there are two coordinate systems:
     * 
     * 1. A two-dimensional coordinate system for Java2D where x lies in the
     *    interval [0, window width] and y lies in the interval
     *    [0, window height] where the units of both x and y are pixels.
     *    
     * 2. An emulated two-dimensional coordinate system where x and y can lie in
     *    any range definable by double precision numbers.
     * 
     * Throughout this class, Point is used to represent a point in system 1
     * while Point2D is used to represent a point in system 2.
     * 
     * The methods translate(Point) and translate(Point2D) is used to translate
     * between the two systems.
     */
    private static final long serialVersionUID = 1L;
    
    /* Some visual options */
    private boolean drawXAxis;
    private boolean drawYAxis;
    
    private boolean drawXUnits;
    private boolean drawYUnits;
    
    private boolean niceGraphics;
    /* End of visual options */
    
    /* Other options */
    private boolean moveable;
    private boolean zoomable;
    
    /* Object containers */
    private ArrayList<Line> lines;
    
    /* Define the range of the visibly xy-plane */
    private double loX;
    private double hiX;
    private double loY;
    private double hiY;
    
    /* The length of the domain of x and y */
    private double distX;
    private double distY;
    
    /* The ratio between System 1 and System 2 */
    private double xscale;
    private double yscale;
    
    private Point2D.Double origo;
    
    /* Some private modifiers */
    private static final boolean DRAW_XAXIS = false;
    private static final boolean DRAW_YAXIS = true;
    
    
    
    /**
     * Initialize a new empty coordinate system.
     */
    public CCSystem() {
        /* Setting some default values. */
        drawXAxis = true;
        drawYAxis = true;;
        drawXUnits = true; // Unit lines are only drawn if the axes are drawn.
        drawYUnits = true;
        niceGraphics = true;
        zoomable = true;
        moveable = true;
        loX = -10;
        hiX = 10;
        loY = -10;
        hiY = 10;
        
        lines = new ArrayList<Line>();
        
        /* Add some default listeners */
        addMouseListener(new mouseListener());
        addMouseMotionListener(new mouseListener());
        addMouseWheelListener(new mouseScrollListener());
    }
    
    
    
    /**
     * Add a {@code Line} to the coordinate system.
     * 
     * @param line
     *        a {@code Line} object.
     */
    public void add(Line line) {
        lines.add(line);
    }
    
    
    
    /**
     * Remove all visible objects in the current system.
     */
    public void clear() {
        lines.clear();
        updateUI();
    }
    
    
    
    /* Move the visible area relevant to the current position. */ 
    private void drag(double moveX, double moveY) {
        loX += moveX;
        hiX += moveX;
        loY += moveY;
        hiY += moveY;
    }
    
    
    
    /**
     * Draw the axes and unit lines in the best looking way possible for the
     * given x- and y-ranges.
     * 
     * @param g2d
     *        A {@code Graphics2D} object.
     */
    public void drawAxes(Graphics2D g2d) {
        g2d.setColor(Color.black);
        if (drawXAxis) drawAxis(g2d, DRAW_XAXIS, drawXUnits);
        if (drawYAxis) drawAxis(g2d, DRAW_YAXIS, drawYUnits);
    }
    
    
    /*
     * Draw a given axis, with or without unit lines.
     */
    private void drawAxis(Graphics2D g2d, boolean yaxis, boolean unitLines) {
        Point o = translate(origo);
        
        /* Draw the actual axes without any unit lines. */
        if (yaxis) g2d.drawLine(o.x, 0, o.x, getHeight());
        else       g2d.drawLine(0, o.y, getWidth(), o.y);
        
        /* If we don't want unit lines, stop here. */
        if (!unitLines) return;
    
        /* Find position for the unit line values. */
        boolean ywest = (loX >= 0) ? false : true;
        boolean xsouth = (loY >= 0) ? false : true;
        
        /* Approximate number of pixels between each unit line. */
        int pbu = 65;
        
        /* Total number of units on the axis */
        int units;
        if (yaxis) units = getHeight() / pbu;
        else       units = getWidth() / pbu;
        
        if (units == 0) return;
        
        /* Exact value between each unit line */
        double udist;
        if (yaxis) udist = distY / units;
        else       udist = distX / units;
        
        /* Round this exact value to a value (of the same magnitude) that can be
         * written with very few decimals (or lots of trailing zeroes.)
         */
        double vbu = findScale(udist);

        /* 
         * The value at each unit line will now be defined as q * vbu. We need
         * to find the value of q such that q * vbu is the value at the first
         * visible unit line.
         */
        int i;
        if (yaxis) i = (int) Math.ceil(loY / vbu); 
        else       i = (int) Math.ceil(loX / vbu);
        
        /* Also find the value at the last visible unit line. */
        int end;
        if (yaxis) end = (int) Math.floor(hiY / vbu);
        else       end = (int) Math.floor(hiX / vbu);
        
        for (; i <= end; i++) {
            double val = i * vbu;
            String strval = Double.toString(val);
            
            Point2D.Double p2d;
            if (yaxis) p2d = new Point2D.Double(origo.x, val);
            else       p2d = new Point2D.Double(val, origo.y);
            
            Point p = translate(p2d);
            
            int pixelPerChar = 7;
            int strValPixels = pixelPerChar * strval.length();
            
            int offset;
            if (yaxis) offset = ywest ? -strValPixels : 5;
            else       offset = xsouth ? 20 : -10;
            
            /* Don't draw the value at origo. */
            if (val != 0.0 && yaxis) {
                g2d.drawString(strval, p.x+offset, p.y+5);
            } else if (val != 0.0) {
                g2d.drawString(strval, p.x - strValPixels/2, p.y+offset);
            }
            
            /* Length of unit line in pixels. */
            int size = 4;
            if (yaxis) g2d.drawLine(p.x-size, p.y, p.x+size, p.y);
            else       g2d.drawLine(p.x, p.y-size, p.x, p.y+size);
        }
    }



    /*
     * Draw a Line.
     */
    private void drawLine(Graphics2D g2d, Line l) {
        Point2D.Double p2d1 = null;
        Point2D.Double p2d2 = null;
        /* Vertical line */
        if (l.b == 0.0) {
            int mul = (l.a < 0) ? -1 : 1;
            
            double xval = l.c*mul;
            /* If the line is outside the visible area, don't draw it. */
            if (xval < loX || xval > hiX) return;
            
            p2d1 = new Point2D.Double(xval, loY);
            p2d2 = new Point2D.Double(xval, hiY);
        /* Horizontal line */
        } else if (l.a == 0.0) {
            int mul = (l.a  < 0) ? -1 : 1;
            double yval = l.c*mul;
            
            if (yval < loY || yval > hiY) return;
            
            p2d1 = new Point2D.Double(loX, l.c*mul);
            p2d2 = new Point2D.Double(hiX, l.c*mul);
        /* Line with a defined non-zero slope. */
        } else {
            /* Find intercepts with the display window */
            double i_loX = l.solveForY(loX);
            double i_hiX = l.solveForY(hiX);
            double i_loY = l.solveForX(loY);
            double i_hiY = l.solveForX(hiY);
            
            if (validY(i_loX)) p2d1 = new Point2D.Double(loX, i_loX);
            else if (validX(i_loY)) p2d1 = new Point2D.Double(i_loY, loY);
            
            if (validY(i_hiX)) p2d2 = new Point2D.Double(hiX, i_hiX);
            else if (validX(i_hiY)) p2d2 = new Point2D.Double(i_hiY, hiY);
        }
        if (p2d1 == null || p2d2 == null) return;
        
        Point p1 = translate(p2d1);
        Point p2 = translate(p2d2);
        
        g2d.setPaint(l.paint);
        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
    }



    /* 
     * Round this exact value to a value (of the same magnitude) that can be
     * written with very few decimals.
     */
    private double findScale(double num) {
        int x = (int) Math.floor(Math.log10(num));
        double scale = Math.pow(10, x);
        
        double quot = num / scale;
        if (quot > 5.0) return 10*scale;
        if (quot > 2.0) return 5*scale;
        if (quot > 1.0) return 2*scale;
        else return scale;
    }
    
    
    
    private RenderingHints getNiceGraphics() {
        RenderingHints rh = new RenderingHints(null);
        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        rh.put(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        rh.put(RenderingHints.KEY_DITHERING,
                RenderingHints.VALUE_DITHER_ENABLE);
        rh.put(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        rh.put(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        rh.put(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_NORMALIZE);
        rh.put(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return rh;
    }
    
    
    
    /**
     * Moves the entire visible area of the current system. The visible area
     * will be x and y in [loX, hiX] and [loY, hiY], respectively.
     * 
     * @param loX
     *        Lowest visible value of x. 
     * @param hiX
     *        Highest visible value of x.
     * @param loY
     *        Lowest visible value of y.
     * @param hiY
     *        Highest visible value of y.
     */
    public void move(double loX, double hiX, double loY, double hiY) {
        this.loX = loX;
        this.hiX = hiX;
        this.loY = loY;
        this.hiY = hiY;
    }
    
    
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        updatePosition();
        
        if (niceGraphics) g2d.addRenderingHints(getNiceGraphics());
        
        drawAxes(g2d);
        for (Line line : lines) drawLine(g2d, line);
    }



    /**
     * Turn on nice graphics.
     * <p>
     * More specifically:
     * <pre>
     *     KEY_ALPHA_INTERPOLATION = VALUE_ALPHA_INTERPOLATION_QUALITY
     *     KEY_ANTIALIASING = VALUE_ANTIALIAS_ON
     *     KEY_COLOR_RENDERING = VALUE_COLOR_RENDER_QUALITY
     *     KEY_DITHERING = VALUE_DITHER_ENABLE
     *     KEY_FRACTIONALMETRICS = VALUE_FRACTIONALMETRICS_ON
     *     KEY_INTERPOLATION = VALUE_INTERPOLATION_BICUBIC
     *     KEY_RENDERING = VALUE_RENDER_QUALITY
     *     KEY_STROKE_CONTROL = VALUE_STROKE_NORMALIZE
     *     KEY_TEXT_ANTIALIASING = VALUE_TEXT_ANTIALIAS_ON.
     * </pre>
     * 
     * @param niceGraphics
     *        If true, use nice graphics.
     */
    public void setNiceGraphics(boolean niceGraphics) {
        this.niceGraphics = niceGraphics;
    }



    /**
     * @param visible
     *        If true, draw axes.
     */
    public void setVisibleAxes(boolean visible) {
        drawXAxis = visible;
        drawYAxis = visible;
    }
    
    
    
    public void setVisibleUnitLines(boolean visible) {
        drawXUnits = visible;
        drawYUnits = visible;
    }
    
    
    
    /*
     * Convert points from system 1 to system 2.
     */
    private Point2D.Double translate(Point p) {
        double x = p.x * xscale + loX;
        double y = p.y * yscale + loY;

        return new Point2D.Double(x, y);
    }
    
    
    
    private Point translate(Point2D.Double p2d) {
        // TODO: Remove when done
        if (p2d.x < loX || p2d.x > hiX || p2d.y < loY || p2d.y > hiY) {
            try {
                throw new Exception(p2d.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int x = (int) Math.round((p2d.x - loX) / xscale);
        int y = (int) Math.round((p2d.y - loY) / yscale);
        
        /* Convert so that increasing y takes you north instead of south. */
        y = getHeight() - y;
        
        return new Point(x, y);
    }
    
    
    
    private void updatePosition() {
        distX = hiX - loX;
        distY = hiY - loY;
        
        xscale = distX / getWidth();
        
        yscale = distY / getHeight();
        
        /* Find origo */
        double ox = 0;
        double oy = 0;
        
        if (loX >= 0) ox = loX;
        else if (hiX <= 0) ox = hiX;
        
        if (loY >= 0) oy = loY;
        else if (hiY <= 0) oy = hiY;
        
        origo = new Point2D.Double(ox, oy);
    }
    
    
    
    private boolean validX(double x) {
        return (x >= loX && x <= hiX);
    }
    
    
    
    private boolean validY(double y) {
        return (y >= loY && y <= hiY);
    }
    
    
    
    /*
     * Zoom into the visible area relevant to the current
     * position by keeping the center the same.
     */
    private void zoom(double zoomX, double zoomY) {
        loX -= zoomX;
        hiX += zoomX;
        loY -= zoomY;
        hiY += zoomY;
    }
    
    
    
    /**
     * A {@code MouseWheelListener} making it possible to zoom in and out
     * of the coordinate system using the mouse wheel.
     */
    class mouseScrollListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (!zoomable) return;

            int units = e.getUnitsToScroll();

            double distx = hiX - loX;
            double disty = hiY - loY;

            double zoomx = distx / 100.0 * units;
            double zoomy = disty / 100.0 * units;

            zoom(zoomx, zoomy);

            repaint();
        }
    }
    
    
    
    /**
     * A {@code MouseListener} making it possible to click and drag to move the
     * position of the coordinaty system.
     */
    class mouseListener implements MouseListener, MouseMotionListener {
        private int lastX;
        private int lastY;

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {
            lastX = e.getX();
            lastY = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            /* Don't move if we don't want to. */
            if (!moveable) return;
            
            int x = e.getX();
            int y = e.getY();
            
            int dx = lastX - x;
            int dy = lastY - y;

            double moveX = distX / getWidth() * dx;
            double moveY = distY / getHeight() * dy;
            
            drag(moveX, -moveY);

            repaint();
            
            lastX = x;
            lastY = y;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            lastX = e.getX();
            lastY = e.getY();
        }
    }
}