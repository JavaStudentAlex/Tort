package TaskManager.ControllerClasses;

import TaskManager.MenuClasses.IView;
import TaskManager.ModelClasses.IModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The class, that controll saving tasks on closing
 */
public class SavingClosingController {

    /**
     * The {@code IView } user interface
     */
    private IView gui;

    /**
     * The {@code IMode } business interface
     */
    private IModel model;

    /**
     * The constructor of {@code SavingClosingController } create a new object and connect it with {@code gui}.
     * @param localView the user interface object
     * @param localModel the budiness layer object
     * @see IView#setWindowCLosing(WindowAdapter)
     */
    public SavingClosingController(IView localView, IModel localModel){
        gui = localView;
        model = localModel;
        gui.setWindowCLosing(new SavingOnClosing());
    }

    /**
     * The class that handle event on user interface.
     */
    private class SavingOnClosing extends WindowAdapter {

        /**
         * The method that handle closing window event and save all tasks
         * @param e the event object
         * @see IModel#saveTasks()
         */
        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            model.saveTasks();
        }
    }
}
