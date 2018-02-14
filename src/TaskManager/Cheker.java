package TaskManager;

import TaskManager.ModelClasses.Task;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Cheker{

    private class TaskChekThread extends Thread{
        private Task task;
        private Date nextTime;
        private Cheker own;

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

        public TaskChekThread(Task task,Cheker cheker){
            this.own = cheker;
            this.task = task;
            nextTime = task.nextTimeAfter(new Date());


        }


    }

    private long ladtMod=0;

    HashMap<String,TaskChekThread> threads;

    private JPanel root;

    public Cheker(){
        threads = new HashMap<String ,TaskChekThread>();
    }

    private JFrame getWindow() {
        JFrame result = new JFrame();
        result.setTitle("Cheking");
        Toolkit thisToolkit = Toolkit.getDefaultToolkit();
        Dimension size = thisToolkit.getScreenSize();
        result.setBounds(0,0,200,200);
        result.add(new JLabel("Cheking tasks"));
        return result;
    }

    /*public void setCloseWindow(){
        window.setVisible(false);
        window=null;
    }*/

    public void addActiveTask(Task task){
        if(task.nextTimeAfter(new Date())==null){
            return;
        }
        String title = task.getTitle();

        TaskChekThread potok = new TaskChekThread(task,this);
        potok.start();

        threads.put(title,potok);

    }

    public void deleteTask(String taskTitle){
        if(threads.containsKey(taskTitle)){
            TaskChekThread thread = threads.get(taskTitle);
            thread.interrupt();
            threads.remove(taskTitle);
        }
    }

    public void deleteAll(){
        for(Map.Entry<String,TaskChekThread> temp : threads.entrySet()){
            temp.getValue().interrupt();
        }
        threads.clear();

    }

    public void setTaskIsNow(String title){
        JOptionPane.showInternalMessageDialog(root,title + " IS NOW");
    }

    public void setPanel(JPanel panel){
        root = panel;
    }
}
