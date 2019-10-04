package ticketing;

import java.util.HashSet;
import java.util.Set;

public class Component {

	private String name;
	private Component parent;
	private Set<Component> children = new HashSet<>();
	
	public Component(String name) {
		this.name = name;
		this.parent = null;
	}
		
	public Component(String name, Component parent) {
		this.name = name;
		this.parent = parent;
		parent.children.add(this);
	}

	public Set<Component> subComponents() {
		return children;
	}


	public String getName() {
		return name;
	}


	public Component getParent() {
		return parent;
	}
	
}
