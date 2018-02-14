package TaskManager.Exceptions;

import java.io.IOException;

public class InvalidTimeArgumentException extends IOException{

    public InvalidTimeArgumentException(){
        super();
    }
    public InvalidTimeArgumentException(String msg){
        super(msg);
    }

}
