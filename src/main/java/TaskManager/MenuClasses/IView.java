package TaskManager.MenuClasses;

import TaskManager.Cheker;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;

/**
 * The interface of user view
 */
public interface IView {

    /**
     * The method that returns the string with new task
     * @return the string
     */
    public String getNewTask();

    /**
     * The method that renews tasks view for user
     * @param tasks - new tasks in string format
     */
    public void renewTasks(String[] tasks);

    /**
     * The method sets handler for choosing task action
     * @param listener - the handler
     */
    public void setSelectionListener(ListSelectionListener listener);

    /**
     * Sets task info into the relevant fields
     * @param task - the task in string format
     */
    public void setTaskToFields(String task);

    /**
     * The method shows to user the error message with text {@code message}
     * @param message - the text message for user
     */
    public void showErrorMessage(String message);

    /**
     * The method clears the fields and transform it to the primary format
     */
    public void clearAll();

    /**
     * The method sets handler of window_closing action
     * @param adapter - the handler
     */
    public void setWindowCLosing(WindowAdapter adapter);

    /**
     * The method returns the value of field for start time in calendar part
     * @return the string with time
     */
    public String getStrartTimeCalendar();

    /**
     * The method returns the value of field for end time in calendar part
     * @return the string with time
     */
    public String getEndTimeCalendar();

    /**
     * The method sets the handler for user looking_up the calendar
     * @param listener - the handler
     */
    public void setActionLookingCalendarListener(ActionListener listener);

    /**
     * The method that pushes dates to perform them to user
     * @param dates - the array of dates in string format
     */
    public void setElemsToDatesList(ArrayList<String> dates);

    /**
     * The method sets the handler for action of date by user selection
     * @param listener - the handler
     */
    public void setChosenDatesListener(ListSelectionListener listener);

    /**
     * The method that pushes the schedule of chosen date
     * @param tasks - array of pairs 'time' - 'title'(schedule)
     */
    public void setSchedule(ArrayList<String> tasks);

    /**
     * The method that returns the task, selected by user
     * @return the string title of relevant task
     */
    public String getSelectedTask();

    /**
     * The method that returns the date selected by user
     * @return the date in string format
     */
    public String getSelectedDate();

    /**
     * The method sets handler of adding new task
     * @param listener - the handler
     */
    public void setAddListener(ActionListener listener);

    /**
     * The method sets handler of changing new task
     * @param listener- the handler
     */
    public void setChangeListener(ActionListener listener);

    /**
     * The method sets handler of removing new task
     * @param listener - the handler
     */
    public void setDeleteListener(ActionListener listener);

    /**
     * The method that tie the cheker with user interface to output the info messages
     * @param cheker - the checker instance
     */
    public void connectCheker(Cheker cheker);
}
