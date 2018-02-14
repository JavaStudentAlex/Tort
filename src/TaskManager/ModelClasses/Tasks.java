package TaskManager.ModelClasses;

import TaskManager.Exceptions.EndTimeInvalidException;
import TaskManager.Exceptions.IntervalInvalidException;
import TaskManager.Exceptions.StartTimeInvalidException;
import TaskManager.Exceptions.TimeInvalidException;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class Tasks {

    private static Logger logger = Logger.getLogger(Tasks.class);

    public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end){
        ArrayList<Task> result=new ArrayList<Task>();
        Iterator<Task> iterOnColl = tasks.iterator();
        while(iterOnColl.hasNext()){
            Task temp = iterOnColl.next();
            Date next = temp.nextTimeAfter(start);
            if(isNotGreaterThanFinish(next,end)){
                result.add(temp);
            }
        }
        return result;
    }

    private static boolean isNotGreaterThanFinish(Date current,Date end){
        return current!=null && (end.equals(current) || end.after(current));
    }

    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks,Date start, Date end){
        SortedMap <Date, Set<Task>> result = new TreeMap<Date,Set<Task>>();
        Iterator<Task> chooseTasks = incoming(tasks,start,end).iterator();
        while(chooseTasks.hasNext()){
            Task tempTask = chooseTasks.next();
            for(Date curTime = tempTask.nextTimeAfter(start);       isNotGreaterThanFinish(curTime,end);     curTime = tempTask.nextTimeAfter(curTime)){
               if(result.containsKey(curTime)){
                   Set<Task> tempOldSet = result.get(curTime);
                   tempOldSet.add(tempTask);
               }
               else{
                   Set<Task> tempNewSet =new HashSet<Task>();
                   tempNewSet.add(tempTask);
                   result.put(curTime,tempNewSet);
               }
            }

        }
        return result;
    }

    public static void writeTaskToSpecForm(Task tempTask, ObjectOutputStream outBuffer) throws IOException{
        String name = tempTask.getTitle();
        byte[] arrayBytes = name.getBytes();
        outBuffer.writeInt(arrayBytes.length);//length name
        outBuffer.write(arrayBytes,0,arrayBytes.length);
        boolean isActive = tempTask.isActive();
        outBuffer.writeBoolean(isActive);
        outBuffer.writeInt(tempTask.getRepeatInterval());
        if(tempTask.isRepeated()){//TimeControll
            outBuffer.writeLong(tempTask.getStartTime().getTime());
            outBuffer.writeLong(tempTask.getEndTime().getTime());
        }
        else{
            outBuffer.writeLong(tempTask.getTime().getTime());
        }
    }

    public static Task readTaskFromSpecForm(ObjectInputStream inBuffer) throws IOException{
        int arrayBytesLength = inBuffer.readInt();
        byte[] arrayName = new byte[arrayBytesLength];
        inBuffer.read(arrayName,0,arrayBytesLength);
        String name = new String(arrayName);
        boolean isActive = inBuffer.readBoolean();
        int interval = inBuffer.readInt();
        Date startTime = new Date(inBuffer.readLong());
        Task result = null;
        if(interval!=0){
            Date endTime = new Date(inBuffer.readLong());
            result = new Task(name,startTime,endTime,interval);
        }
        else{
            result = new Task(name,startTime);
        }
        result.setActive(isActive);
        return result;
    }

    public static String writeTaskAsString(Task task){
        StringBuilder gusterBeiter = new StringBuilder(); //HI - HI
        final char dQuots = (char)34;
        String lookingUp = ""+dQuots;
        gusterBeiter.append(dQuots+task.getTitle().replace(lookingUp,(""+dQuots+dQuots))+dQuots);
        if(task.isRepeated()){
            gusterBeiter.append(" from " + formDateForWriting(task.getStartTime()) + " to " + formDateForWriting(task.getEndTime())).append(" every " + formInterval(task.getRepeatInterval()));
        }
        else{
            gusterBeiter.append(" at " + formDateForWriting(task.getTime()));
        }
        if(!task.isActive()){
            gusterBeiter.append(" inactive");
        }
        return gusterBeiter.toString();
    }

    private static String formDateForWriting(Date dateTime){
        GregorianCalendar date = new GregorianCalendar();
        date.setTime(dateTime);
        int year = date.get(Calendar.YEAR), month = date.get(Calendar.MONTH)+1,day = date.get(Calendar.DAY_OF_MONTH), hours = date.get(Calendar.HOUR_OF_DAY), minutes = date.get(Calendar.MINUTE), seconds = date.get(Calendar.SECOND);
        StringBuilder result=new StringBuilder("["+formDate(""+year,4)+
                      ":"+formDate(""+month,2)+
                      ":"+formDate(""+day,2)+
                      " "+formDate(""+hours,2)+
                      ":"+formDate(""+minutes,2)+
                      ":"+formDate(""+seconds,2)+
                      "."+formDate(""+date.get(Calendar.MILLISECOND),3)+"]");
        return result.toString();
    }

    private static String formDate(String number, int shemeLength){
        StringBuilder temp = new StringBuilder(number);
        if(temp.length()<shemeLength){
            while(temp.length()<shemeLength){
                temp.insert(0,"0");
            }
        }
        return temp.toString();
    }

    private static String formInterval(int intervalArg){
        String result="[";
        ArrayList<Integer> capacity = new ArrayList<Integer>();
        capacity.add(60*60*24);
        ArrayList<Integer> interval = new ArrayList<Integer>();
        interval.add(intervalArg);
        String[] nameValue = new String[]{"day","hour","minute","second"};
        int[] res = new int[4];
        res[0] = getDown(interval,capacity,24);
        res[1] = getDown(interval,capacity,60);
        res[2] = getDown(interval,capacity,60);
        res[3] = interval.get(interval.size()-1).intValue();
        for(int i=0;i<res.length;++i){
            String nameVls = nameValue[i];
            if(res[i]==0){
                continue;
            }
            if(res[i]>1){
                nameVls+="s";
            }
            String separator="";
            if(i!=3){
                separator=" ";
            }
            result+=res[i]+" "+nameVls+separator;
        }
        return result+"]";
    }

    private static int getDown(ArrayList<Integer> interval,ArrayList<Integer> capacity, int downArg){
        int elemArg = interval.get(interval.size()-1).intValue(),elemCap =  capacity.get(capacity.size()-1).intValue();
        int temp = elemArg/elemCap;
        if(temp>0){
            interval.add(elemArg%elemCap);
        }
        capacity.add(elemCap/downArg);
        return temp;
    }

    public static Task parseTask(String source) throws StartTimeInvalidException,EndTimeInvalidException,
            IntervalInvalidException,TimeInvalidException{
        Task res=null;
        String name = getName(source);
        String db = ""+((char)34)+((char)34);
        name.replace(db,"");
        int start = source.indexOf("[");
        int finish = source.indexOf("]");
        if(isRepeated(source)){
            Date startTime, endTime;
            int interval;
            try {
                startTime = parseDate(source.substring(start + 1, finish));
            }catch (Exception ex){
                throw new StartTimeInvalidException();
            }
            String tempStr = source.substring(finish+1,source.length());
            start = tempStr.indexOf("[");
            finish = tempStr.indexOf("]");
            String secData = tempStr.substring(start+1,finish);
            try {
                endTime = parseDate(secData);
            }catch (Exception e){
                throw new EndTimeInvalidException();
            }
            tempStr = tempStr.substring(finish+1,tempStr.length());
            start = tempStr.indexOf("[");
            finish = tempStr.indexOf("]");
            try {
                interval = getInterval(tempStr.substring(start + 1, finish));
            }catch (Exception e){
                throw new IntervalInvalidException();
            }

            if (startTime.after(endTime)) {
                throw new StartTimeInvalidException();
            }
            res=new Task(name,startTime,endTime,interval);
        }
        else{
            Date time;
            try{
            time = parseDate(source.substring(start+1,finish));}
            catch (Exception e){
                throw new TimeInvalidException();
            }
            res=new Task(name,time);
        }
        res.setActive(isActive(source));
        return res;
    }

    private static String getName(String source){
        String find = "" + ((char)34);
        int start  = source.indexOf(find);
        int finish = source.lastIndexOf(find);
        String name = source.substring(start+1,finish);
        return name;
    }

    private static boolean isRepeated(String resource){
        return !resource.contains("at");
    }

    private static boolean isActive(String resource){
        return !resource.contains("inactive");
    }

    public static Date parseDate(String source) throws Exception{
        String tempStr = source;
        int year = parseInt(tempStr.substring(0,4));
        int mounth = parseInt(tempStr.substring(5,7))-1;
        if(mounth<0 || mounth>12){throw new Exception();}
        int day = parseInt(tempStr.substring(8,10));
        if(day>31 || day <0){throw new Exception();}
        int hours = parseInt(tempStr.substring(11,13));
        if(hours<0 || hours >=24){throw new Exception();}
        int minutes = parseInt(tempStr.substring(14,16));
        if(minutes<0 || minutes>=60){throw new Exception();}
        int seconds = parseInt(tempStr.substring(17,19));
        if(seconds<0 || seconds>=60){throw new Exception();}
        int miliseconds = parseInt(tempStr.substring(20,23));
        if(miliseconds<0 || miliseconds >=1000){throw new Exception();}
        GregorianCalendar date = new GregorianCalendar(year,mounth,day,hours,minutes,seconds);
        Date current = new Date(date.getTimeInMillis()+miliseconds);
        return current;
    }

    private static int parseInt(String znach){
        return Integer.parseInt(znach);
    }

    private static int getInterval(String source){
        String temp = source;
        String[] measures = new String[] {"day","hour","minute","second"};
        int[] measuresValue = new int[4];
        for(int i=0;i<measures.length;++i){
            measuresValue[i]=getInteger(measures[i],temp);
            temp=getStringAfter(measures[i],"s",temp);
        }
        return measuresValue[0]*24*60*60+measuresValue[1]*60*60+measuresValue[2]*60+measuresValue[3];
    }

    private static int getInteger(String word, String source){
        if(!source.contains(word)){
            return 0;
        }
        String number = source.substring(0,source.indexOf(word)-1);
        if(number.charAt(0)==' '){
            number=number.substring(1,number.length());
        }
        if(number.charAt(number.length()-1)==' '){
            number=number.substring(0,number.length()-1);
        }
        return Integer.parseInt(number);
    }

    private static String getStringAfter(String word, String adds, String source){
        if(!source.contains(word)){
            return source;
        }
        int index = source.indexOf(word)+word.length();
        if(source.contains(word+adds)){
            index+= adds.length();
        }
        if(source.length()!=index){
            ++index;
        }
        return source.substring(index,source.length());
    }
}
