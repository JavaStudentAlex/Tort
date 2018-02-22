package TaskManager.MenuClasses;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The class that includes all components for calendar working.
 */
public class CalendarContent {

    /**
     * The main container.
     */
    public JPanel calendarRootPanel;
    /**
     * Containers for lists of dates - {@code datesRootPanel} and schedule - {@code scheduleRootPanel}.
     */
    public JPanel scheduleRootPanel, datesRootPanel;

    /**
     * Lists of dates - {@code datesList} and schedule - {@code scheduleList}.
     */
    private JList<String> datesList,scheduleList;

    /**
     * Models for lists of dates - {@code datesListModel} and schedule - {@code scheduleListModel}.
     */
    private DefaultListModel<String> datesListModel,scheduleListModel;

    /**
     * Controll button to find calendar - {@code findSchedule} and to reset fields - {@code reset}.
     */
    private JButton findSchedule,reset;

    /**
     * Fields for (start/end)Time input.
     */
    private JTextField startTimeField, endTimeField;

    /**
     * The constructor that create a new instance and deploy into the instance the functional elements for calendar.
     * @see CalendarContent#initializeElems()
     */
    public CalendarContent(){
        initializeElems();
    }


    /**
     * The method that deploy all functional elements for calendar together.
     * @see CalendarContent#placeFieldsAndLabels()
     * @see CalendarContent#placeButtons()
     * @see CalendarContent#placeListPanels()
     * @see PanelFactory#createPanel(String)
     */
    private void initializeElems(){
        calendarRootPanel = PanelFactory.createPanel(View.menuCalendar);
        calendarRootPanel.setLayout(new GridBagLayout());
        placeFieldsAndLabels();
        placeButtons();
        placeListPanels();
    }

    /**
     * The method that bring all fields and lists to the start format
     * @see CalendarContent#clearList(DefaultListModel)
     */
    public void clear(){
        startTimeField.setText(TimerContent.formatTime);
        endTimeField.setText(TimerContent.formatTime);
        clearList(datesListModel);
        clearList(scheduleListModel);
        calendarRootPanel.revalidate();
    }

    /**
     * The method that delete all elements from the relevant list by working with it model.
     * @param model - the model of the list
     */
    private void clearList(DefaultListModel<String> model){
        model.removeAllElements();
    }

    /**
     * The method that create field- {@code startTimeField, endTimeField} and label-
     * elements and place it on the {@code calendarRootPanel}
     * @see Constrains#setLocation(GridBagConstraints, int, int)
     * @see Constrains#setSize(GridBagConstraints, int, int)
     * @see Constrains#getLocator()
     */
    private void placeFieldsAndLabels(){
        startTimeField = new JTextField(TimerContent.formatTime,40);
        endTimeField = new JTextField(TimerContent.formatTime,40);
        GridBagConstraints locator = Constrains.getLocator();
        locator.weighty = 0.01;
        locator.weightx = 0.35;
        locator.anchor = GridBagConstraints.CENTER;
        Constrains.setLocation(locator,0,0);
        calendarRootPanel.add(new JLabel("From : "),locator);
        Constrains.setLocation(locator,1,0);
        calendarRootPanel.add(new JLabel("To : "),locator);
        Constrains.setLocation(locator,0,1);
        calendarRootPanel.add(startTimeField,locator);
        Constrains.setLocation(locator,1,1);
        calendarRootPanel.add(endTimeField,locator);
    }

    /**
     * The method that create buttons {@code findSchedule,reset}and place it on the
     * {@code calendarRootPanel}. Also {@#code reset} get the action listener that uses {@code clear()} method.
     * @see Constrains#getLocator()
     * @see Constrains#setLocation(GridBagConstraints, int, int)
     * @see Constrains#setSize(GridBagConstraints, int, int)
     */
    private void placeButtons(){
        findSchedule = new JButton("Find");
        reset= new JButton("Reset");
        GridBagConstraints locator = Constrains.getLocator();
        locator.weighty = 0.01;
        locator.weightx=0.3;
        locator.anchor = GridBagConstraints.CENTER;
        Constrains.setLocation(locator,2,0);
        calendarRootPanel.add(findSchedule,locator);
        Constrains.setLocation(locator,2,1);
        calendarRootPanel.add(reset,locator);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
    }

    /**
     * This method create and place the panels {@code scheduleRootPanel, datesRootPanel}
     * with lists {@code datesList,scheduleList} on the {@code calendarRootPanel}.
     * @see Constrains#getLocator()
     * @see Constrains#setLocation(GridBagConstraints, int, int)
     * @see Constrains#setSize(GridBagConstraints, int, int
     * @see CalendarContent#createPanelsWithLists()
     */
    private void placeListPanels(){
        createPanelsWithLists();
        GridBagConstraints locator = Constrains.getLocator();
        locator.weightx=1;
        locator.weighty=0.49;
        Constrains.setLocation(locator,0,2);
        Constrains.setSize(locator,3,1);
        calendarRootPanel.add(datesRootPanel,locator);
        Constrains.setLocation(locator,0,3);
        calendarRootPanel.add(scheduleRootPanel,locator);
    }

    /**
     * This method create {@code scheduleRootPanel, datesRootPanel} panels with {@code datesList,scheduleList} lists
     * based on {@code datesListModel,scheduleListModel} models.
     * @see ListFactory#createList(DefaultListModel)
     */
    private void createPanelsWithLists(){
        datesRootPanel = PanelFactory.createPanel("Dates");
        scheduleRootPanel = PanelFactory.createPanel("Schedule");

        datesListModel = new DefaultListModel<String>();
        scheduleListModel = new DefaultListModel<String>();

        datesList = ListFactory.createList(datesListModel);
        scheduleList = ListFactory.createList(scheduleListModel);

        datesRootPanel.setLayout(new GridLayout(1,1));
        scheduleRootPanel.setLayout(new GridLayout(1,1));

        datesRootPanel.add(datesList);
        scheduleRootPanel.add(scheduleList);
    }

    /**
     * This method return the value of {@code startTimeField} field. If the field is empty the method push error
     * message "Fill please start time field" and returns the empty string.
     * @return empty or filled string
     * @see View#showErrorMessage(String)
     */
    public String getStartTime(){
        if(startTimeField.getText().isEmpty()){
            View.showErrorMessage(calendarRootPanel,"Fill please start time field");
            return "";
        }
        return startTimeField.getText();
    }

    /**
     * This method return the value of {@code endTimeField} field. If the field is empty the method push error
     * message "Fill please end time field" and returns the empty string.
     * @return empty or filled string
     * @see View#showErrorMessage(String)
     */
    public String getEndTime(){
        if(endTimeField.getText().isEmpty()){
            View.showErrorMessage(calendarRootPanel,"Fill please end time field");
            return "";
        }
        return endTimeField.getText();
    }

    /**
     * The method that set the handler for looking_up_the_calendar event on {@code findSchedule}.
     * @param listener - the handler
     */
    public void setFindEvent(ActionListener listener){
        findSchedule.addActionListener(listener);
    }

    /**
     * The method that push array of dates in string format to the {@code datesList}.
     * @param dates - the array of formatted in string dates
     */
    public void setElemsToDatesList(ArrayList<String> dates){
        datesListModel.removeAllElements();
        for(String buf : dates){
            datesListModel.addElement(buf);
        }
    }

    /**
     * The method that set the handler for select_the_date event on {@code datesList}
     * @param listener - the handler
     */
    public void setChosenDatesListener(ListSelectionListener listener){
        datesList.addListSelectionListener(listener);
    }

    public void setSchedule(ArrayList<String> tasks){
        scheduleListModel.removeAllElements();
        for(String buffer : tasks){
            scheduleListModel.addElement(buffer);
        }
    }

    /**
     * The method that return the date. chosen by user
     * @return the string with string formatted date
     */
    public String getSelectedDate(){
        return datesList.getSelectedValue();
    }
}
