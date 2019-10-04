package ticketing;

/**
 * Class representing the ticket linked to an issue or malfunction.
 * 
 * The ticket is characterized by a severity and a state.
 */
public class Ticket {
    
	private int ticketId;
	private String username;
	private String componentPath;
	private String description;
	private Severity severity;
	private User user;
	private State state = State.Open;
	private String resoluiton;
	private User maintainer;
	
    public Ticket(int ticketId,String username, String componentPath, String description, Severity severity) {
		
    	this.ticketId = ticketId;
    	this.username = username;
    	this.componentPath = componentPath;
    	this.description = description;
    	this.severity = severity;
    	
	}

	/**
     * Enumeration of possible severity levels for the tickets.
     * 
     * Note: the natural order corresponds to the order of declaration
     */
    public enum Severity { Blocking, Critical, Major, Minor, Cosmetic };
    
    /**
     * Enumeration of the possible valid states for a ticket
     */
    public static enum State { Open, Assigned, Closed }
    
    public int getId(){
        return ticketId;
    }

    public String getDescription(){
        return description;
    }
    
    public Severity getSeverity() {
        return severity;
    }

    public String getAuthor(){
        return username;
    }
    
    public String getComponent(){
        return componentPath;
    }
    
    public State getState(){
        return state;
    }
    
    public String getSolutionDescription() throws TicketException {
    	if(state != State.Closed) {
    		throw new TicketException();
    	}
        return resoluiton;
    }

	public void setAssign(User u) {
		this.maintainer = u;
		this.state = State.Assigned;
		
	}
	public User getAssign() {
		return user;
	}

	public void setToClosedState(String resolution) {
		this.resoluiton = resolution;
		this.maintainer.incrementClosed();
		this.state = State.Closed;
		
	}
}
