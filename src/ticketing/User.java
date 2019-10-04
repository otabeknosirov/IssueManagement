package ticketing;

import java.util.HashSet;
import java.util.Set;



   public class User {
	
	   private String name;
	   private Set<IssueManager.UserClass> classes = new HashSet<>();
	   private int incrementClosedNumber = 0;

	public User(String name, IssueManager.UserClass[] classes) {
	      this.name = name;
		  for(IssueManager.UserClass cls: classes) {
			  this.classes.add(cls);
		  }
	}
	public User(String name, Set<IssueManager.UserClass> classes) {
	      this.name = name;
		  for(IssueManager.UserClass cls: classes) {
			  this.classes.add(cls);
		  }
	}

	public Set<IssueManager.UserClass> getClasses() {
		return classes;
	}

	public String getName() {
		return name;
	}
	public void incrementClosed() {
		incrementClosedNumber++;
		
	}
	public int getIncrementNum() {
		return incrementClosedNumber;
	}
	
	
	

}
