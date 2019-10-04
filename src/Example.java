
import java.util.HashSet;

import ticketing.IssueManager;
import ticketing.Ticket;
import ticketing.TicketException;
import static ticketing.IssueManager.*;

public class Example {

    public static void main(String[] args) throws TicketException {
        
        IssueManager ts = new IssueManager();
        HashSet<UserClass> both = new HashSet<>();
        both.add(UserClass.Reporter);
        both.add(UserClass.Maintainer);
        
        ts.createUser("alpha", UserClass.Reporter);
        ts.createUser("beta", UserClass.Reporter);
        ts.createUser("gamma", both);
        ts.createUser("delta", both);
        ts.createUser("epsilon", UserClass.Maintainer);
        
        System.out.println(ts.getUserClasses("gamma"));
        
        ts.defineComponent("System");
        ts.defineSubComponent("SubA", "/System");
        ts.defineSubComponent("SubB", "/System");
        ts.defineSubComponent("SubC", "/System");
        ts.defineSubComponent("SubB.1", "/System/SubB");
        ts.defineSubComponent("SubB.2", "/System/SubB");
               
        System.out.println("System has " + ts.getSubComponents("/System") + " children");
        System.out.println("SubB.2 has parent " + ts.getParentComponent("/System/SubB/SubB.2"));
        
      
        ts.openTicket("alpha", "/System/SubA", "Initial menu does not show 'open' item", Ticket.Severity.Major);
        ts.openTicket("beta", "/System/SubA", "Cannot save form XYZ", Ticket.Severity.Major);
        ts.openTicket("alpha", "/System/SubB", "The colors in the diagram are hard to tell apart", Ticket.Severity.Minor);
       
        int id = ts.openTicket("alpha", "/System", "The system is not responding today", Ticket.Severity.Blocking);
        
        
        Ticket t = ts.getTicket(id);
        
        System.out.println("User " + t.getAuthor() + " created ticket " + t.getId() + " on component " + t.getComponent());
       
        ts.assingTicket(id, "delta");
        ts.closeTicket(id, "The user had the network cable unplugged...");
        
        System.out.println("The ticket status is " + t.getState() + " solution: " + t.getDescription());
        
        System.out.println("Count open tickets:\n"  +ts.countBySeverityOfState(Ticket.State.Open));
        System.out.println("Count all tickets:\n"  +ts.countBySeverityOfState(null));
        System.out.println("Most active maintainers:\n"  +ts.topMaintainers());
    
    }

}
