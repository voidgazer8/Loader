package root.distribution;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Base64;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import root.grantingAccess.GrantedMode;
import root.info.FullInfo;
import root.info.InfoRelated;
import root.standart.ToDelete;
import root.standart.ToRename;

public class RServlet extends HttpServlet {

	public final static String mainWay;
	private HttpServletRequest req;

	static {
		mainWay = "C:/Program Files/Apache Software Foundation/Tomcat 10.0/webapps/Loader/";
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setContentType("text/html;charset=utf-8");
			this.req = req;
			PrintWriter out = resp.getWriter();
			String param = req.getParameter("type");

			Proceed proceed = new RServlet.Proceed(param);
			out.print(proceed.match());
			out.close();
		} catch (IOException e) {
			treat(e);
		}
	}

	class Proceed {
		private String param;
		private Actions obj;

		{
			obj = new Actions() {

				@Override
				public String addClient() {
					String login, password;
					login = getParameter("login");
					password = getParameter("password");

					Configure config = new Configure(login, password);
					return config.obj.addClient();
				}

				@Override
				public String enterClient() {
					String login, password;
					login = getParameter("login");
					password = getParameter("password");

					Configure config = new Configure(login, password);
					config.obj.enterClient();
					return config.obj.enterClient();
				}

				@Override
				public String changeLogin() {
					String newLogin, oldLogin;
					newLogin = getParameter("new_login");
					oldLogin = getParameter("old_login");

					Configure config = new Configure(newLogin, oldLogin, 1);
					return config.obj.changeLogin();
				}

				@Override
				public String changePassword() {
					String user, newPassword, oldPassword;
					user = getParameter("user");
					newPassword = getParameter("new_password");
					oldPassword = getParameter("old_password");

					Configure config = new Configure(user, newPassword, oldPassword);
					return config.obj.changePassword();
				}

				@Override
				public String deleteAccount() {
					String user = getParameter("user");
					Configure config = new Configure(user);
					return config.obj.deleteAccount();
				}

				@Override
				protected String move() {
					String user, file, targetFolder, currentFolder;
					user = getParameter("user");
					file = getParameter("filename");
					targetFolder = getParameter("target_folder");
					currentFolder = getParameter("current_folder");

					Configure config = new Configure(user, file, targetFolder, currentFolder);
					return config.obj.move();
				}
			};
		}

		private String forUpdateList() {
			String login, folder = null;
			login = getParameter("user");
			folder = getParameter("folder_to_get");

			Configure config = new Configure(login);
			return config.getContent(folder);
		}

		private String checkRegs() {
			File dir = new File(mainWay + "/Clients");
			if (dir.list().length == 0)
				return "no_users";
			else
				return "users_present";
		}

		private String forUpdateSize() {
			String login;
			login = getParameter("user");

			Configure config = new Configure(login);
			return config.getCurrentSize(-1);
		}

		private String delete() throws IOException {
			String user, file, typeOfElement, folder;
			user = getParameter("user");
			file = getParameter("to_delete");
			typeOfElement = getParameter("object");
			folder = getParameter("currentFolder");

			return new ToDelete(user, file, folder).correlate(typeOfElement);
		}

		private String rename() throws IOException {
			String user, oldName, newName, typeOfElement, folder;
			user = getParameter("user");
			oldName = getParameter("to_rename");
			newName = getParameter("set");
			typeOfElement = getParameter("object");
			folder = getParameter("currentFolder");

			return new ToRename(user, oldName, newName, folder).correlate(typeOfElement);

		}

		private String createFolder() throws IOException {
			String user, folder;
			user = getParameter("user");
			folder = getParameter("folder");

			Configure config = new Configure();
			return config.toCreateFolder(user, folder);
		}

		private String getType() {
			String user, element;
			user = getParameter("user");
			element = getParameter("nameOf");

			Configure config = new Configure();
			return config.toGetType(user, element);
		}

		private String getGeneralized() {
			String user, folder;
			user = getParameter("user");
			folder = getParameter("directory");

			return new InfoRelated().new GeneralizedInfo(user, folder).outcome;
		}

		private String getFull() throws IOException {
			String user, name;
			user = getParameter("user");
			name = getParameter("name");

			FullInfo info = new FullInfo(user, name);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(info.extract("useSerializing"));

			return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
		}

		private String getFolderList() {
			String user;
			user = getParameter("user");
			File dir = new File(RServlet.mainWay + "Clients/" + user + "/files/");
			String list = "";

			if (dir.list().length > 0) {
				for (File file : dir.listFiles()) {
					if (file.isDirectory())
						list = list + file.getName() + "--";
				}
			}

			return list;
		}

		private String checkUser() {
			String user = getParameter("user");
			File dir = new File(RServlet.mainWay + "Clients/" + user);
			if (dir.exists())
				return "user_exist";
			else
				return "didn't_find";
		}

		private String getParameter(String textValue) {
			return RServlet.this.req.getParameter(textValue);
		}

		private Proceed(String param) {
			this.param = param;
		}

		public String match() throws IOException {
			String rezult = null;

			switch (this.param) {
			case "check_regs":
				rezult = checkRegs();
				break;
			case "add_client":
				rezult = obj.addClient();
				break;
			case "make_enter":
				rezult = obj.enterClient();
				break;
			case "get_list":
				rezult = forUpdateList();
				break;
			case "get_size":
				rezult = forUpdateSize();
				break;
			case "change_login":
				rezult = obj.changeLogin();
				break;
			case "change_password":
				rezult = obj.changePassword();
				break;
			case "delete_account":
				rezult = obj.deleteAccount();
				break;
			case "delete":
				rezult = delete();
				break;
			case "rename":
				rezult = rename();
				break;
			case "create_folder":
				rezult = createFolder();
				break;
			case "kind_of":
				rezult = getType();
				break;
			case "get_generalized":
				rezult = getGeneralized();
				break;
			case "get_full":
				rezult = getFull();
				break;
			case "get_folder_list":
				rezult = getFolderList();
				break;
			case "move_to":
				rezult = obj.move();
				break;
			case "check_user":
				rezult = checkUser();
				break;
			case "is_granted":
				String user = getParameter("user");
				rezult = new GrantedMode(user).checkUpGranted();
				break;
			case "get_granted_files":
				String user1=getParameter("user");
				rezult=new GrantedMode(user1).new ConfigureGrantedFiles().getRezult();
				break;
			}
			return rezult;
		}

	}

	public static void treat(Exception ex) {
		try {
			File log = new File(mainWay + "/log/openWhenLifeGoesDown.txt");
			PrintWriter writer = new PrintWriter(log);
			ex.printStackTrace(writer);
			writer.close();
		} catch (FileNotFoundException e) {
			treat(e);
		}
	}
}
