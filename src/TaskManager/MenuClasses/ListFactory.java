package TaskManager.MenuClasses;

import javax.swing.*;

public class ListFactory {

    public static JList<String> createList(DefaultListModel<String> model){
        JList<String> result = new JList(model);
        result.setLayoutOrientation(JList.VERTICAL);
        result.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return result;
    }
}
