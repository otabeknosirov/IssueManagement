package ticketing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import ticketing.Ticket.State;

public class IssueManager {
	
	private Map<String,Component> components = new HashMap<>();
	private Map<String, User> users = new HashMap<>();
	//private int ticketId;
	private List<Ticket> tickets = new ArrayList<>();
	
	
    public static enum UserClass {
        Reporter, 
        Maintainer 
      }
    
    /**
     * Creates a new user
     * 
     * @param username name of the user
     * @param classes user classes
     * @throws TicketException if the username has already been created or if no user class has been specified
     */
    public void createUser(String username, UserClass... classes) throws TicketException {
      
    	if(users.containsKey(username)) {
    		throw new TicketException("Such a User Exists!!!");
    	}
    	else {
    	User u = new User(username, classes);
    	users.put(username, u);
    	} 
   }

    /**
     * Creates a new user
     * 
     * @param username name of the user
     * @param classes user classes
     * @throws TicketException if the username has already been created or if no user class has been specified
     */
    public void createUser(String username, Set<UserClass> classes) throws TicketException {
       
    	if(users.containsKey(username)) {
    		throw new TicketException("Such a User Exists!!!");
    	}
    	else {
    	User u = new User(username, classes);
    	users.put(username,u);
    	}
    }
   
    /**
     * Retrieves the user classes for a given user
     * 
     * @param username name of the user
     * @return the set of user classes the user belongs to
     */
    public Set<UserClass> getUserClasses(String username){
    	User u = users.get(username);
        return u.getClasses();
    }
    
    /**
     * Creates a new component
     * 
     * @param name unique name of the new component
     * @throws TicketException if a component with the same name already exists
     */
    public void defineComponent(String name) throws TicketException {
     
    	if(components.containsKey(name)) {
    		throw new TicketException();
    	}
    	else {
    	Component c = new Component(name);
    	
    	String path = "/" + name;
    	components.put(path, c);
    	}
        
    }
    
    /**
     * C reates a new sub-component as a child of an existing parent component
     * 
     * @param name unique name of the new component
     * @param parentPath path of the parent component
     * @throws TicketException if the the parent component does not exist or 
     *                          if a sub-component of the same parent exists with the same name
     */
    public void defineSubComponent(String name, String parentPath) throws TicketException {
        if(components.containsKey(name)) {
        	throw new TicketException();
        }
        else {
    	Component parent = components.get(parentPath);
        
    	Component c = new Component(name,parent);
    	String path = parentPath + "/" + name;
    	components.put(path, c);
        } 
    }
    
    /**
     * Retrieves the sub-components of an existing component
     * 
     * @param path the path of the parent
     * @return set of children sub-components
     */
    public Set<String> getSubComponents(String path){
    	Component c = components.get(path);
    
     	HashSet<String> result = new HashSet<>();
    	
    	for(Component com: c.subComponents()) {
    		result.add(com.getName());
    	}
          return result;    	
    	
    	//alternative way with stream()
//        return c.subComponents().stream()
//        		                .map(com->com.getName())
//        		                .collect(Collectors.toSet());
//        
    	//OR
    	
//    	return components.values()
//    			         .stream()    			        
//    			         .filter(com->com.getParent()== c)
//    			         .map(Component::getName)
//    			         .collect(Collectors.toSet());
//    	
    }

    /**
     * Retrieves the parent component
     * 
     * @param path the path of the parent
     * @return name of the parent
     */
    public String getParentComponent(String path){
        return components.get(path).getParent().getName();
    }

    /**
     * Opens a new ticket to report an issue/malfunction
     * 
     * @param username name of the reporting user
     * @param componentPath path of the component or sub-component
     * @param description description of the malfunction
     * @param severity severity level
     * 
     * @return unique id of the new ticket
     * 
     * @throws TicketException if the user name is not valid, the path does not correspond to a defined component, 
     *                          or the user does not belong to the Reporter {@link IssueManager.UserClass}.
     */
    public int openTicket(String username, String componentPath, String description, Ticket.Severity severity) throws TicketException {
   
    	User user = users.get(username);
    	if(user == null) {
    		throw new TicketException("No Such a User");
    	}
    	Component comp = components.get(componentPath);
    	if(comp == null) {
    		throw new TicketException("No Such Sub-Component");
    	}
   // 	Ticket t = new Ticket(++ticketId,user.getName(),comp.getName(), description, severity);
   // alternative
    	Ticket t = new Ticket(tickets.size() + 1,user.getName(),comp.getName(), description, severity);
    	tickets.add(t);
    	
    	return t.getId();
    }
    
    /**
     * Returns a ticket object given its id
     * 
     * @param ticketId id of the tickets
     * @return the corresponding ticket object
     */
    public Ticket getTicket(int ticketId){
    	
    	Ticket t = tickets.get(ticketId - 1);
        
    	return t;      

         //alternative s-n
//    for(Ticket t: tickets) {
//    	if(t.getId() == ticketId) {
//    		return t;
//    	}
//    }
//    	
//        return null;
    }
    
    /**
     * Returns all the existing tickets sorted by severity
     * 
     * @return list of ticket objects
     */
    public List<Ticket> getAllTickets(){
       
    	
    	
        tickets.sort(new Comparator<Ticket>() {

			@Override
			public int compare(Ticket t1, Ticket t2) {
				
				return t1.getSeverity().compareTo(t2.getSeverity());
			}
		});
        return tickets;
    	
    }
    
    /**
     * Assign a maintainer to an open ticket
     * 
     * @param ticketId  id of the ticket
     * @param username  name of the maintainer
     * @throws TicketException if the ticket is in state <i>Closed</i>, the ticket id or the username
     *                          are not valid, or the user does not belong to the <i>Maintainer</i> user class
     */
   
    public void assingTicket(int ticketId, String username) throws TicketException {
        User u = users.get(username);
        if(u == null || !u.getClasses().contains(UserClass.Maintainer)) throw new TicketException();
        Ticket t = tickets.get(ticketId-1);
        if(t == null || t.getState() == Ticket.State.Closed) throw new TicketException();
        
        else {
          t.setAssign(u);	
        }
        
    }
    /**
     * Closes a ticket
     * 
     * @param ticketId id of the ticket
     * @param description description of how the issue was handled and solved
     * @throws TicketException if the ticket is not in state <i>Assigned</i>
     */
    public void closeTicket(int ticketId, String description) throws TicketException {
        Ticket t = tickets.get(ticketId - 1);
        if(t == null || t.getState() != State.Assigned) throw new TicketException();
        t.setToClosedState(description);
    }

    /**
     * returns a sorted map (keys sorted in natural order) with the number of  
     * tickets per Severity, considering only the tickets with the specific state.
     *  
     * @param state state of the tickets to be counted, all tickets are counted if <i>null</i>
     * @return a map with the severity and the corresponding count 
     */
    public SortedMap<Ticket.Severity,Long> countBySeverityOfState(Ticket.State state){
        
    	return tickets.stream()
    		   .filter(s->s.getState() == state || state == null)
    	       .collect(Collectors.groupingBy(Ticket::getSeverity,TreeMap::new,Collectors.counting()));
    }

    /**
     * Find the top maintainers in terms of closed tickets.
     * 
     * The elements are strings formatted as <code>"username:###"</code> where <code>username</code> 
     * is the user name and <code>###</code> is the number of closed tickets. 
     * The list is sorter by descending number of closed tickets and then by username.

     * @return A list of strings with the top maintainers.
     */
    public List<String> topMaintainers(){
        return users.values().stream()
        	  .sorted(Comparator.comparing(User::getIncrementNum)
        	  .reversed()
        	  .thenComparing(User :: getName))
        	  .map(u->u.getName() + ": " + u.getIncrementNum())
        	  .collect(Collectors.toList());
        	  
        	  
    }

}
