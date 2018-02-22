package TaskManager.ControllerClasses;

import TaskManager.MenuClasses.IView;
import TaskManager.ModelClasses.IModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *The element, that control adding a new task in the model repository
 */
public class AddController {
    /**
     * The {@code View} user interface
     */
    private IView gui;
    /**
     * The {@code Model} business interface
     */
    private IModel model;

    /**
     * Constructs the object of {@code AddController} class and connect {@code Addlistener} object to {@code IView}
     * user interface.
     * @param localView Organize the interface for user.
     * @param localModel Organize the business layer.
     */
    public AddController(IView localView, IModel localModel){
        gui = localView;
        model = localModel;
        gui.setAddListener(new AddListener());
    }

    /**
     * Get user input, put it into the {@code model} and reload all tasks.
     * If the {@code res} is empty string - the method returns.
     * If we catch the {@cose exception}, we will output the error message to {@code gui} with text of message
     * of this exception.
     * @see IView#getNewTask()
     * @see IView#showErrorMessage(String)
     * @see IModel#addTask(String)
     */
    private void adding(){
        String res = gui.getNewTask();
        if(res.isEmpty()){
            return;
        }
        try{
            model.addTask(res);
        }catch (Exception ex){
            gui.showErrorMessage(ex.getMessage());
            return;
        }

        gui.clearAll();
        Presenter.reloadTasksList(gui,model);
    }

    /**
     * The event listener, that react on adding a new task.
     */
    private class AddListener implements ActionListener {

        /**
         * The method that handle the event
         * @param e The object of event
         * @see AddController#adding()
         */
        @Override
        public void actionPerformed(ActionEvent e) {  // switch buttons
            adding();
        }
    }
}
