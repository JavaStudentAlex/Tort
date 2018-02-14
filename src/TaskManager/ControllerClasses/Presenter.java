package TaskManager.ControllerClasses;

import TaskManager.Cheker;
import TaskManager.MenuClasses.IView;
import TaskManager.ModelClasses.IModel;

public class Presenter {

    private AddController adder;
    private ChangeController changer;
    private DeleteController deleter;
    private SelectTaskController selector;
    private SavingClosingController saver;
    private CalendarFinderController finder;

    public Presenter(IView gui, IModel model){
        Cheker cheker = new Cheker();
        gui.connectCheker(cheker);
        model.setChecker(cheker);
        adder = new AddController(gui,model);
        selector = new SelectTaskController(gui,model);
        changer = new ChangeController(gui,model);
        deleter = new DeleteController(gui,model);
        saver = new SavingClosingController(gui,model);
        finder = new CalendarFinderController(gui,model);
        reloadTasksList(gui,model);
    }
    public static void reloadTasksList(IView gui,IModel model){
        String[] tasks = model.getAllTasks();
        gui.renewTasks(tasks);
    }
}
