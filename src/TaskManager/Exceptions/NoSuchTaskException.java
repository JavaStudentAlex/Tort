package TaskManager.Exceptions;

import java.util.NoSuchElementException;

public class NoSuchTaskException extends IllegalStateException/*NoSuchElementException*/ {

    public NoSuchTaskException(){super();}

    public NoSuchTaskException(String msg){
        super(msg);
    }
}
