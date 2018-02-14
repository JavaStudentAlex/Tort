package TaskManager.MenuClasses;

import TaskManager.Cheker;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TasksContent{

    private ListContent listContent;
    private EditionContent editionContent;
    public JPanel tasksMainRootPanel;

    public TasksContent(){
        inializeElems();
    }

    private void inializeElems(){
        tasksMainRootPanel = PanelFactory.createPanel(View.menuTasks);
        listContent = new ListContent(new ResetListener());
        editionContent = new EditionContent();
        tasksMainRootPanel.setLayout(new GridBagLayout());
        GridBagConstraints loc = Constrains.getLocator();
        Constrains.setLocation(loc,0,0);
        loc.weighty = 0.28;
        tasksMainRootPanel.add(listContent.tasksRootPanel,loc);
        Constrains.setLocation(loc,0,1);
        loc.weighty = 0.72;
        tasksMainRootPanel.add(editionContent.editionRootPanel,loc);
    }

    private class ResetListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            editionContent.clearFields();
        }
    }

    public void clear(){
        editionContent.clearFields();
        listContent.setClearSelection();
    }


    public String getNewTask(){
        return editionContent.getNewTask();
    }

    public void renewTasks(String[] tasks){
        listContent.renewTasks(tasks);
    }

    public void setSelectionListener(ListSelectionListener listener){
        listContent.setSelectionListener(listener);
    }

    public void setTaskToFields(String task){
        editionContent.setTaskToFields(task);
    }

    public String getSelectedTask(){
        return listContent.getSelectedTask();
    }

    public void setAddListener(ActionListener listener){
        listContent.setAddListener(listener);
    }

    public void setChangeListener(ActionListener listener){
        listContent.setChangeListener(listener);
    }

    public void setDeleteListener(ActionListener listener){
        listContent.setDeleteListener(listener);
    }
}
