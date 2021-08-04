package root.info;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import root.distribution.RServlet;

public class InfoRelated {
	public String outcome;
	String user;
	File workWith;
	HashMap<String, String> inputInfo;
	
	public InfoRelated() {
	}

	public InfoRelated(String user) {
		this.user = user;
	}

	public InfoRelated(File workWith, HashMap<String, String> inputInfo) {
		this.workWith = workWith;
		this.inputInfo = inputInfo;

	}

	public class GeneralizedInfo extends InfoRelated {
		private String folder;

		public GeneralizedInfo(String user, String folder) {
			super(user);
			this.folder = folder;
			doing();
		}

		private void doing() {
			File dir;
			String shorted = RServlet.mainWay + "Clients/" + user + "/files/";

			if (folder.equals("root"))
				dir = new File(shorted);
			else
				dir = new File(shorted + folder);

			int allCount = 0, filesCount, foldersCount = 0;
			if (dir.list().length > 0) {
				for (File file : dir.listFiles()) {
					allCount++;
					if (file.isDirectory())
						foldersCount++;
				}
			}
			filesCount = allCount - foldersCount;
			super.outcome = allCount + "--" + filesCount + "--" + foldersCount;
		}
	}
	
}

