package TaskManager.Exceptions;

public class SameTaskException extends Exception {
    public SameTaskException(){
        super("There is the same task");
    }
}
