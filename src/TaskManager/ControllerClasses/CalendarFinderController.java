package TaskManager.ControllerClasses;

import TaskManager.MenuClasses.IView;
import TaskManager.ModelClasses.IModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class CalendarFinderController {

    private IView gui;
    private IModel model;
    private SortedMap<GregorianCalendar, TreeMap<GregorianCalendar,Set<String>>> info;
    private int datesRaz;

    public CalendarFinderController(IView localView, IModel localModel){
        gui = localView;
        model = localModel;

        gui.setActionLookingCalendarListener(new FindCalendar());
        gui.setChosenDatesListener(new ChooseDate());
    }

    private class ChooseDate implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            selectDate();
        }
    }

    private class FindCalendar implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            scheduleLooking();
        }
    }

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

    private String dateToString(GregorianCalendar date){
        return new StringBuilder().append(getStringWeekDate(date.get(GregorianCalendar.DAY_OF_WEEK))).
                append("(").append(date.get(GregorianCalendar.YEAR)).append("-").
                append(date.get(GregorianCalendar.MONTH)+1).
                append("-").append(date.get(GregorianCalendar.DAY_OF_MONTH)).append(")").toString();
    }

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

    private GregorianCalendar parseGregCalDate(String date){
        int year = Integer.parseInt(date.substring(0,date.indexOf("-")));
        date = date.substring(date.indexOf("-")+1,date.length());
        int mounth = Integer.parseInt(date.substring(0,date.indexOf("-")));
        date = date.substring(date.indexOf("-")+1,date.length());
        int day = Integer.parseInt(date);
        GregorianCalendar result = new GregorianCalendar(year,mounth-1,day);

        return result;
    }

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

    private String setScheduleToString(GregorianCalendar time, String title){
        String adding;
        adding = new StringBuilder().append(time.get(GregorianCalendar.HOUR_OF_DAY)).append(":").append(time.get(GregorianCalendar.MINUTE)).
                append(":").append(time.get(GregorianCalendar.SECOND)).append(".").append(time.get(GregorianCalendar.MILLISECOND)).
                append(" - title : ").append(title).toString();
        return adding;
    }

}
