package root.standart;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import root.distribution.Configure;
import root.distribution.RServlet;
import root.info.FullInfo;

public class ToDelete extends Configure {
	private File file;
	private String shorted;
	private String user,name,folder;
	

	public ToDelete(String user, String name, String folder) {
		this(user,name);
		this.folder=folder;
		 
		shorted=RServlet.mainWay + "Clients/" + user + "/files/";
		if (folder.equals("root"))
			file = new File(shorted + name);
		else
			file = new File(shorted + folder + "/" + name);
		
	}
	
	ToDelete(String user,String name) {
		this.user=user;
		this.name=name;
	
	}

	public String correlate(String type) throws IOException {
		ArrayList<Boolean> rezults=new ArrayList<>();
		switch (type) {
		case "file":
			boolean b;
			if (b=file.delete()) {
				rezults.add(b);
				if (!folder.equals("root"))
					rezults.add(updateFolderSize());
			}
			break;
		case "directory":
			doDelete();
			break;
		}
		
		rezults.add(deleteInfoFile());
		
		boolean total=true;
		for(boolean b: rezults) 
			total=total && b;
	
		if(total)
			return "deleted";
		else 
			return "went_wrong";
	}

	public boolean updateFolderSize() throws IOException {
	
		if(!folder.equals("root")) {
			
			
			
			File file = new File(RServlet.mainWay + "Clients/" + user + "/files/"+folder); 
		    FullInfo info=new FullInfo(user,folder);
		    info.extract("withoutSerializing");
		    
		    String current=info.outcome, toReplace=getCurrentSize(FileUtils.sizeOfDirectory(file));
		    toReplace=toReplace.substring(0,toReplace.indexOf("/")-1);
		    StringBuilder build=new StringBuilder(current);
		    build.replace(current.indexOf("[size]:")+7, current.indexOf("[creation]:"), toReplace);
		    return info.dropInformation(build.toString());
		}
		return true;
	}

	private boolean deleteInfoFile() {
		File info=new File(RServlet.mainWay + "Clients/"+user+"/about/"+name+"@info.system");
		return info.delete();
	}

	private boolean doDelete() {
		try {
			FileUtils.deleteDirectory(file);
			return true;
		} catch (IOException e) {
			RServlet.treat(e);
			return false;
		}
	}

}
