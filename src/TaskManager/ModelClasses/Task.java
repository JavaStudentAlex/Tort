package TaskManager.ModelClasses;

import java.io.*;
import java.util.Date;

public class Task implements Cloneable, Comparable<Task>, Serializable{
    private String title;
    private boolean active = false;
    private TimeControll controller;

    //////////////////////////////////////////////////////////Controller - start

    private class TimeControll {
        boolean repeat = false;
        Date startTime = null;
        Date endTime = null;
        long interval = 0;

        TimeControll(Date time){
            endTime = startTime = (Date)time.clone();
        }

        TimeControll(Date start, Date finish, int interval){
            repeat = true;
            startTime = (Date)start.clone();
            endTime = (Date)finish.clone();
            this.interval = interval*1000;
        }

        TimeControll(TimeControll temp){
            repeat = temp.repeat;
            interval = temp.interval;
            startTime = (Date)temp.startTime.clone();
            endTime = (Date)temp.endTime.clone();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TimeControll)) return false;
            TimeControll that = (TimeControll) o;
            if (repeat != that.repeat) return false;
            if(isRepeated()){
                if(!startTime.equals(that.startTime)){return false;}
                if(!endTime.equals(that.endTime)){return false;}
                if(interval!=that.interval){return  false;}
            }
            else{
                if(!startTime.equals(that.startTime)){return false;}
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = (repeat ? 1 : 0);
            result = 31 * result + startTime.hashCode();
            result = 31 * result + endTime.hashCode();
            result = 31 * result + (int) (interval ^ (interval >>> 32));
            return result;
        }

        @Override
        public String toString() {
            String result ="  repeated : " + repeat +  (repeat ? "  start : " + startTime.toString() + "  end : " + endTime.toString() + "  interval : " + interval +" ms":"   time : " + startTime.toString());
            return result;
        }
    }

    //////////////////////////////////////////////////////////Controller - end


    private Task(String title,boolean active, TimeControll controller ){
        this.title = title;
        this.active = active;
        this.controller = new TimeControll(controller);
    }

    public Task(String title, Date time){
        this.title = title;
        controller = new TimeControll(time);
    }

    public Task(String title, Date start, Date end, int interval){
        this.title = title;
        controller = new TimeControll(start,end,interval);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean act) {
        active = act;
    }

    public Date getTime() {

        return (Date) controller.startTime.clone();
    }

    public void setTime(Date time){
        controller=new TimeControll(time);
    }

    public void setTime(Date start, Date end, int interval){
        controller = new TimeControll(start,end,interval);
    }

    public Date getStartTime() {
        return getTime();
    }

    public Date getEndTime() {
        return (Date)controller.endTime.clone();
    }

    private long getDateTime(Date date){
        return date.getTime();
    }

    public int getRepeatInterval() {
        return (int)controller.interval/1000;
    }

    public boolean isRepeated() {
        return controller.repeat;
    }

    private Date nextTimeCalc(Date curTime){
        long start = getDateTime(controller.startTime);
        long interval = controller.interval;
        long current = getDateTime(curTime);
        return new Date(start + ((current-start)/interval+1)*interval);
    }
    public Date nextTimeAfter(Date current) {
        if(isActive()){
            Date result = controller.startTime;
            if(current.before(result)){
                return result;
            }
            else if(isRepeated()){
                result = nextTimeCalc(current);
            }
            if(result.after(current) && (result.before(controller.endTime) || result.equals(controller.endTime))){
                return result;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (active != task.active) return false;
        if (!title.equals(task.title)) return false;
        return controller.equals(task.controller);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + controller.hashCode();
        return result;
    }

    @Override
    public Task clone(){
        try {
            Task result = (Task) super.clone();
            if(!isRepeated()){
                result.controller = new TimeControll(controller.startTime);
            }
            else{
                result.controller = new TimeControll(controller.startTime,controller.endTime,(int)controller.interval/1000);
            }
            result.controller.repeat = controller.repeat;
            return result;
        }
        catch (Exception e){
            throw new Error("By cloning Task object",e);
        }
    }

    @Override
    public String toString() {
        String result = "name : " + title + "  active : " + active + controller.toString();
        return result;
    }

    @Override
    public int compareTo(Task temp) {
        if(this.equals(temp)){
            return 0;
        }
        return controller.startTime.compareTo(temp.controller.startTime);
    }
}
