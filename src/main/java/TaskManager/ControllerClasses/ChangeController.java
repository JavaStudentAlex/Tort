package TaskManager.ControllerClasses;

import TaskManager.MenuClasses.IView;
import TaskManager.ModelClasses.IModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The class, that control changing the tasks by user
 */
public class ChangeController {

    /**
     * The {@code IView} user interface
     */
    private IView gui;

    /**
     * The {@code IModel} business interface
     */
    private IModel model;


    /**
     * The constructor of {@code ChangeController} class object.
     * Here connect the {@code ChangeListener} to user interface.
     * @param localGui Organize the interface for user.
     * @param localModel Organize the business layer.
     */
    public ChangeController(IView localGui, IModel localModel){
        gui = localGui;
        model = localModel;
        gui.setChangeListener(new ChangeListener());
    }

    /**
     * Get user input and selected task, by user, put it into the {@code model} and reload all tasks.
     * If the {@code newTask} is empty string or {@code oldTask} is {@code null}- the method returns.
     * If we catch the {@cose exception}, we will output the error message to {@code gui} with text of message
     * of this exception.
     * @see IView#getNewTask()
     * @see IView#getSelectedTask()
     * @see IView#showErrorMessage(String)
     * @see IModel#changeTask(String, String)
     */
    private void changing(){
        String newTask = gui.getNewTask();
        if(newTask.isEmpty()){
            return;
        }
        String oldTask = gui.getSelectedTask();
        if(oldTask==null || oldTask.isEmpty()){
            return;
        }
        try{
            model.changeTask(oldTask,newTask);
        }catch (Exception ex){
            gui.showErrorMessage(ex.getMessage());
            return;
        }

        gui.clearAll();
        Presenter.reloadTasksList(gui,model);
    }

    /**
     * The event listener, that react on changing task.
     */
    private class ChangeListener implements ActionListener {

        /**
         * The method that handle the event
         * @param e The object of event
         * @see ChangeController#changing()
         */
        @Override
        public void actionPerformed(ActionEvent e) {  // switch buttons
            changing();
        }
    }
}
