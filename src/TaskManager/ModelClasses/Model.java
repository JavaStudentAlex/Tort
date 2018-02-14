package TaskManager.ModelClasses;

import TaskManager.Cheker;
import TaskManager.Exceptions.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;


public class Model implements IModel {

    private static Logger logger = Logger.getLogger(Model.class);

    private File mainDir = new File("TaskManager");
    private File tasksDir = new File(mainDir,"tasks");
    private File tasksFile = new File(tasksDir,"tasks.db");

    private HashMap<String, Task> tasks;
    Cheker cheker;



    public Model(){
        assertPath(tasksDir,tasksFile);//asserting file and directories
        this.cheker = cheker;
        tasks = new HashMap<String, Task>();
        readAllTasks();
    }

    public void setChecker(Cheker checker){
        this.cheker = checker;
        for(Map.Entry<String,Task> temp : tasks.entrySet()){
            if(temp.getValue().isActive()){
                checker.addActiveTask(temp.getValue());
            }
        }
    }

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
            logger.warn("Can not be here IORxception - Model - readTasks");
        }
    }

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

    public String getTaskByName(String title){
        Task result = tasks.get(title);
        return Tasks.writeTaskAsString(result);
    }

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

    public void deleteTask(String deleted){
        tasks.remove(deleted);
        writeTasksToFile(tasks,tasksFile);

        cheker.deleteTask(deleted);
    }

    public void saveTasks(){
        writeTasksToFile(tasks,tasksFile);
        cheker.deleteAll();
    }

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

        for(Map.Entry<String,Task> temp : tasks.entrySet()){// only the array of tasks
            tempTasks.add(temp.getValue());
        }

        SortedMap<Date,Set<Task>> schedule = Tasks.calendar(tempTasks,start,finish);//get tasks  - dates structure

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

    private Set<String> getTitlesSet(Set<Task> tasks){
        Set<String> result = new HashSet<String >();
        for(Task temp : tasks){
            result.add(temp.getTitle());
        }
        return result;
    }
}
