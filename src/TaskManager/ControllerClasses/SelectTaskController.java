package TaskManager.ControllerClasses;

import TaskManager.MenuClasses.IView;
import TaskManager.ModelClasses.IModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The class that control selecting tasks
 */
public class SelectTaskController {

    /**
     * The {@code IView} user interface
     */
    private IView gui;

    /**
     * The {@ode IModel} business layer
     */
    private IModel model;

    /**
     * The controller of choosing number
     */
    private int raz;

    /**
     * The constructor of {@code SelectTaskController} that create a new object of this class and connect
     * it to user interface.
     * @param localView the user interface
     * @param localModel the business layer
     */
    public SelectTaskController(IView localView, IModel localModel){
        gui = localView;
        model = localModel;
        raz=0;
        gui.setSelectionListener(new SelectionListener());
    }

    /**
     * A class listener that handle task choosing event
     */
    private class SelectionListener implements ListSelectionListener {

        /**
         * The method that handle choosing event
         * @param e the event object
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            select();
        }
    }

    /**
     * The method that fill in info of task chosen by user.
     * If the {@code name} is null - method returns.
     * @see IView#getSelectedTask()
     * @see IModel#getTaskByName(String)
     * @see IView#setTaskToFields(String)
     */
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
