package root.standart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import root.info.FullInfo;
import root.distribution.Configure;
import root.distribution.RServlet;

public class ToRename extends Configure {
	private File workFile;
	private boolean state;
	private String user,oldName, newName, folder,shorted;

	public ToRename(String user, String oldName, String newName, String folder) {
		
		this.user = user;
		
		shorted=RServlet.mainWay + "Clients/" + user + "/files/";
		if (folder.equals("root")) {
			workFile = new File(shorted+ oldName);
			state = true;
		} else
			workFile = new File(shorted + folder + "/" + oldName);

		this.oldName=oldName;
		this.newName = newName;
		this.folder = folder;
	}

	public String correlate(String type) throws IOException {
		ArrayList<Boolean> rezults=new ArrayList<>();
		switch (type) {
		case "file":
			if (state)
				rezults.add(rootLocation());
			else
				rezults.add(innerLocation());
			break;
		case "directory":
			rezults.add(ifDirectory());
			break;
		}
		
		rezults.add(renameInfoFile());
		rezults.add(updateInfo());
		
		boolean total=true;
		for(boolean b: rezults) 
			total=total && b;
		
		try { File j=new File("D:/r.txt"); j.createNewFile(); OutputStream n=new FileOutputStream(j);
		 n.write((rezults.get(0)+" "+rezults.get(1)+" "+rezults.get(2)).getBytes()); n.close(); }
		 catch (IOException e) {}
		
		if(total)
			return"renamed";
		else 
			return "went_wrong";
		
	}

	private boolean renameInfoFile() {
		String shorted=RServlet.mainWay + "Clients/"+user+"/about/";
		File file=new File(shorted+oldName+"@info.system");
		return file.renameTo(new File(shorted+newName+"@info.system"));
	}

	private boolean updateInfo() throws IOException {
		FullInfo info=new FullInfo(user,newName);
		info.extract("withoutSerializing");
		String current=info.outcome;
		
		StringBuilder build=new StringBuilder(current);
		build.replace(current.indexOf("[last_changing]:")+16, current.length(),info.getNow());
		build.replace(7, current.indexOf("[type]:"), newName);
		return info.dropInformation(build.toString());
	}

	private boolean ifDirectory() {
		if (workFile.renameTo(new File(shorted + newName)))
			return true;
		else
			return false;
	}

	private boolean innerLocation() {
		if (workFile.renameTo(new File(shorted + folder + "/" + newName)))
			return true;
		else
			return false;
	}

	private boolean rootLocation() {
		if (workFile.renameTo(new File(shorted + newName)))
			return true;
		else
			return false;
	}
}
