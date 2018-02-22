package TaskManager.MenuClasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The class that keep all elements for edition/removing tasks
 */
public class EditionContent{

    /**
     * The element that control time input
     */
    private TimerContent timerContent;

    /**
     * The main panel of edition
     */
    public JPanel editionRootPanel;

    /**
     * The field for taks's input
     */
    private JTextField titleField;

    /**
     * The buttons for user activity
     */
    private JRadioButton active, not_active, repeat, not_repeat;

    /**
     * The constructor thar create a new instance and initialize elements
     * @see EditionContent#initializeContent()
     */
    public EditionContent(){
        initializeContent();
        editionRootPanel.revalidate();
    }

    /**
     * The method for initializing all elements for edition role
     * @see PanelFactory#createPanel(String)
     * @see EditionContent#initializeTitle()
     * @see EditionContent#initializeRadioButtons()
     * @see EditionContent#initializeTimer()
     */
    private void initializeContent(){
        editionRootPanel = PanelFactory.createPanel("Edition / Info");

        editionRootPanel.setLayout(new GridBagLayout());

        initializeTitle();
        initializeRadioButtons();
        initializeTimer();
    }

    /**
     * The method that add action listener for switch timer panels
     * @param button - the radio button that will be source switch
     * @see TimerContent
     */
    private void setListener(JRadioButton button){
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!timerContent.assertCurrent(button.getText())){
                    timerContent.clearFields();
                    timerContent.showCard(button.getText());
                    editionRootPanel.revalidate();
                }
            }
        });
    }

    /**
     * The method that creates and place title
     * @see Constrains
     */
    private void initializeTitle(){
        titleField = new JTextField(20);
        GridBagConstraints loc1  = Constrains.getLocator();
        GridBagConstraints loc2 = Constrains.getLocator();
        loc1.weighty=0.15;
        loc2.weighty=0.15;
        loc1.weightx = 0.3;
        loc2.weightx=0.7;

        Constrains.setLocation(loc1,0,0);
        editionRootPanel.add(new JLabel("Title : "),loc1);

        Constrains.setLocation(loc2,1,0);
        Constrains.setSize(loc2,2,1);
        editionRootPanel.add(titleField,loc2);

    }

    /**
     * The method that create and place radio buttons {@code active, not_active, repeat, not_repeat}
     * @see Constrains
     */
    private void initializeRadioButtons(){
        GridBagConstraints loc1  = Constrains.getLocator();
        GridBagConstraints loc2 = Constrains.getLocator();
        loc1.weighty=0.15;
        loc2.weighty=0.15;
        loc1.weightx = 0.3;
        loc2.weightx=0.7;

        Constrains.setSize(loc2,1,1);

        Constrains.setLocation(loc1,0,1);
        editionRootPanel.add(new JLabel("Is active : "),loc1);

        active = new JRadioButton("Active");
        active.setSelected(true);
        not_active = new JRadioButton("Not active");
        ButtonGroup actived = new ButtonGroup();
        actived.add(active);
        actived.add(not_active);

        loc2.weightx = 0.35;
        Constrains.setLocation(loc2,1,1);
        editionRootPanel.add(active,loc2);

        Constrains.setLocation(loc2,2,1);
        editionRootPanel.add(not_active,loc2);

        ButtonGroup repeated = new ButtonGroup();
        not_repeat = new JRadioButton(TimerContent.SINGLE);
        not_repeat.setSelected(true);
        repeat = new JRadioButton(TimerContent.MULTIPLE);
        repeated.add(not_repeat);
        repeated.add(repeat);

        Constrains.setLocation(loc1,0,2);
        editionRootPanel.add(new JLabel("Is repeat : "),loc1);

        Constrains.setLocation(loc2,1,2);
        editionRootPanel.add(not_repeat,loc2);

        Constrains.setLocation(loc2,2,2);
        editionRootPanel.add(repeat,loc2);
    }

    /**
     * The method that create and place timer content
     * @see TimerContent
     */
    private void initializeTimer(){
        timerContent = new TimerContent();
        GridBagConstraints loc1  = Constrains.getLocator();
        GridBagConstraints loc2 = Constrains.getLocator();
        loc1.weighty=0.15;
        loc2.weighty=0.15;
        loc1.weightx = 0.3;
        loc2.weightx=0.7;
        loc1=Constrains.getLocator();
        loc1.weighty = 0.55;


        setListener(repeat);
        setListener(not_repeat);
        Constrains.setLocation(loc1,0,3);
        Constrains.setSize(loc1,3,1);
        editionRootPanel.add(timerContent.timerRootPanel,loc1);

    }

    /**
     * The method that return the edition elements to primary state
     */
    public void clearFields(){
        titleField.setText("");
        timerContent.clearFields();
        active.setSelected(true);
        not_repeat.setSelected(true);
        timerContent.showCard(TimerContent.SINGLE);
        editionRootPanel.revalidate();
    }

    /**
     * The method that get here the title, mining of active/not_active and single/multiple info of task. Also from timer
     * content we get time info. We combine all of it to the string in format
     * {@code "Title" at [yyyy-mm-dd hh:mm:ss.msmsms] (inactive)} or
     * {@code "Title" from [yyyy-mm-dd hh:mm:ss.msmsms] to [yyyy-mm-dd hh:mm:ss.msmsms]
     * every [0 day(s) 0 hour(s) 0 minute(s) 0 second(s)] (inactive)}
     * @return the string
     */
    public String getNewTask(){
        if(!validate()){
            return "";
        }
        String time = timerContent.getNewTask();
        if(time.isEmpty()){
            return "";
        }

        String name = titleField.getText();
        String db=""+((char)34);
        String twice = ""+db+db;
        name.replace(db,twice);
        name=db+name+db;
        StringBuilder result = new StringBuilder(name).append(time);
        if(!active.isSelected()){
            result.append("inactive");
        }
        return result.toString();
    }

    /**
     * The method that assert ois not title field empty. If it is empty the method push the error message
     * "Fill please title field!" and return false. If all is normal - true
     * @return true or false
     */
    private boolean validate(){
        if(titleField.getText().isEmpty()){
            View.showErrorMessage(editionRootPanel,"Fill please title field!");
            return false;
        }
        return true;
    }

    /**
     * This method parse {@code task} string on corresponding information parts and push all of them to correct fields.
     * The string in format {@code "Title" at [yyyy-mm-dd hh:mm:ss.msmsms] (inactive)} or
     * {@code "Title" from [yyyy-mm-dd hh:mm:ss.msmsms] to [yyyy-mm-dd hh:mm:ss.msmsms]
     * every [0 day(s) 0 hour(s) 0 minute(s) 0 second(s)] (inactive)}
     * @param task - the task string
     */
    public void setTaskToFields(String task){
        boolean actTask=true, repeatTask=false;
        if(task.contains("inactive")){
            actTask=false;
        }
        if(task.contains("from")){
            repeatTask=true;
        }

        active.setSelected(actTask);
        repeat.setSelected(repeatTask);

        int start = 1;
        int finish = task.lastIndexOf((char)34);
        String name = getValueFromSepars(task,start,finish);
        titleField.setText(name);

        timerContent.setTimeToFields(task,repeatTask);
        editionRootPanel.revalidate();
    }

    /**
     * This method get string value from start to finish
     * @param source - source string
     * @param start - start position including
     * @param finish - the end position excluding
     * @return the string value
     */
    public static String getValueFromSepars(String source,int start, int finish){
        String result = source.substring(start,finish);
        return result;
    }
}
