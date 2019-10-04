package ticketing;

public class TicketException extends Exception {
    private static final long serialVersionUID = 1L;

    public TicketException(){
        super("Generic ticketing error");
    }

    public TicketException(String msg){
        super( msg );
    }
}
