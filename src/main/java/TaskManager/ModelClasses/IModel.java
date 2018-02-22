package TaskManager.ModelClasses;

import TaskManager.Cheker;
import TaskManager.Exceptions.*;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * The interface of business layer
 */
public interface IModel {

    /**
     * The method return the array of tasks formatted to string
     * @return the string array with size 0 - max int
     */
    public String[] getAllTasks();

    /**
     * The method return the task formatted to string by the {@code title}
     * @param title - the key string to find the task
     * @return the task formatted to string
     */
    public String getTaskByName(String title);

    /**
     * The method add the task {@code Task}
     * @param Task - the task formatted to String
     * @throws SameTaskException - is thrown if there is the task with the same title
     * @throws StartTimeInvalidException - is thrown if input in start time field was not right
     * @throws EndTimeInvalidException - is thrown if input in end time field was not right
     * @throws IntervalInvalidException - is thrown if input in interval field was not right
     * @throws TimeInvalidException - is thrown if input in time field was not right
     */
    public void addTask(String Task) throws SameTaskException,StartTimeInvalidException,EndTimeInvalidException,
            IntervalInvalidException,TimeInvalidException;

    /**
     * The method change attributes in task with title {@code oldTask} to attributes from task {@code newTask}
     * @param oldTask - the title of old task
     * @param newTask - the new task formatted to string
     * @throws SameTaskException - is thrown if there is the task with the same
     * @throws StartTimeInvalidException - is thrown if input in start time field was not right
     * @throws EndTimeInvalidException - is thrown if input in end time field was not right
     * @throws IntervalInvalidException - is thrown if input in interval field was not right
     * @throws TimeInvalidException - is thrown if input in time field was not right
     */
    public void changeTask(String oldTask, String newTask) throws SameTaskException,StartTimeInvalidException,EndTimeInvalidException,
            IntervalInvalidException,TimeInvalidException;

    /**
     * The method that remove task with the shown title
     * @param deleted - the title of shown task
     */
    public void deleteTask(String deleted);

    /**
     * The method that saves all tasks to file system
     */
    public void saveTasks();

    /**
     * The method that gets the schedule map in format Map(date,Map(time,title))
     * @param fromDate - the start looking up time
     * @param toDate - he end looking up time
     * @return the map in format : 'date, schedule' where schedule is in format : 'time,title'
     * @throws StartTimeInvalidException - is thrown if input in start time field was not right
     * @throws EndTimeInvalidException - is thrown if input in end time field was not right
     */
    public SortedMap<GregorianCalendar, TreeMap<GregorianCalendar,Set<String>>>
    getSchedule(String fromDate, String toDate)throws StartTimeInvalidException,EndTimeInvalidException;

    /**
     * the method sets the checker
     * @param checker - the checker
     */
    public void setChecker(Cheker checker);
}
