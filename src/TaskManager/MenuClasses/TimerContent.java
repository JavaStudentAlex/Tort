package TaskManager.MenuClasses;

import javax.swing.*;
import java.awt.*;

public class TimerContent {


    private CardLayout manager;
    private JPanel singlePanel, multiplePanel;
    private String currentTimer;
    private JTextField timeField,startTimeField, endTimeField, intervalField;

    public static final String SINGLE="Single", MULTIPLE = "Multiple", formatTime = "yyyy-mm-dd hh:mm:ss.msmsms",
            formatInterval = "0 days 0 hours 0 minutes 0 seconds";
    public JPanel timerRootPanel;

    public TimerContent(){
        initializeContent();
        timerRootPanel.revalidate();
    }

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

    public boolean assertCurrent(String name){
        return currentTimer.equals(name);
    }

    public void showCard(String name){
        currentTimer = name;
        manager.show(timerRootPanel,currentTimer);
    }

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
            time = EditionContent.concatString(time,finish+1,time.length());

            start = time.indexOf("[")+1;
            finish = time.indexOf("]");

            String tempEndTime = EditionContent.getValueFromSepars(time,start,finish);
            time = EditionContent.concatString(time,finish+1,time.length());

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
