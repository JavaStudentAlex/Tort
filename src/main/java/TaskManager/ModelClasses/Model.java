package TaskManager.ModelClasses;

import TaskManager.Cheker;
import TaskManager.Exceptions.*;
import org.apache.log4j.Logger;
import java.io.*;
import java.util.*;


/**
 * The class that represent business layer
 */
public class Model implements IModel {

    /**
     * Logger of the class
     */
    private static Logger logger = Logger.getLogger(Model.class);

    /**
     * The working dir in for programme in file system
     */
    private File mainDir = new File("TaskManager");

    /**
     * The dir for saving tasks
     */
    private File tasksDir = new File(mainDir,"tasks");

    /**
     * The file for saving tasks
     */
    private File tasksFile = new File(tasksDir,"tasks.db");

    /**
     * The temp structure for keeping tasks on the work time
     */
    private HashMap<String, Task> tasks;

    /**
     * The chekcker of active tasks
     * @see Cheker
     */
    Cheker cheker;

    /**
     * The constructor that create a new instance. Here is asserted the files {@code taskFile} and directories
     * {@code mainDir,tasksDir} paths and if some problems - create dirs/files. In this method also {@code tasks}
     * is created and is filled by tasks from file system
     * @see Model#readAllTasks()
     * @see Model#assertPath(File, File)
     */
    public Model(){
        assertPath(tasksDir,tasksFile);
        tasks = new HashMap<String, Task>();
        readAllTasks();
    }

    /**
     * The method sets the checker to {@code checker} and push active tasks to that instance
     * @param checker - the checker
     */
    public void setChecker(Cheker checker){
        this.cheker = checker;
        for(Map.Entry<String,Task> temp : tasks.entrySet()){
            if(temp.getValue().isActive()){
                checker.addActiveTask(temp.getValue());
            }
        }
    }

    /**
     * The method assert exists dirs and file and if no - create it.
     * @param directory - working dir
     * @param file - saving file
     */
    private void assertPath(File directory ,File file){
        if(!directory.exists()){
            directory.mkdirs();
        }
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.warn("Problems in file system");
            }
        }
    }

    /**
     * The method read all tasks from the file {@code taskFile} and push it to {@code tasks}
     * @see TaskIO#readTask(DataInputStream)
     */
    private void readAllTasks(){
        try (DataInputStream reader = new DataInputStream(new BufferedInputStream(new FileInputStream(tasksFile)))) {
            while(reader.available()>0){
                Task temp = TaskIO.readTask(reader);
                tasks.put(temp.getTitle(),temp);
            }
        }
        catch (FileNotFoundException ex){
            logger.warn("Can not be here File Not Found - Model - readTasks");
        }
        catch (IOException ex){
            logger.warn("Can not be here IOException - Model - readTasks");
        }
    }

    /**
     * The method returns the array of task's titles
     * @return the array of strings
     */
    public String[] getAllTasks(){
        if(logger.isInfoEnabled()){
            logger.info("Reading all tasks");
        }
        String[] result = new String[tasks.size()];
        int i=0;
        for(Map.Entry<String,Task> temp : tasks.entrySet()){
            result[i++]=temp.getKey();
        }
        return result;
    }

    /**
     * The method return the task formatted to string by the title from param {@code title}
     * @param title - the key string to find the task
     * @return the task that is formatted to string
     * @see Tasks#writeTaskAsString(Task)
     */
    public String getTaskByName(String title){
        Task result = tasks.get(title);
        return Tasks.writeTaskAsString(result);
    }

    /**
     * The method parses the param {@code taskString} to {@code Task} class and add it to the all task's container. Also
     * all tasks rewrite to file
     * @param taskString - new tasks formatted to string
     * @throws SameTaskException - is thrown if there is the task with the same
     * @throws StartTimeInvalidException - is thrown if input in start time field was not right
     * @throws EndTimeInvalidException - is thrown if input in end time field was not right
     * @throws IntervalInvalidException - is thrown if input in interval field was not right
     * @throws TimeInvalidException - is thrown if input in time field was not right
     * @see Model#writeTasksToFile(HashMap, File)
     * @see Tasks#parseTask(String)
     * @see Cheker#addActiveTask(Task)
     */
    public void addTask(String taskString) throws SameTaskException,StartTimeInvalidException,EndTimeInvalidException,
            IntervalInvalidException,TimeInvalidException{
        Task result = Tasks.parseTask(taskString);
        if(logger.isInfoEnabled()){
            logger.info("adding "  + result.toString() + " TASK");
        }
        if(tasks.containsKey(result.getTitle())){
            logger.warn("The same task message " + result.toString() + " TASK");
            throw new SameTaskException();
        }
        tasks.put(result.getTitle(),result);
        writeTasksToFile(tasks,tasksFile);

        if (result.isActive()){
            cheker.addActiveTask(result);
        }
    }

    /**
     * The method find the all task by the title {@code oldTask},remove it,than parse a new tasks from
     * {@code newTask},save it to task's container and rewrite to file all tasks
     * @param oldTask - the title of old task
     * @param newTask - the new task formatted to string
     * @throws SameTaskException - is thrown if there is the task with the same
     * @throws StartTimeInvalidException - is thrown if input in start time field was not right
     * @throws EndTimeInvalidException - is thrown if input in end time field was not right
     * @throws IntervalInvalidException - is thrown if input in interval field was not right
     * @throws TimeInvalidException - is thrown if input in time field was not right
     */
    public void changeTask(String oldTask, String newTask) throws SameTaskException,StartTimeInvalidException,EndTimeInvalidException,
            IntervalInvalidException,TimeInvalidException{
        if(logger.isInfoEnabled()){
            logger.info("Start changing function");
        }
        Task result = Tasks.parseTask(newTask);
        tasks.remove(oldTask);
        tasks.put(result.getTitle(),result);

        cheker.deleteTask(oldTask);

        if(result.isActive()){
            cheker.addActiveTask(result);
        }
        writeTasksToFile(tasks,tasksFile);
    }

    /**
     * The method find the task in task's container by {@code deleted} param and remove it. Than occurs the rewriting
     * of the tasks to file
     * @param deleted - the title of shown task
     */
    public void deleteTask(String deleted){
        tasks.remove(deleted);
        writeTasksToFile(tasks,tasksFile);

        cheker.deleteTask(deleted);
    }

    /**
     * The method rewrite tasks to file and clear the checker list of active tasks
     * @see Cheker#deleteAll()
     */
    public void saveTasks(){
        writeTasksToFile(tasks,tasksFile);
        cheker.deleteAll();
    }

    /**
     * The method write all elements from {@code tasks} to file {@code tasksFile}
     * @param taskList - the pairs in format (title,task) that will be written
     * @param file - the destination file
     * @see TaskIO#writeTask(DataOutputStream, Task)
     */
    private void writeTasksToFile(HashMap<String, Task> taskList, File file){
        try (DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            for(Map.Entry<String,Task> temp : taskList.entrySet()){
                TaskIO.writeTask(writer,temp.getValue());
            }
        }catch(FileNotFoundException ex){
            logger.warn("can not be, we creat file at first");
        }
        catch (IOException ex){
            logger.warn("should not be  - delete task model");
        }
    }

    /**
     * The method parse {@code fromDate} and {@code toDate} to format {@code Date} and find all pairs
     * ('date+time' - 'title'), where date+time is between {@code fromDate} and {@code toDate}. Than pick the date out
     * and group info (time - title ) by the dates. So we create a map formatted by : (date,(date+time,title)). This map
     * we will return
     * @param fromDate - the start looking up time
     * @param toDate - he end looking up time
     * @return the map(date,map(date+time,title))
     * @throws StartTimeInvalidException - is thrown if input in start time field was not right
     * @throws EndTimeInvalidException - is thrown if input in end time field was not right
     * @see Tasks#parseTask(String)
     * @see GregorianCalendar
     */
    public SortedMap<GregorianCalendar, TreeMap<GregorianCalendar,Set<String>>>
    getSchedule(String fromDate,String toDate)throws StartTimeInvalidException,EndTimeInvalidException{
        Date start, finish;
        try{
            start = Tasks.parseDate(fromDate);
        }catch (Exception ex){
            throw new StartTimeInvalidException();
        }

        try{
            finish = Tasks.parseDate(toDate);
        }catch(Exception ex){
            throw new EndTimeInvalidException();
        }

        if(start.after(finish)){
            throw new StartTimeInvalidException();
        }

        int year,mounth,day;

        SortedMap<GregorianCalendar, TreeMap<GregorianCalendar,Set<String>>> result =
                new TreeMap<GregorianCalendar, TreeMap<GregorianCalendar,Set<String>>>();

        Set<Task> buffer;

        TreeMap<GregorianCalendar,Set<String>> added;

        ArrayTaskList tempTasks = new ArrayTaskList();

        Set<String> titles;

        for(Map.Entry<String,Task> temp : tasks.entrySet()){
            tempTasks.add(temp.getValue());
        }

        SortedMap<Date,Set<Task>> schedule = Tasks.calendar(tempTasks,start,finish);

        for(Map.Entry<Date,Set<Task>> temp : schedule.entrySet()){

            buffer = temp.getValue();

            GregorianCalendar dateSplitter = new GregorianCalendar();
            dateSplitter.setTime(temp.getKey());

            year = dateSplitter.get(GregorianCalendar.YEAR);
            mounth = dateSplitter.get(GregorianCalendar.MONTH);
            day = dateSplitter.get(GregorianCalendar.DAY_OF_MONTH);

            GregorianCalendar keyDate = new GregorianCalendar(year,mounth,day);
            GregorianCalendar keyTime = new GregorianCalendar();
            keyTime.setTime(temp.getKey());

            titles = getTitlesSet(buffer);

            if(result.containsKey(keyDate)){
                added = result.get(keyDate);
                added.put(keyTime,titles);
            }
            else{
                added = new TreeMap<GregorianCalendar,Set<String>>();
                added.put(keyTime,titles);
                result.put(keyDate,added);
            }
        }
        return result;
    }

    /**
     * The method return the array of titles of tasks in param {@code tasks}
     * @param tasks - the set of tasks
     * @return the set of task's title
     */
    private Set<String> getTitlesSet(Set<Task> tasks){
        Set<String> result = new HashSet<String >();
        for(Task temp : tasks){
            result.add(temp.getTitle());
        }
        return result;
    }
}
