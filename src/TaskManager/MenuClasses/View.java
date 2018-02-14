package TaskManager.MenuClasses;

import TaskManager.Cheker;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;

public class View implements IView{

    private TasksContent tasksContent;
    private CalendarContent calendarContent;

    private CardLayout panels;
    private JPanel rootPanel;
    private JFrame window;
    public static final String menuTasks = "Tasks Controll",menuCalendar = "Calendar";
    private String currentMenu;

    public View(){
        window = getWindow();
    }

    public JFrame getFrame(){ // creating frame method
        JFrame window = new JFrame(){};
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Demo API");
        Toolkit thisToolkit = Toolkit.getDefaultToolkit();
        Dimension size = thisToolkit.getScreenSize();
        window.setBounds((int)(size.width*0.3),(int)(size.height*0.2),(int)(size.width*0.4),(int)(size.height*0.6));
        window.setVisible(true);
        return window;
    }

    private JFrame getWindow() {
        JFrame result = getFrame();//create frame

        rootPanel = new JPanel();// navigation
        panels = new CardLayout();
        result.setContentPane(rootPanel);
        rootPanel.setLayout(panels);
        tasksContent = new TasksContent();
        calendarContent = new CalendarContent();
        rootPanel.add(tasksContent.tasksMainRootPanel,menuTasks);
        rootPanel.add(calendarContent.calendarRootPanel,menuCalendar);
        currentMenu=menuTasks;
        panels.show(rootPanel,currentMenu);

        JMenuBar bar = new JMenuBar();
        JMenu tasksMenu = new JMenu("Tasks Controll");
        JMenuItem tasksItem = getMenuItem(menuTasks);
        tasksMenu.add(tasksItem);
        JMenu calendarMenu = new JMenu("Calendar");
        JMenuItem calendarItem = getMenuItem(menuCalendar);
        calendarMenu.add(calendarItem);
        bar.add(tasksMenu);
        bar.add(calendarMenu);
        result.setJMenuBar(bar);
        result.revalidate();
        return result;
    }

    private JMenuItem getMenuItem(String name){
        JMenuItem result = new JMenuItem(name);
        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(name!=currentMenu){
                    currentMenu=name;
                    panels.show(rootPanel,currentMenu);
                    rootPanel.revalidate();
                }
                if(name==menuTasks){
                   tasksContent.clear();
                }
                if(name==menuCalendar){
                    calendarContent.clear();
                }
                rootPanel.revalidate();
            }
        });
        return result;
    }

    public static void showErrorMessage(JPanel root, String message){
        JOptionPane.showMessageDialog(root,message,"ERROR",JOptionPane.ERROR_MESSAGE);
    }

    public void renewTasks(String[] tasks){
        tasksContent.renewTasks(tasks);
    }

    public void setSelectionListener(ListSelectionListener listener){
        tasksContent.setSelectionListener(listener);
    }

    public void setTaskToFields(String task){
        tasksContent.setTaskToFields(task);
    }

    public void setWindowCLosing(WindowAdapter adapter){
        window.addWindowListener(adapter);
    }

    public String getNewTask(){
        return tasksContent.getNewTask();
    }

    public void clearCalendar(){
        calendarContent.clear();
    }

    public String getStrartTimeCalendar(){
        return calendarContent.getStartTime();
    }

    public String getEndTimeCalendar(){
        return calendarContent.getEndTime();
    }

    public void setActionLookingCalendarListener(ActionListener listener){
        calendarContent.setFindEvent(listener);
    }

    public void setElemsToDatesList(ArrayList<String> dates){
        calendarContent.setElemsToDatesList(dates);
    }

    public void setChosenDatesListener(ListSelectionListener listener){
        calendarContent.setChosenDatesListener(listener);
    }

    public void setSchedule(ArrayList<String> tasks){
        calendarContent.setSchedule(tasks);
    }

    public void showErrorMessage(String message){
        JOptionPane.showMessageDialog(rootPanel,message,"ERROR",JOptionPane.ERROR_MESSAGE);
    }

    public void clearAll(){
        if(currentMenu==menuTasks){
            tasksContent.clear();
        }
        else{
            calendarContent.clear();
        }
    }

    public String getSelectedTask(){
        return tasksContent.getSelectedTask();
    }

    public String getSelectedDate(){
        return calendarContent.getSelectedDate();
    }

    public void setAddListener(ActionListener listener){
        tasksContent.setAddListener(listener);
    }

    public void setChangeListener(ActionListener listener){
        tasksContent.setChangeListener(listener);
    }

    public void setDeleteListener(ActionListener listener){
        tasksContent.setDeleteListener(listener);
    }

    public void connectCheker(Cheker cheker){
        cheker.setPanel(rootPanel);
    }
}
