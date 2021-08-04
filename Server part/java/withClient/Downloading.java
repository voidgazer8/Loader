package root.withClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import root.distribution.RServlet;

public class Downloading extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html;charset=utf-8");

		InputStream inStream = req.getInputStream();
		BufferedInputStream inBuf = new BufferedInputStream(inStream);
		String info[] = getFirstly(inBuf);

		OutputStream outStream = resp.getOutputStream();
		BufferedOutputStream outBuf = new BufferedOutputStream(outStream);
		File toSend;

		if (info[2].equals("root"))
			toSend = new File(RServlet.mainWay + "Clients/" + info[0] + "/files/" + info[1]);
		else
			toSend = new File(RServlet.mainWay + "Clients/" + info[0] + "/files/" + info[2] + "/" + info[1]);
		sending(toSend, outBuf);
	}

	private boolean sending(File toSend, BufferedOutputStream out) {
		try {
			InputStream inStream = new FileInputStream(toSend);
			BufferedInputStream inBuf = new BufferedInputStream(inStream);

			int n;
			byte[] bytes = new byte[4096];

			while ((n = inBuf.read(bytes)) != -1)
				out.write(bytes, 0, n);

			out.close();
			inBuf.close();
			return true;
		} catch (IOException e) {
			RServlet.treat(e);
			return false;
		}
	}

	private String[] getFirstly(BufferedInputStream in) {
		try {
			byte[] infoBytes = new byte[256];
			in.read(infoBytes);
			String info = new String(infoBytes, "utf-8");

			String[] rezults = new String[3];
			rezults[0] = info.substring(5, info.indexOf("}+needed{"));
			rezults[1] = info.substring(info.indexOf("}+needed{") + 9, info.indexOf("}+folder{"));
			rezults[2] = info.substring(info.indexOf("}+folder{") + 9, info.lastIndexOf("}"));

			return rezults;
		} catch (IOException e) {
			RServlet.treat(e);
			return null;
		}
	}
}
