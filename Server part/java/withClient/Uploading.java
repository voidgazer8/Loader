package root.withClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.HashMap;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import root.distribution.RServlet;
import root.info.FullInfo;
import root.standart.ToDelete;

public class Uploading extends HttpServlet {

	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();

		InputStream inStream = req.getInputStream();
		BufferedInputStream inBuf = new BufferedInputStream(inStream);

		String mask = inBuf.toString();
		File file = new File(RServlet.mainWay + "temp/" + mask.substring(mask.indexOf("@"), mask.length()) + ".temp");
		file.createNewFile();

		Object[] copy = modding(file, inBuf);
		if ((boolean) copy[0]) {
			out.print("success");
			new FullInfo((File) copy[1], (HashMap<String, String>) copy[2]).initial("byUpload");
			
			ToDelete function=new ToDelete(((HashMap<String, String>) copy[2]).get("user"),null,((HashMap<String, String>) copy[2]).get("folder"));
			function.updateFolderSize();
		} else
			out.print("went_wrong");
		out.close();
	}

	private Object[] modding(File file, BufferedInputStream inBuf) throws IOException {
		Object[] rezults = new Object[3];
		try {
			OutputStream outStream = new FileOutputStream(file);
			BufferedOutputStream outBuf = new BufferedOutputStream(outStream);

			int n;
			byte[] bytes = new byte[4096];

			while ((n = inBuf.read(bytes)) != -1)
				outBuf.write(bytes, 0, n);

			outBuf.close();
			inBuf.close();

			byte[] buffer = new byte[256];

			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			raf.seek(raf.length() - 256);
			raf.read(buffer);
			raf.close();

			String info = new String(buffer, "UTF-8");
			info = info.substring(info.indexOf("[~name~{"), info.length());
			String filename = info.substring(8, info.indexOf("+~user~{"));

			String user = info.substring(info.indexOf("+~user~{") + 8, info.indexOf("+~folder~{"));
			String folder = info.substring(info.indexOf("+~folder~{") + 10, info.length() - 1);

			File newFile;
			String part = RServlet.mainWay + "Clients/" + user + "/files/";

			if (folder.equals("root")) {
				newFile = new File(part + filename);
				file.renameTo(newFile);
			} else {
				newFile = new File(part + folder + "/" + filename);
				file.renameTo(newFile);
			}

			rezults[0] = true;
			rezults[1] = newFile;
			rezults[2] = getInfoMap(user, filename,folder);
		} catch (IOException e) {
			RServlet.treat(e);
			rezults[0] = false;
			return rezults;
		}
		return rezults;
	}

	private HashMap<String, String> getInfoMap(String user, String filename,String folder) {
		HashMap<String, String> map = new HashMap<>();
		map.put("user", user);
		map.put("filename", filename);
		map.put("folder", folder);
		return map;
	}
}
