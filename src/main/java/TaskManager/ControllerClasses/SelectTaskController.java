package TaskManager.ControllerClasses;

import TaskManager.MenuClasses.IView;
import TaskManager.ModelClasses.IModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SelectTaskController {

    private IView gui;
    private IModel model;
    private int raz;

    public SelectTaskController(IView localView, IModel localModel){
        gui = localView;
        model = localModel;
        raz=0;
        gui.setSelectionListener(new SelectionListener());
    }

    private class SelectionListener implements ListSelectionListener { // for JList
        @Override
        public void valueChanged(ListSelectionEvent e) {
            select();
        }
    }

    private void select(){
        if(raz==1){
            raz=0;
            return;
        }
        raz=1;
        String name = gui.getSelectedTask();
        if(name==null){
            return;
        }
        String chosenTask = model.getTaskByName(name);
        gui.setTaskToFields(chosenTask);
    }
}
