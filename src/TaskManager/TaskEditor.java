package TaskManager;

import TaskManager.ControllerClasses.Presenter;
import TaskManager.MenuClasses.*;
import TaskManager.ModelClasses.Model;

public class TaskEditor {

    public static void main(String[] args){
        Presenter presenter = new Presenter(new View(),new Model());
    }
}
