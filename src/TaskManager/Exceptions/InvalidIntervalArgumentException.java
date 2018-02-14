package TaskManager.Exceptions;

import java.io.IOException;

public class InvalidIntervalArgumentException extends IOException {

    public InvalidIntervalArgumentException(){
        super();
    }

    InvalidIntervalArgumentException(String msg){
        super(msg);
    }

}
