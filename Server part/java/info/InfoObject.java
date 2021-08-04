package root.info;

import java.io.Serializable;

public class InfoObject implements Serializable {
	
	private static final long serialVersionUID=23434349235L;
	
	private String name,type,size,creation,lastChanging;
	
	void setName(String name) {
		this.name=name;
	}
	
	void setType(String type) {
		this.type=type;
	}
	
	void setSize(String size) {
		this.size=size;
	}
	
	void setCreation(String creation) {
		this.creation=creation;
	}
	
	void setLastChanging(String lastChanging) {
		this.lastChanging=lastChanging;
	}
}
