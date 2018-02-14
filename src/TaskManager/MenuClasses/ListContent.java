package TaskManager.MenuClasses;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class ListContent{

    public JPanel tasksRootPanel;
    private DefaultListModel<String> listModel;
    private JList<String> listTasks;
    private JButton add, change, delete,reset;

    public ListContent( ActionListener resetListener){
        initializeElems();
        setResetListener(resetListener);
        tasksRootPanel.revalidate();
    }

    private void initializeElems(){
        tasksRootPanel = PanelFactory.createPanel("Tasks");
        tasksRootPanel.setLayout(new GridBagLayout());
        GridBagConstraints locator = Constrains.getLocator();

        listModel = new DefaultListModel<String>();
        listTasks = ListFactory.createList(listModel);
        add = new JButton("Add");
        change = new JButton("Change");
        delete = new JButton("Remove");
        reset = new JButton("Reset");

        Constrains.setLocation(locator,0,0);
        Constrains.setSize(locator,7,5);
        tasksRootPanel.add(listTasks,locator);
        tasksRootPanel.add(new JScrollPane(listTasks),locator);

        locator.weighty = 0.0005;
        Constrains.setLocation(locator,0,5);
        Constrains.setSize(locator,1,1);
        tasksRootPanel.add(add,locator);
        Constrains.setLocation(locator,2,5);
        tasksRootPanel.add(change,locator);
        Constrains.setLocation(locator,4,5);
        tasksRootPanel.add(delete,locator);
        Constrains.setLocation(locator,6,5);
        tasksRootPanel.add(reset,locator);
    }

    private void setResetListener(ActionListener resetListener){
        reset.addActionListener(resetListener);
    }

    public void renewTasks(String[] tasks){
        listModel.removeAllElements();
        for(String buf : tasks){
            listModel.addElement(buf);
        }
        tasksRootPanel.revalidate();
    }

    public void setSelectionListener(ListSelectionListener listener){
        listTasks.addListSelectionListener(listener);
    }

    public void clearList(){
        listModel.removeAllElements();
        listTasks.setSelectedIndex(-1);
    }

    public void setClearSelection(){
        listTasks.clearSelection();
        listTasks.setSelectedIndex(-1);
    }

    public String getSelectedTask(){
        return listTasks.getSelectedValue();
    }

    public void setAddListener(ActionListener listener){
        add.addActionListener(listener);
    }

    public void setChangeListener(ActionListener listener){
        change.addActionListener(listener);
    }

    public void setDeleteListener(ActionListener listener){
        delete.addActionListener(listener);
    }
}
