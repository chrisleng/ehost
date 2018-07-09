package resultEditor.annotationClasses.navigationTree.treeRelated;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.Color;
import java.awt.Polygon;
import javax.swing.Icon;
import java.awt.Component;
import java.awt.Graphics;

/**
 *
 * @author Chris
 */
public class DiamondIcon implements Icon {
  private Color color;

  private boolean selected;

  private int width;

  private int height;

  private Polygon poly;

  private static final int DEFAULT_WIDTH = 12;

  private static final int DEFAULT_HEIGHT = 12;

  public DiamondIcon(Color color) {
    this(color, true, DEFAULT_WIDTH, DEFAULT_HEIGHT);
  }

  public DiamondIcon(Color color, boolean selected) {
    this(color, selected, DEFAULT_WIDTH, DEFAULT_HEIGHT);
  }

  public DiamondIcon(Color color, boolean selected, int width, int height) {
    this.color = color;
    this.selected = selected;
    this.width = width;
    this.height = height;
    initPolygon();
  }

  private void initPolygon() {
    poly = new Polygon();
    int halfWidth = width / 2;
    int halfHeight = height / 2;
    poly.addPoint(0, halfHeight);
    poly.addPoint(halfWidth, 0);
    poly.addPoint(width, halfHeight);
    poly.addPoint(halfWidth, height);

  }

  public int getIconHeight() {
    return height+8;
  }

  public int getIconWidth() {
    return width;
  }

  public void paintIcon(Component c, Graphics g, int x, int y) {
    g.setColor(color);
    g.translate(x, y);
    if (selected) {
      g.fillPolygon(poly);
    } else {
      g.drawPolygon(poly);
    }

    if( color == Color.black ){
        g.setColor(Color.white);
        g.drawLine(width / 2, 0, width/2, height);
        g.drawLine(0, height/2, width, height/2);
    }
    g.translate(-x, -y);


  }
}
