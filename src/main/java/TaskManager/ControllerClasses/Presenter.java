package TaskManager.ControllerClasses;

import TaskManager.Cheker;
import TaskManager.MenuClasses.IView;
import TaskManager.ModelClasses.IModel;

/**
 * The class that control the behaviour of user interface and have some handlers of user's actions
 */
public class Presenter {

    /**
     * Add activity controller
     */
    private AddController adder;

    /**
     * Change activity controller
     */
    private ChangeController changer;

    /**
     * Remove activity controller
     */
    private DeleteController deleter;

    /**
     * The controller of task selection
     */
    private SelectTaskController selector;

    /**
     * Controller of close_window_tasks_saving
     */
    private SavingClosingController saver;

    /**
     * Calendar activity controller
     */
    private CalendarFinderController finder;

    /**
     * The constructor of the class that create a new instance. Here create also all controllers and reload all tasks
     * for user.
     * @param gui - user interface
     * @param model - business layer
     * @see Presenter#reloadTasksList(IView, IModel)
     */
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

    /**
     * The method that reload tasks in user interface after some user activity (renew tasks). It takes all tasks from
     * {@code model} and put it to {@code gui}.
     * @param gui - user interface
     * @param model - business layer
     */
    public static void reloadTasksList(IView gui,IModel model){
        String[] tasks = model.getAllTasks();
        gui.renewTasks(tasks);
    }
}
