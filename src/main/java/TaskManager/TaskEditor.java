package TaskManager;

import TaskManager.ControllerClasses.Presenter;
import TaskManager.MenuClasses.*;
import TaskManager.ModelClasses.Model;

/**
 * The main class of the programme
 */
public class TaskEditor {

    /**
     * The main function that run the {@code Presenter} instance and it pulls running all other components
     * @param args - params of starting programm
     */
    public static void main(String[] args){
        Presenter presenter = new Presenter(new View(),new Model());
    }
}
