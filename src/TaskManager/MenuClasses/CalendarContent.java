package TaskManager.MenuClasses;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CalendarContent {

    public JPanel calendarRootPanel;
    public JPanel scheduleRootPanel, datesRootPanel;
    private JList<String> datesList,scheduleList;
    private DefaultListModel<String> datesListModel,scheduleListModel;
    private JButton findSchedule,reset;
    private JTextField startTimeField, endTimeField;

    public CalendarContent(){
        initializeElems();
    }

    private void initializeElems(){
        calendarRootPanel = PanelFactory.createPanel(View.menuCalendar);
        calendarRootPanel.setLayout(new GridBagLayout());
        placeFieldsAndLabels();
        placeButtons();
        placeListPanels();
    }

    public void clear(){
        startTimeField.setText(TimerContent.formatTime);
        endTimeField.setText(TimerContent.formatTime);
        clearList(datesListModel);
        clearList(scheduleListModel);
        calendarRootPanel.revalidate();
    }

    private void clearList(DefaultListModel<String> model){
        model.removeAllElements();
    }

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

    public String getStartTime(){
        if(startTimeField.getText().isEmpty()){
            View.showErrorMessage(calendarRootPanel,"Fill please start time field");
            return "";
        }
        return startTimeField.getText();
    }

    public String getEndTime(){
        if(endTimeField.getText().isEmpty()){
            View.showErrorMessage(calendarRootPanel,"Fill please end time field");
            return "";
        }
        return endTimeField.getText();
    }

    public void setFindEvent(ActionListener listener){
        findSchedule.addActionListener(listener);
    }

    public void setElemsToDatesList(ArrayList<String> dates){
        datesListModel.removeAllElements();
        for(String buf : dates){
            datesListModel.addElement(buf);
        }
    }

    public void setChosenDatesListener(ListSelectionListener listener){
        datesList.addListSelectionListener(listener);
    }

    public void setSchedule(ArrayList<String> tasks){
        scheduleListModel.removeAllElements();
        for(String buffer : tasks){
            scheduleListModel.addElement(buffer);
        }
    }

    public String getSelectedDate(){
        return datesList.getSelectedValue();
    }
}
