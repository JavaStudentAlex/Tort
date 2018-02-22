package TaskManager;

import TaskManager.ModelClasses.Task;
import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The class that manage task's happening control
 */
public class Cheker{

    /**
     * The class that control task's happening
     */
    private class TaskChekThread extends Thread{
        /**
         * The current task
         */
        private Task task;

        /**
         * The time she should happen
         */
        private Date nextTime;

        /**
         * The manager of this controller
         */
        private Cheker own;

        /**
         * The method that start running with starting new thread of this class. Here happen checking current time and
         * {@code nextTime}. If the task happens, we got new {@code nextTime}. If it is null the checker is interrupted
         * and remove, else - we push a new time to {@code nextTime}
         */
        @Override
        public void run() {
            while(true){
                Date cur = new Date();
                if(cur.equals(nextTime) || cur.after(nextTime)){
                    own.setTaskIsNow(task.getTitle());
                    nextTime = task.nextTimeAfter(cur);
                    if(nextTime==null){
                        own.deleteTask(task.getTitle());
                        return;
                    }
                }
            }
        }

        /**
         * The constructor of new instance that also initialize elements
         * @param task - the current task
         * @param cheker - the manager of the checker
         */
        public TaskChekThread(Task task,Cheker cheker){
            this.own = cheker;
            this.task = task;
            nextTime = task.nextTimeAfter(new Date());


        }


    }

    /**
     * The container of all checkers
     */
    HashMap<String,TaskChekThread> threads;

    /**
     * The user panel for messages pushing
     */
    private JPanel root;

    /**
     * The manager constructor that create main container
     */
    public Cheker(){
        threads = new HashMap<String ,TaskChekThread>();
    }

    /**
     * The method add new active tasks, by creating new checker in manager's container and run it like a new thread
     * @param task - the active task
     */
    public void addActiveTask(Task task){
        if(task.nextTimeAfter(new Date())==null){
            return;
        }
        String title = task.getTitle();

        TaskChekThread potok = new TaskChekThread(task,this);
        potok.start();

        threads.put(title,potok);

    }

    /**
     * The method find and interrupt and remove the thread wit task that have title equal to {@code taskTitle}
     * @param taskTitle - the title of controlled task
     */
    public void deleteTask(String taskTitle){
        if(threads.containsKey(taskTitle)){
            TaskChekThread thread = threads.get(taskTitle);
            thread.interrupt();
            threads.remove(taskTitle);
        }
    }

    /**
     * The method clear the main manager's container and interrupt all threads
     */
    public void deleteAll(){
        for(Map.Entry<String,TaskChekThread> temp : threads.entrySet()){
            temp.getValue().interrupt();
        }
        threads.clear();

    }

    /**
     * The method output the message about happening task
     * @param title - the title of happened task
     */
    public void setTaskIsNow(String title){
        JOptionPane.showInternalMessageDialog(root,title + " IS NOW");
    }

    /**
     * The method set the root panel, where will appear messages about happening tasks
     * @param panel - the root panel for dialogue messages
     */
    public void setPanel(JPanel panel){
        root = panel;
    }
}
