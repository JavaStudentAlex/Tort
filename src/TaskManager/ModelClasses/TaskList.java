package TaskManager.ModelClasses;

import java.io.Serializable;
import java.util.*;

public abstract class TaskList implements Iterable<Task>,Cloneable, Serializable{
    protected int size = 0;

    protected int countChanging = 0;

    public abstract Task getTask(int i);
    public abstract void add(Task ex);
    public abstract boolean remove(Task ex);

    public int size(){
        return size;
    }

    ////////////////////////////////////////////Iterator - start

    protected abstract class TaskListIterator implements Iterator<Task>{

        protected int localCountChanging = countChanging;
        protected  int nextIndex=0;

        protected boolean assertChangingCount(){
            return localCountChanging != countChanging;
        }

        protected abstract void setItreratorToBegin();

        protected void secureOfChangigng(){
            if(assertChangingCount()){
                setItreratorToBegin();
            }
        }

        @Override
        public boolean hasNext(){
            return nextIndex<size;
        }

        @Override
        public abstract Task next();

        @Override
        public abstract void remove();
    }

    ////////////////////////////////////////////Iterator - end

    @Override
    public abstract TaskListIterator iterator();

    @Override
    public String toString() {
        Iterator<Task> tempItr= this.iterator();
        String result="";
        while(tempItr.hasNext()){
            result += tempItr.next().toString()+"\n";
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskList)) return false;

        TaskList tasks = (TaskList) o;

        if(tasks.size!=size){return false;}

        //Iterator<Task> iterThis = this.iterator();
        Iterator<Task> iterThose = tasks.iterator();
        while(iterThose.hasNext()){
            Task temp = iterThose.next();
            if(!this.contains(temp)){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result  = 0;
        Iterator<Task> itr = this.iterator();
        while(itr.hasNext()){
            Task buffer = itr.next();
            result+=buffer==null?0:buffer.hashCode();
        }
        return result;
    }

    private boolean contains(Task e){
        Iterator<Task> itr = this.iterator();
        while(itr.hasNext()){
            if(e.equals(itr.next())){
                return true;
            }
        }
        return false;
    }


}
