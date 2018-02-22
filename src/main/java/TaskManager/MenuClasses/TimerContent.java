package TaskManager.MenuClasses;

import javax.swing.*;
import java.awt.*;

/**
 * The class for control time info
 */
public class TimerContent {


    /**
     * Manager for changing two different timer menus
     */
    private CardLayout manager;
    /**
     * Panels with timer menus
     */
    private JPanel singlePanel, multiplePanel;

    /**
     * The name of current timer menu
     */
    private String currentTimer;

    /**
     * The fields for task's time info
     */
    private JTextField timeField,startTimeField, endTimeField, intervalField;

    /**
     * The names of timer menu and formats for fields
     */
    public static final String SINGLE="Single", MULTIPLE = "Multiple", formatTime = "yyyy-mm-dd hh:mm:ss.msmsms",
            formatInterval = "0 days 0 hours 0 minutes 0 seconds";

    /**
     * The main panel
     */
    public JPanel timerRootPanel;

    /**
     * The constructor that creates a new instance and initialize elems
     * @see TimerContent#initializeContent()
     */
    public TimerContent(){
        initializeContent();
        timerRootPanel.revalidate();
    }

    /**
     * Method create and place functional elements
     * @see PanelFactory
     * @see TimerContent#initializeSinglePanel()
     * @see TimerContent#initializeMultiplePanel()
     */
    private void initializeContent(){
        timerRootPanel = new JPanel();

        singlePanel = PanelFactory.createPanel(SINGLE);
        multiplePanel = PanelFactory.createPanel(MULTIPLE);

        initializeSinglePanel();
        initializeMultiplePanel();

        manager = new CardLayout();
        timerRootPanel.setLayout(manager);

        timerRootPanel.add(singlePanel,SINGLE);
        timerRootPanel.add(multiplePanel,MULTIPLE);

        currentTimer = SINGLE;

        manager.show(timerRootPanel,currentTimer);
    }

    /**
     * Them method creates and locates the single panel of timer
     * @see Constrains
     */
    private void initializeSinglePanel(){
        timeField= new JTextField(formatTime,40);
        singlePanel.setLayout(new GridBagLayout());
        GridBagConstraints loc = Constrains.getLocator();
        loc.fill = GridBagConstraints.NONE;
        loc.weightx = 0.25;
        Constrains.setLocation(loc,0,0);
        singlePanel.add(new Label("Time : "),loc);
        loc.weightx=0.75;
        Constrains.setLocation(loc,1,0);
        singlePanel.add(timeField,loc);
    }

    /**
     * Them method creates and locates the multiple panel of timer
     * @see Constrains
     */
    private void initializeMultiplePanel(){
        startTimeField = new JTextField(formatTime,40);
        endTimeField = new JTextField(formatTime,40);
        intervalField = new JTextField(formatInterval,40);
        multiplePanel.setLayout(new GridBagLayout());
        GridBagConstraints loc = Constrains.getLocator();
        GridBagConstraints loc1 = Constrains.getLocator();
        loc.fill = loc1.fill = GridBagConstraints.NONE;
        loc.weightx = 0.25;
        loc1.weightx = 0.75;

        Constrains.setLocation(loc,0,0);
        multiplePanel.add(new JLabel("Start time : "),loc);

        Constrains.setLocation(loc1,1,0);
        multiplePanel.add(startTimeField,loc1);

        Constrains.setLocation(loc,0,1);
        multiplePanel.add(new JLabel("Interval : "),loc);

        Constrains.setLocation(loc1,1,1);
        multiplePanel.add(intervalField,loc1);

        Constrains.setLocation(loc,0,2);
        multiplePanel.add(new JLabel("End time : "),loc);

        Constrains.setLocation(loc1,1,2);
        multiplePanel.add(endTimeField,loc1);
    }

    /**
     * The method assert equaling current timer menu name to the name in param. And returns relevant true if it is equal
     * and false if not
     * @param name - the name of asserted menu's name
     * @return return true of false
     */
    public boolean assertCurrent(String name){
        return currentTimer.equals(name);
    }

    /**
     * The method show on the main panel the menu with name in param
     * @param name - the name of menu that should be shown
     */
    public void showCard(String name){
        currentTimer = name;
        manager.show(timerRootPanel,currentTimer);
    }

    /**
     * Thos method clear fields on the panel that now is shown on the main panel
     */
    public void clearFields(){
        if(currentTimer.equals(SINGLE)){
            timeField.setText(formatTime);
        }
        else{
            startTimeField.setText(formatTime);
            endTimeField.setText(formatTime);
            intervalField.setText(formatInterval);
        }
    }

    /**
     * The method validate, transform the time from the fields to special string and return in format
     * {@code at [yyyy-mm-dd hh:mm:ss.msmsms] (inactive)} or
     * {@code from [yyyy-mm-dd hh:mm:ss.msmsms] to [yyyy-mm-dd hh:mm:ss.msmsms]
     * every [0 day(s) 0 hour(s) 0 minute(s) 0 second(s)] (inactive)}. If the fields are not valid the method returns ""
     * @return the string with the time or empty string
     */
    public String getNewTask(){
        if(!validate()){
            return "";
        }
        StringBuilder result = new StringBuilder();
        if(currentTimer==MULTIPLE){
            result.append(" from [").append(startTimeField.getText()).append("] to [").append(endTimeField.getText()).
                    append("] every [").append(intervalField.getText()).append("] ").toString();
        }
        else{
            result.append(" at [").append(timeField.getText()).append("] ");
        }
        return result.toString();
    }

    /**
     * The method asserts are the fields not empty. If it is empty the method pushes the error message and return false.
     * If validation is successful returns true;
     * @return true or false
     */
    private boolean validate(){
        if(currentTimer==MULTIPLE){
            if(startTimeField.getText().isEmpty()){
                View.showErrorMessage(timerRootPanel,"Fill please start time field!");
                return false;
            }
            if(intervalField.getText().isEmpty()){
                View.showErrorMessage(timerRootPanel,"Fill please interval field!");
                return false;
            }
            if(endTimeField.getText().isEmpty()){
                View.showErrorMessage(timerRootPanel,"Fill please end time field!");
                return false;
            }
        }
        else if(timeField.getText().isEmpty()){
            View.showErrorMessage(timerRootPanel,"Fill please time field!");
            return false;
        }
        return true;
    }

    /**
     * The method parse string {@code time} from params and push info to relevant fields on the panel. Also this method
     * change menu timer panels according to value of {@code repeatTask} param. If param is true so the method show
     * panel with {@code MULTIPLE} name, if not  - {@code SINGLE} name.
     * @param time - the source string with time info
     * @param repeatTask - the id for menu panels
     * @see EditionContent#getValueFromSepars(String, int, int)
     */
    public void setTimeToFields(String time, boolean repeatTask){
        int start,finish;
        start=time.indexOf("[")+1;
        finish=time.indexOf("]");
        clearFields();
        if(repeatTask){
            if(currentTimer==SINGLE){
                manager.show(timerRootPanel,MULTIPLE);
                currentTimer = MULTIPLE;
            }

            String tempStartTime = EditionContent.getValueFromSepars(time,start,finish);
            time = EditionContent.getValueFromSepars(time,finish+1,time.length());

            start = time.indexOf("[")+1;
            finish = time.indexOf("]");

            String tempEndTime = EditionContent.getValueFromSepars(time,start,finish);
            time = EditionContent.getValueFromSepars(time,finish+1,time.length());

            start = time.indexOf("[")+1;
            finish = time.indexOf("]");


            String tempInterval = EditionContent.getValueFromSepars(time,start,finish);
            startTimeField.setText(tempStartTime);
            endTimeField.setText(tempEndTime);
            intervalField.setText(tempInterval);
        }
        else{
            if(currentTimer==MULTIPLE){
                manager.show(timerRootPanel,SINGLE);
                currentTimer = SINGLE;
            }
            String tempTime = EditionContent.getValueFromSepars(time,time.indexOf("[")+1,time.indexOf("]"));
            timeField.setText(tempTime);
        }
        timerRootPanel.revalidate();
    }
}
