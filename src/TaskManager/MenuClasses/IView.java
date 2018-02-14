package TaskManager.MenuClasses;

import TaskManager.Cheker;

import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;

public interface IView {

    public String getNewTask();

    public void renewTasks(String[] tasks);

    public void setSelectionListener(ListSelectionListener listener);

    public void setTaskToFields(String task);

    public void showErrorMessage(String message);

    public void clearAll();

    public void setWindowCLosing(WindowAdapter adapter);

    public String getStrartTimeCalendar();

    public String getEndTimeCalendar();

    public void setActionLookingCalendarListener(ActionListener listener);

    public void setElemsToDatesList(ArrayList<String> dates);

    public void setChosenDatesListener(ListSelectionListener listener);

    public void setSchedule(ArrayList<String> tasks);

    public String getSelectedTask();

    public String getSelectedDate();

    public void setAddListener(ActionListener listener);

    public void setChangeListener(ActionListener listener);

    public void setDeleteListener(ActionListener listener);

    public void connectCheker(Cheker cheker);
}
