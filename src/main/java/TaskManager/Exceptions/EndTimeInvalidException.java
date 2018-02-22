package TaskManager.Exceptions;

/**
 * The special exception with mining, that user have done invalid input into end time field.
 */
public class EndTimeInvalidException extends Exception {
    /**
     * The constructor that create a super class with special for this exception message about end time field.
     */
    public EndTimeInvalidException(){
        super("Invalid end time input");
    }
}
