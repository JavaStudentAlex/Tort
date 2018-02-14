package TaskManager.MenuClasses;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class PanelFactory {

    public static JPanel createPanel(String name){
        JPanel result = new JPanel();
        Border border = BorderFactory.createEtchedBorder();
        TitledBorder borderTitle = BorderFactory.createTitledBorder(border,name);
        result.setBorder(borderTitle);
        return result;
    }
}
