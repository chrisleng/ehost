/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotationClasses;

/**
 *
 * @author Chris
 */
public class ClassColor {
    /*private static Vector<ColorCode> COLORS = new Vector<ColorCode>();

    public static void clear(){
        COLORS.clear();
    }

    //add a record for an annotated classname and its display background color.
    public static void addColorCode(String className, Color color){
        if ((className == null)||(color== null))
            return;
        
        ColorCode cc = new ColorCode( className, color );
        COLORS.add(cc);
    }

    public static int size(){
        return COLORS.size();
    }*/

    /*public static String[] getColorCodePair(int _index){
        if ( _index > (COLORS.size() - 1) )
            return null;

        String[] ColorCodePair = new String[2];

        ColorCodePair[0] = COLORS.get(_index).className.toLowerCase().toString();
        ColorCodePair[1] = COLORS.get(_index).colorCode.toLowerCase().toString();

        return ColorCodePair;
    }*/

    /*private static Color getColorCodeByClassname(String _classname){
        int size = COLORS.size();
        Color color = null;
        for(int i=0; i<size; i++){
            //System.out.println("--------------------");
            //System.out.println("i="+i+" "+COLORS.get(i).className.trim().toLowerCase());
            //System.out.println("i="+i+" "+COLORS.get(i).colorCode.trim().toLowerCase());
            String classname = COLORS.get(i).className.trim().toLowerCase();
            //System.out.println("1.line - " + classname );
            if( classname.trim().toLowerCase().compareTo(_classname.trim().toLowerCase())==0){
                color = COLORS.get(i).color;
                return color;
            }
        }
        return null;
    }*/

    /**return the color to a given annotated class name*/
    /*public static Color getColor(String _classname){


        if (_classname == null)
            return null;

        // ##<1>## get color code of the annotated class
        Color color = getColorCodeByClassname(_classname);

        // System.out.println("colorcode is - " + colorCode);

        // ##<2>## some color code of your annotated classes maybe get defined in string of name of color
        if (color == null) return null;
        /*
        if( colorCode.compareTo("cyan")==0) return Color.CYAN;
        else if( colorCode.compareTo("orange")==0) return Color.orange;
        else if( colorCode.compareTo("black")==0) return Color.black;
        else if( colorCode.compareTo("blue")==0) return Color.BLUE;
        else if( colorCode.compareTo("dark_gray")==0) return Color.DARK_GRAY;
        else if( colorCode.compareTo("gray")==0) return Color.GRAY;
        else if( colorCode.compareTo("green")==0) return Color.GREEN;
        else if( colorCode.compareTo("light_gray")==0) return Color.LIGHT_GRAY;
        else if( colorCode.compareTo("magenta")==0) return Color.MAGENTA;
        else if( colorCode.compareTo("pink")==0) return Color.PINK;
        else if( colorCode.compareTo("red")==0) return Color.RED;
        else if( colorCode.compareTo("yellow")==0) return Color.YELLOW;
        */
/*
        return color;
    }*/
}
