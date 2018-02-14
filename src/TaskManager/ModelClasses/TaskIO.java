package TaskManager.ModelClasses;

import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class TaskIO {

    private static Logger logger = Logger.getLogger(TaskIO.class);
    public static void writeTask(DataOutputStream out, Task task){
        try {
            out.writeInt(task.getTitle().length());
            out.write(task.getTitle().getBytes());
            out.writeBoolean(task.isActive());
            out.writeInt(task.getRepeatInterval());
            if(task.isRepeated()){
                out.writeLong(task.getStartTime().getTime());
                out.writeLong(task.getEndTime().getTime());
            }
            else{
                out.writeLong(task.getTime().getTime());
            }
        } catch (IOException e) {
            logger.warn("Can not be TaskIO");
        }
    }

    public static Task readTask(DataInputStream in){
        Task result = null;
        try {
            int size = in.readInt();
            byte[] array = new byte[size];
            in.read(array);
            String title = new String(array);
            boolean isActive = in.readBoolean();
            int interval = in.readInt();
            if(interval>0){
                long startTime = in.readLong();
                long endTime = in.readLong();
                result = new Task(title,new Date(startTime),new Date(endTime),interval);
            }
            else{
                long time = in.readLong();
                result = new Task(title,new Date(time));
            }
            result.setActive(isActive);
        } catch (IOException e) {
            logger.warn("Can not be TaskIO");
        }
        return result;
    }
}
