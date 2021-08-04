package root.grantingAccess;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

import root.distribution.RServlet;

public class GrantedMode {

	private String user;

	public GrantedMode(String user) {
		this.user = user;
	}

	public String checkUpGranted() {
		String road = RServlet.mainWay + "Clients/" + user + "/accessed/";
		if (new File(road).listFiles().length > 0)
			return "is";
		else
			return "isn't";
	}

	public class ConfigureGrantedFiles {

		private String rezult;

		public ConfigureGrantedFiles() throws IOException {
			getGrantedFiles();
		}

		private void getGrantedFiles() throws IOException {
			String road = RServlet.mainWay + "Clients/" + user + "/accessed/";
			File dir = new File(road);

			ArrayList<String> listFiles = new ArrayList<>();
			ArrayList<String> listSenders = new ArrayList<>();
			for (File f : dir.listFiles()) {
				listFiles.add(extractName(f));
				listSenders.add(extractSender(f));
			}
			
			rezult = serialize(new HeapInfo(listFiles,listSenders));
		}

		private String serialize(HeapInfo heapInfo) throws IOException {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(heapInfo);
			return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
		}
		
		private String extractSender(File f) throws IOException {
			String str=reading(f);
			return str.substring(str.indexOf("[from user]:")+12,str.lastIndexOf("[to user]:"));
		}

		private String extractName(File f) throws IOException {
			String str=reading(f);
			return str.substring(str.indexOf("[file]:") + 7, str.length());
		}

		private String reading(File f) throws IOException {
			InputStream in = new FileInputStream(f);
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			in.close();
			return new String(bytes, "UTF-8");
		}

		public String getRezult() {
			return this.rezult;
		}

	}
}
