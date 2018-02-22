package TaskManager.ControllerClasses;

import TaskManager.MenuClasses.IView;
import TaskManager.ModelClasses.IModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * The class that control the behaviour of calendar content in (@code gui)
 */
public class CalendarFinderController {

    /**
     * The user interface object
     * @see IView
     */
    private IView gui;

    /**
     * The model layer object
     * @see IModel
     */
    private IModel model;
    /**
     * The map of dates and relevant schedule(time and name of task) of each day, in that active tasks can happen
     */
    private SortedMap<GregorianCalendar, TreeMap<GregorianCalendar,Set<String>>> info;
    /**
     * The variable to control the number of time selecting on dates list on calendar content
     */
    private int datesRaz;

    /**
     * The constructor of the class, that create the instance and connect event handlers. {@code localView}
     * and {@code localModel} tie with relevant classes objects {@code gui} and {@code model}
     * @see ChooseDate
     * @see FindCalendar
     * @param localView - current user interface
     * @param localModel - current business layer
     */
    public CalendarFinderController(IView localView, IModel localModel){
        gui = localView;
        model = localModel;

        gui.setActionLookingCalendarListener(new FindCalendar());
        gui.setChosenDatesListener(new ChooseDate());
    }

    /**
     * The class that control choosing date in date's list in calendar content
     */
    private class ChooseDate implements ListSelectionListener {
        /**
         * The method that handle event of choosing new element in date's list
         * @param e - object of the event
         * @see CalendarFinderController#selectDate()
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            selectDate();
        }
    }

    /**
     * The class that control looking up task's calendar
     */
    private class FindCalendar implements ActionListener {

        /**
         * The method that handle event(pressing or etc) looking up the dates and schedules
         * @param e - the object of the event
         * @see CalendarFinderController#scheduleLooking()
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            scheduleLooking();
        }
    }

    /**
     * The main method that look up the schedule of tasks. {@code fromDate} - is the start date and the {@code toDate}
     * - is the finish. We get map of dates and relevant lists(time  + task's title), save it into the variable
     * {@code info}, than get only the dates and put in to gui.
     * @see IView#getStrartTimeCalendar()
     * @see IView#getEndTimeCalendar()
     * @see IView#showErrorMessage(String)
     * @see IModel#getSchedule(String, String)
     * @see IView#setElemsToDatesList(ArrayList)
     */
    private void scheduleLooking(){
        String fromDate = gui.getStrartTimeCalendar();
        if(fromDate.isEmpty()){
            return;
        }
        String toDate = gui.getEndTimeCalendar();
        if(toDate.isEmpty()){
            return;
        }

        try{
            info = model.getSchedule(fromDate,toDate);
        }catch (Exception ex){
            gui.showErrorMessage(ex.getMessage());
            return;
        }

        ArrayList<String> dates = new ArrayList<String>();

        for(Map.Entry<GregorianCalendar,TreeMap<GregorianCalendar,Set<String>>> temp : info.entrySet()){
            dates.add(dateToString(temp.getKey()));
        }

        gui.setElemsToDatesList(dates);
    }

    /**
     * This method transform {@code GregorianCalendar} date to String format in view : yyyy-mm-dd
     * @param date - the date object, that we should transform
     * @return the string of transformed date
     */
    private String dateToString(GregorianCalendar date){
        return new StringBuilder().append(getStringWeekDate(date.get(GregorianCalendar.DAY_OF_WEEK))).
                append("(").append(date.get(GregorianCalendar.YEAR)).append("-").
                append(date.get(GregorianCalendar.MONTH)+1).
                append("-").append(date.get(GregorianCalendar.DAY_OF_MONTH)).append(")").toString();
    }

    /**
     * This method that identify the day of week by int id {@code date}.
     * @param date - the int identificator
     * @return the name of week day
     * @see GregorianCalendar#get(int)
     */
    private String getStringWeekDate(int date){
        String res="";
        switch (date){
            case 1: res="Sunday"; break;
            case 2: res="Monday"; break;
            case 3: res="Tuesday"; break;
            case 4: res="Wednesday"; break;
            case 5: res="Thurthday"; break;
            case 6: res="Friday"; break;
            case 7: res="Saturday"; break;
        }
        return res;
    }

    /**
     * The main method of choosing date. The user selected the date and here we identify what the date is and got
     * schedule(time  + task's title) and push it into the user interface
     * @see IView#getSelectedDate()
     * @see CalendarFinderController#getSchedule(TreeMap)
     * @see IView#setSchedule(ArrayList)
     */
    private void selectDate(){
        if(datesRaz==1){
            datesRaz=0;
            return;
        }
        datesRaz=1;
        String chosen = gui.getSelectedDate();
        if(chosen==null){
            return;
        }
        GregorianCalendar chosenDate =
                parseGregCalDate(chosen.substring(chosen.indexOf("(")+1,chosen.indexOf(")")));

        ArrayList<String> schedule = getSchedule(info.get(chosenDate));
        gui.setSchedule(schedule);
    }

    /**
     * The method that identify GregorianCalendar date by string , that we got from user interface
     * @param date - the string chosen by user
     * @return the GregorianCalendar object, that always into {@code info} and has it's relevant schedule
     */
    private GregorianCalendar parseGregCalDate(String date){
        int year = Integer.parseInt(date.substring(0,date.indexOf("-")));
        date = date.substring(date.indexOf("-")+1,date.length());
        int mounth = Integer.parseInt(date.substring(0,date.indexOf("-")));
        date = date.substring(date.indexOf("-")+1,date.length());
        int day = Integer.parseInt(date);
        GregorianCalendar result = new GregorianCalendar(year,mounth-1,day);

        return result;
    }

    /**
     * The method that transform map(GreagorianCalendar, String ) int the ArrayList of strings ('time' + 'task.title')
     * @param dateSchedule - the schedule of the relevant date from info
     * @return the arrayList of strings
     * @see CalendarFinderController#setScheduleToString(GregorianCalendar, String)
     */
    private  ArrayList<String> getSchedule(TreeMap<GregorianCalendar,Set<String>> dateSchedule){
        ArrayList<String> result = new ArrayList<String>();

        for(Map.Entry<GregorianCalendar,Set<String>> temp : dateSchedule.entrySet()){
            GregorianCalendar key = temp.getKey();
            Set<String> tasks = temp.getValue();

            for(String title : tasks){
                result.add(setScheduleToString(key,title));
            }
        }
        return result;
    }

    /**
     * The method that transform one pair GregCal - String to String('time' + 'title')
     * @param time - the time
     * @param title - the title of some task
     * @return the transformed string
     */
    private String setScheduleToString(GregorianCalendar time, String title){
        String adding;
        adding = new StringBuilder().append(time.get(GregorianCalendar.HOUR_OF_DAY)).append(":").append(time.get(GregorianCalendar.MINUTE)).
                append(":").append(time.get(GregorianCalendar.SECOND)).append(".").append(time.get(GregorianCalendar.MILLISECOND)).
                append(" - title : ").append(title).toString();
        return adding;
    }

}
