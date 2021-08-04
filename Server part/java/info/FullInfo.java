package root.info;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import root.distribution.Configure;
import root.distribution.RServlet;

public class FullInfo extends InfoRelated {
	private String wayToInfoFile, name;

	public FullInfo(File workWith, HashMap<String, String> inputInfo) {
		super(workWith, inputInfo);
	}

	public FullInfo(String user, String name) {
		super(user);
		this.name = name;
	}

	public void initial(String param) throws IOException {
		Necessary fileCase = inFileCase(), folderCase = inFolderCase();
		String shorted = RServlet.mainWay + "Clients/" + inputInfo.get("user") + "/about/";

		if (param.equals("byUpload")) {
			wayToInfoFile = shorted + inputInfo.get("filename") + "@info.system";
			fileCase.start();
			folderCase=null;
		} else {
			wayToInfoFile = shorted + inputInfo.get("foldername") + "@info.system";
			folderCase.start();
			fileCase = null;
		}
		put(fileCase, folderCase);
	}

	public Necessary inFolderCase() {
		return new Necessary() {

			@Override
			public void start() {
				super.creation = getNow();
				super.name = workWith.getName();
				getType();
				getSize();
			}

			@Override
			protected void getType() {
				super.type = "œ‡ÔÍ‡";
			}

			@Override
			protected void getSize() {
				super.size = new Configure().getCurrentSize(FileUtils.sizeOfDirectory(workWith));
			}
		};
	}

	private Necessary inFileCase() {
		return new Necessary() {

			@Override
			public void start() {
				super.creation = getNow();
				super.name = workWith.getName();
				getType();
				getSize();
			}

			@Override
			protected void getType() {
				super.type = "‘‡ÈÎ";
			}

			@Override
			protected void getSize() {
				super.size = new Configure().getCurrentSize(workWith.length());
			}

		};
	}

	public String getNow() {
		return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Calendar.getInstance().getTime());
	}

	public void put(Necessary obj1, Necessary obj2) throws IOException {
		Necessary copy;

		if (obj2 == null)
			copy = obj1;
		else
			copy = obj2;

		String size = copy.size.substring(0, copy.size.indexOf("/") - 1);
		String toWrite = "[name]:" + copy.name + "[type]:" + copy.type + "[size]:" + size + "[creation]:"
				+ copy.creation + "[last_changing]:" + copy.creation;

		dropInformation(toWrite);
	}

	public InfoObject extract(String arg) throws IOException {
		File infoFile = new File(RServlet.mainWay + "Clients/" + user + "/about/" + name + "@info.system");
		InputStream str = new FileInputStream(infoFile);
		byte[] bytes = new byte[str.available()];
		str.read(bytes);
		str.close();

		String rezult=new String(bytes, "UTF-8");
		
		if(arg.equals("withoutSerializing"))
			super.outcome = rezult;
		else 
			return(parse(rezult));
		return null;
	}

	private InfoObject parse(String rezult) {
		InfoObject object=new InfoObject();
        object.setName(rezult.substring(rezult.indexOf(":") + 1, rezult.indexOf("[type]:")));
        object.setType(rezult.substring(rezult.indexOf("[type]:") + 7, rezult.indexOf("[size]:")));
        object.setSize(rezult.substring(rezult.indexOf("[size]:") + 7, rezult.indexOf("[creation]:")));
        object.setCreation(rezult.substring(rezult.indexOf("[creation]:") + 11, rezult.indexOf("[last_changing]:")));
        object.setLastChanging(rezult.substring(rezult.indexOf("[last_changing]:") + 16));
		return object;
	}

	public boolean dropInformation(String content) throws IOException {
		File infoFile;
		if (user == null) {
			infoFile = new File(wayToInfoFile);
			infoFile.createNewFile();
		} else
			infoFile = new File(RServlet.mainWay + "Clients/" + user + "/about/" + name + "@info.system");

		OutputStream str = new FileOutputStream(infoFile);
		str.write(content.getBytes("UTF-8"));
		str.close();

		return true;
	}

	private abstract class Necessary {

		public String creation, type, size, name;

		public abstract void start();

		protected abstract void getType();

		protected abstract void getSize();
	}
}
