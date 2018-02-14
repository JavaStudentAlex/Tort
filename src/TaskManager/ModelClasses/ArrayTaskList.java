package TaskManager.ModelClasses;

import TaskManager.Exceptions.NoSuchTaskException;

import java.util.Arrays;

public class ArrayTaskList extends TaskList{
    private Task[] data;
    private static final int startCapacity = 12;

    public ArrayTaskList() {
        data = new Task[startCapacity];
    }

    public ArrayTaskList(int capasity){
        if(capasity>0){
            data = new Task[capasity];
        }
    }

    /*public ArrayTaskList(Collection<Task> ex){
        data  = Arrays.copyOf(ex.toArray(),startCapacity);
        size = ex.size();
    }*/

    private void secureCapacity(int localSize){
        if(localSize>data.length){
            data = Arrays.copyOf(data,(localSize*3)/2+1);
        }
    }

    public void add(Task ex){
        if(ex==null){
            throw new NullPointerException("Null pointer to the Object");
        }
        secureCapacity(size+1);
        data[size++] = ex;
    }

    protected void replaceElements(int index){
        int numberElems = size-index-1;
        if(numberElems>0){
            System.arraycopy(data,index+1,data,index,numberElems);
        }
        data[--size] = null;
    }

    @Override
    public boolean remove( Task ex){
        if (ex != null) {
            for(int i = 0;i<size;++i){
                if(data[i].equals(ex)){
                    replaceElements(i);
                    return true;
                }
            }
        }
        else{
            for(int i = 0;i<size;++i){
                if(data[i]==null){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean remove(int index){
        if(index<0||index>=size){
            throw new IndexOutOfBoundsException("Try to remove Task in nonexistent index : " + index);
        }
        return remove(data[index]);
    }

    private void assertRange(int index){
        if(index<0 || index>=size ){
            throw new IndexOutOfBoundsException("Index : " + index + " Size : " + size);
        }
    }

    @Override
    public  Task getTask(int index){
        assertRange(index);
        return data[index];
    }


    @Override
    public ArrayTaskListIterator iterator() {
        return new ArrayTaskListIterator();
    }

    ////////////////////////////////////////////Iterator - start

    private class ArrayTaskListIterator extends TaskListIterator{
        private int lastReturnedIndex=-1;

        @Override
        public Task next() {
            secureOfChangigng();
            int tempIndex = nextIndex;
            if(!hasNext()){
                throw new NoSuchTaskException("You try to go to nonexistent Task!");
            }
            ++nextIndex;
            lastReturnedIndex=tempIndex;
            return data[lastReturnedIndex];
        }

        @Override
        public void remove() {
            secureOfChangigng();
            if(lastReturnedIndex<0){
                throw new NoSuchTaskException("You try to remove nonexistent Task!");
            }
            ArrayTaskList.this.remove(lastReturnedIndex);
            nextIndex = lastReturnedIndex;
            lastReturnedIndex = -1;
            localCountChanging = ++countChanging;
        }

        @Override
        protected void setItreratorToBegin() {
            nextIndex = 0;
            lastReturnedIndex = -1;
            localCountChanging = countChanging;
        }
    }

    ////////////////////////////////////////////Iterator - end

    @Override
    public ArrayTaskList clone(){
        try{
            ArrayTaskList result = (ArrayTaskList) super.clone();
            result.data = Arrays.copyOf(data,size);
            countChanging = 0;
            return result;
        }catch (Exception e){
            throw new Error("Error in cloning ArrayTaskList");
        }
    }

}
