package root.withClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import root.grantingAccess.Admission;
import root.distribution.RServlet;

public class ReceiveSerialized extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		InputStream inStream = req.getInputStream();
		HackedObjectInputStream data = new HackedObjectInputStream(inStream);
		PrintWriter writer = resp.getWriter();
	
		try {
			Admission admission = (Admission) data.readObject();
			if (putData(admission))
				writer.print("granted");
			else
				writer.print("went_wrong");
			writer.close();
		} catch (ClassNotFoundException | IOException e) {
			RServlet.treat(e);
		}
	}

	private boolean putData(Admission admission) throws IOException {
		File accessFile = new File(RServlet.mainWay + "Clients/" + admission.getAdmitted() + "/accessed/"
				+ admission.getFileName() + "@granted.system");
		accessFile.createNewFile();

		OutputStream out = new FileOutputStream(accessFile);
		String info = "[from user]:" + admission.getFrom() + "[to user]:" + admission.getAdmitted() + "[file]:"
				+ admission.getFileName();
		out.write(info.getBytes("UTF-8"));
		out.close();
		return true;
	}
}
