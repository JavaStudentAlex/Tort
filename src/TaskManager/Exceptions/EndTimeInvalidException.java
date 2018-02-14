package TaskManager.Exceptions;

public class EndTimeInvalidException extends Exception {
    public EndTimeInvalidException(){
        super("Invalid end time input");
    }
}
