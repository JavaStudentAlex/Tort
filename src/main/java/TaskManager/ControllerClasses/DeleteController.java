package TaskManager.ControllerClasses;

import TaskManager.MenuClasses.IView;
import TaskManager.ModelClasses.IModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * The class, that control removing task by user.
 */
public class DeleteController {

    /**
     * The {@code IView} user interface
     */
    private IView gui;

    /**
     * The {@code IModel} business interface
     */
    private IModel model;

    /**
     * The constructor of {@code DeleteController} class object.
     * Here connect the {@code DeleteListener} to user interface.
     * @param localGui Organize the interface for user.
     * @param localModel Organize the business layer.
     */
    public DeleteController(IView localGui, IModel localModel){
        gui = localGui;
        model = localModel;
        gui.setDeleteListener(new DeleteListener());
    }

    /**
     * The event listener, that react on deleting task.
     */
    private class DeleteListener implements ActionListener { // for buttons
        /**
         * The method that handle the event
         * @param e The object of event
         * @see DeleteController#deleting()
         */
        @Override
        public void actionPerformed(ActionEvent e) {  // switch buttons
            deleting();
        }
    }

    /**
     * Get task,selected by user, put it into the {@code model} and reload all tasks.
     * If the {@code deleted} is empty string - the method returns.
     * @see IView#getSelectedTask()
     * @see IModel#deleteTask(String)
     * @see Presenter#reloadTasksList(IView, IModel)
     */
    private void deleting(){
        String deleted = gui.getSelectedTask();
        if (deleted==null || deleted.isEmpty()){
            return;
        }
        model.deleteTask(deleted);

        gui.clearAll();
        Presenter.reloadTasksList(gui,model);
    }
}
