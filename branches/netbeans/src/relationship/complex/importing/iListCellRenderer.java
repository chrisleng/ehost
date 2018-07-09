/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relationship.complex.importing;

/**
 *
 * @author Chris
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


public class iListCellRenderer extends JCheckBox implements ListCellRenderer {

    private static final long serialVersionUID = -2575287177726702542L;
    private static final Color HIGHLIGHT_COLOR = new Color(200, 200, 208);
    protected iListEntry entry;

    public iListCellRenderer() {
        this.setOpaque(true);
        this.setIconTextGap(12);
        this.setFont(new Font("Calibri", Font.PLAIN, 12));
    }

    public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean cellHasFocus) {

        entry = (iListEntry) value;

        this.setText( entry.getTitle() );

        this.setSelected( entry.selected );

        //this.setIcon( entry.getImage() );
        this.setVerticalTextPosition(1);


        if (entry.selected) {
            this.setBackground(HIGHLIGHT_COLOR);
            this.setForeground(Color.white);
        } else {
            this.setBackground(Color.white);
            this.setForeground(Color.black);
        }

        if( !entry.isCheckBoxSelected ){
            this.setForeground(new Color(200,200,200));
        }else
            this.setForeground(Color.black);

        /*this.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                iListCellRenderer.this.setCheckStatus();
            }
        });

        this.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                if( e.getClickCount() == 2){
                    System.out.println("--");
                }
            }

            public void mousePressed(MouseEvent e) {

            }

            public void mouseReleased(MouseEvent e) {

            }

            public void mouseEntered(MouseEvent e) {

            }

            public void mouseExited(MouseEvent e) {

            }
        });

         * */
        
        return this;
        
    }

    /*
    public void setCheckStatus(){
        String filename = entry.filename;
        boolean flag = !entry.isCheckBoxSelected;

        System.out.println( "filename = " + filename + " = " + flag );

        SaveDialog.setCheckBoxSelectedStatus(filename, flag);
        SaveDialog.listdisplay();
    }*/
}

