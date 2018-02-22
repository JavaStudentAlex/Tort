package TaskManager.Exceptions;

/**
 * The special exception class that shows, that the date parse with errors and user have done something wrong in his
 * activity.
 */
public class ParseDateException extends Exception{
    /**
     * The constructor of exception that create a new instance by delegating super constructor.
     */
    public ParseDateException(){
        super();
    }
}
