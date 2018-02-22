package TaskManager.ModelClasses;

import TaskManager.Exceptions.*;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * The class for working with write/parse strings and tasks
 */
public class Tasks {

    private static Logger logger = Logger.getLogger(Tasks.class);

    /**
     * The method returns the collection of tasks that happens between {@code start} and {@code finish}
     * @param tasks - iterator on collection of all tasks
     * @param start - start time in interval
     * @param end - end time in interval
     * @return the iterator on collection with all active tasks that happens in those period
     * @see Task#nextTimeAfter(Date)
     * @see Tasks#isNotGreaterThanFinish(Date, Date)
     */
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

    /**
     * the method assert that {@code current } is not null and before of equal {}@code end
     * @param current - current time
     * @param end - the upper border
     * @return the true if the conditions happen, else - false
     */
    private static boolean isNotGreaterThanFinish(Date current,Date end){
        return current!=null && (end.equals(current) || end.after(current));
    }

    /**
     * The Method returns the map of format : 'date, Set(task)'. It is the schedule of dates and we identify by the date
     * the set of tasks that happend in those time
     * @param tasks - iterator on all task's collection
     * @param start - start time of period
     * @param end - enmd time of period
     * @return the {@code Map<Date,Set<Task>>} instance
     */
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

    /**
     * The method write task {@code task} to string in format : {@code "Title" at [yyyy-mm-dd hh:mm:ss.msmsms] (inactive)}
     * or {@code "Title" from [yyyy-mm-dd hh:mm:ss.msmsms] to [yyyy-mm-dd hh:mm:ss.msmsms] every
     * [0 day(s) 0 hour(s) 0 minute(s) 0 second(s)] (inactive)} and return that string
     * @param task - the {@code Task} instance
     * @return the task special formatted to string
     * @see Tasks#formDateForWriting(Date)
     * @see Task
     */
    public static String writeTaskAsString(Task task){
        StringBuilder gusterBeiter = new StringBuilder(); //HI - HI
        final char dQuots = (char)34;
        String lookingUp = ""+dQuots;
        gusterBeiter.append(dQuots+task.getTitle().replace(lookingUp,(""+dQuots+dQuots))+dQuots);
        if(task.isRepeated()){
            gusterBeiter.append(" from " + formDateForWriting(task.getStartTime()) + " to " +
                    formDateForWriting(task.getEndTime())).append(" every " + formInterval(task.getRepeatInterval()));
        }
        else{
            gusterBeiter.append(" at " + formDateForWriting(task.getTime()));
        }
        if(!task.isActive()){
            gusterBeiter.append(" inactive");
        }
        return gusterBeiter.toString();
    }

    /**
     * The method transform the {@code Date} structure to string in form {@code [yyyy-mm-dd hh:mm:ss.msmsms]}
     * @param dateTime - the control time
     * @return the time formatted to string
     */
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

    /**
     * the method transform the number to the number with {@code schemeLength} count of symbols
     * @param number - string with number
     * @param shemeLength - the number of symbols
     * @return the time formatted to string
     */
    private static String formDate(String number, int shemeLength){
        StringBuilder temp = new StringBuilder(number);
        if(temp.length()<shemeLength){
            while(temp.length()<shemeLength){
                temp.insert(0,"0");
            }
        }
        return temp.toString();
    }

    /**
     * The method that form interval to string value and return it in format :
     * [0 day(s) 0 hour(s) 0 minute(s) 0 second(s)]
     * @param intervalArg - the number of interval's seconds count
     * @return the interval formatted to string
     */
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

    /**
     * The single descent with noting result of div, mode operations and the {@code downArg}
     * @param interval - array of residues of seconds
     * @param capacity - the capacity of each down measure level
     * @param downArg - the divider
     * @return the result of div operation
     */
    private static int getDown(ArrayList<Integer> interval,ArrayList<Integer> capacity, int downArg){
        int elemArg = interval.get(interval.size()-1).intValue(),elemCap =  capacity.get(capacity.size()-1).intValue();
        int temp = elemArg/elemCap;
        if(temp>0){
            interval.add(elemArg%elemCap);
        }
        capacity.add(elemCap/downArg);
        return temp;
    }

    /**
     * The method parses the task {#code source } formatted to string
     * @param source
     * @return the task instance
     * @throws StartTimeInvalidException - invalid start time input
     * @throws EndTimeInvalidException - invalid end time input
     * @throws IntervalInvalidException - invalid interval input
     * @throws TimeInvalidException - invalid time input
     * @see Tasks#parseDate(String)
     */
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

    /**
     * The method returns the title of task formatted to string - {@ode source }
     * @param source - the source task's string
     * @return the string of task's title
     */
    private static String getName(String source){
        String find = "" + ((char)34);
        int start  = source.indexOf(find);
        int finish = source.lastIndexOf(find);
        String name = source.substring(start+1,finish);
        return name;
    }

    /**
     * The method assert is task repeated
     * @param resource - the source task's string
     * @return true or false
     */
    private static boolean isRepeated(String resource){
        return !resource.contains("at");
    }

    /**
     * The method assert is task active
     * @param resource - the source task's string
     * @return true or false
     */
    private static boolean isActive(String resource){
        return !resource.contains("inactive");
    }

    /**
     * The method parse date from the source string {@code source}
     * @param source - the string with {@code Date} info
     * @return the Date structure
     * @throws ParseDateException - if the source has invalid format
     */
    public static Date parseDate(String source) throws ParseDateException{
        String tempStr = source;
        int year = parseInt(tempStr.substring(0,4));
        int mounth = parseInt(tempStr.substring(5,7))-1;
        if(mounth<0 || mounth>12){throw new ParseDateException();}
        int day = parseInt(tempStr.substring(8,10));
        if(day>31 || day <0){throw new ParseDateException();}
        int hours = parseInt(tempStr.substring(11,13));
        if(hours<0 || hours >=24){throw new ParseDateException();}
        int minutes = parseInt(tempStr.substring(14,16));
        if(minutes<0 || minutes>=60){throw new ParseDateException();}
        int seconds = parseInt(tempStr.substring(17,19));
        if(seconds<0 || seconds>=60){throw new ParseDateException();}
        int miliseconds = parseInt(tempStr.substring(20,23));
        if(miliseconds<0 || miliseconds >=1000){throw new ParseDateException();}
        GregorianCalendar date = new GregorianCalendar(year,mounth,day,hours,minutes,seconds);
        Date current = new Date(date.getTimeInMillis()+miliseconds);
        return current;
    }

    /**
     * The method returns the int valus of the string {@code znach}
     * @param znach - the source string with int value into
     * @return the itn value
     */
    private static int parseInt(String znach){
        return Integer.parseInt(znach);
    }

    /**
     * The method parse interval from the string {@code source}
     * @param source - the source string with interval info into
     * @return the int value of task's interval
     */
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

    /**
     * The method returns the int number before the string {@code word}
     * @param word - the id string
     * @param source - the string with word and id value
     * @return the int value from {@code source}
     */
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

    /**
     * The method returns the substring of {@code source} after string {@code word}
     * @param word - the id string
     * @param adds - the suffix can be after the word
     * @param source - the source string that should be divided
     * @return the substring of source after {@code work} + may be {@code adds}
     */
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
