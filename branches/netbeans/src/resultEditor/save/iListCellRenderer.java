/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.save;

/**
 *
 * @author Chris
 */

import java.awt.Color;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import javax.swing.JList;
import java.awt.Font;
import javax.swing.JCheckBox;


public class iListCellRenderer extends JCheckBox implements ListCellRenderer {

    private static final long serialVersionUID = -2575287177726702542L;
    private static final Color HIGHLIGHT_COLOR = new Color(50, 50, 128);
    protected iListEntry entry;

    public iListCellRenderer() {
        this.setOpaque(true);
        this.setIconTextGap(12);
        this.setFont(new Font("Calibri", Font.PLAIN, 11));
    }

    public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean cellHasFocus) {

        entry = (iListEntry) value;

        this.setText( entry.getTitle() );

        this.setSelected( entry.isCheckBoxSelected );

        //this.setIcon( entry.getImage() );
        this.setVerticalTextPosition(1);


        if (isSelected) {
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

