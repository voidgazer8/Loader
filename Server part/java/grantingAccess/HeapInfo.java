package root.grantingAccess;

import java.io.Serializable;
import java.util.ArrayList;

public class HeapInfo implements Serializable {

	private static final long serialVersionUID=23434349236L;
	
	private ArrayList<String> listFiles,listSenders;
	
	public HeapInfo(ArrayList<String> listFiles, ArrayList<String> listSenders) {
		this.listFiles=listFiles;
		this.listSenders=listSenders;
	}
	
}
