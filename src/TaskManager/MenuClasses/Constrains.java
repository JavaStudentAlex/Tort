package TaskManager.MenuClasses;

import java.awt.*;

public class Constrains {

    public static GridBagConstraints getLocator(){
        GridBagConstraints result = new GridBagConstraints();
        result.fill = GridBagConstraints.BOTH;
        result.anchor = GridBagConstraints.CENTER;
        result.weightx=1;
        result.weighty=1;
        return  result;
    }

    public static void setLocation(GridBagConstraints locator,int grx, int gry){
        locator.gridx = grx;
        locator.gridy = gry;
    }

    public static void setSize(GridBagConstraints locator, int w, int h){
        locator.gridwidth = w;
        locator.gridheight = h;
    }
}
