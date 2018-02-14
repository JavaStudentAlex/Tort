package TaskManager.ModelClasses;

import TaskManager.Cheker;
import TaskManager.Exceptions.*;

import java.util.GregorianCalendar;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public interface IModel {

    public String[] getAllTasks();

    public String getTaskByName(String title);

    public void addTask(String Task) throws SameTaskException,StartTimeInvalidException,EndTimeInvalidException,
            IntervalInvalidException,TimeInvalidException;
    public void changeTask(String oldTask, String newTask) throws SameTaskException,StartTimeInvalidException,EndTimeInvalidException,
            IntervalInvalidException,TimeInvalidException;

    public void deleteTask(String deleted);

    public void saveTasks();

    public SortedMap<GregorianCalendar, TreeMap<GregorianCalendar,Set<String>>>
    getSchedule(String fromDate, String toDate)throws StartTimeInvalidException,EndTimeInvalidException;

    public void setChecker(Cheker checker);
}
