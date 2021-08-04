package root.distribution;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import root.info.FullInfo;


public class Configure {
	private String login, password,newPassword,oldPassword,
	               newLogin,oldLogin,filename,targetFolder,currentFolder;
	private Connection connect;
	private Statement state;
	
	public Actions obj;
	
	public Configure() {}
	
	public Configure(String login) {
		this.login = login;
	}
	
	public Configure(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public Configure(String newLogin, String oldLogin,int r) {
		this.oldLogin = oldLogin;
		this.newLogin = newLogin;
	}
	
	public Configure(String login, String newPassword, String oldPassword) {
		this.login = login;
		this.newPassword = newPassword;
		this.oldPassword=oldPassword;
	}
	
	public Configure(String login, String filename, String targetFolder,String currentFolder) {
		this.login = login;
		this.filename=filename;
		this.targetFolder=targetFolder;
		this.currentFolder=currentFolder;
	}


	{
		obj=new Actions() {

			@Override
			protected String addClient() {
				try {
					connectWith();
					state.executeUpdate("INSERT INTO users (login,password) VALUES ('" + login + "','" + password + "')");
					
					String shorted=RServlet.mainWay + "Clients/" + login;
					new File(shorted + "/files").mkdirs();
					new File(shorted + "/about").mkdir();
					new File(shorted + "/accessed").mkdir();
					
					return "well_done" + getContent(null) + "size->" + getCurrentSize(-1);
				} catch (SQLException e) {
					RServlet.treat(e);
					if (e.getMessage().contains("Duplicate entry"))
						return "existed";
					
					return "went_wrong";
					
				}
			}

			@Override
			protected String enterClient() {
				try {
					connectWith();
					ResultSet set;
					if (password.equals("<>"))
						set = state.executeQuery("SELECT 1 FROM users WHERE login='" + login + "' ");
					else
						set = state.executeQuery(
								"SELECT 1 FROM users WHERE login='" + login + "' " + "AND password='" + password + "'");
					String a = null;
					while (set.next())
						a = set.getString(1);

					if (a != null) {
						if (a.equals("1")) 
							return  "well_done" + getContent(null) + "size->" + getCurrentSize(-1);
					} else
						return "didn't_find";

				} catch (SQLException e) {
					RServlet.treat(e);
					return"went_wrong";
				}
				return "went_wrong";
			}

	
			@Override
			protected String changeLogin() {
				try {
					connectWith();
					state.executeUpdate("UPDATE users SET login='" + newLogin + "' WHERE login='" + oldLogin + "'");

					File dir = new File(RServlet.mainWay + "Clients/" + oldLogin);
					dir.renameTo(new File(RServlet.mainWay + "Clients/" + newLogin));
					return "changed";
				} catch (SQLException e) {
					RServlet.treat(e);
					if (e.getMessage().contains("Duplicate entry"))
						return "existed";
					else
						return "went_wrong";
				}
			}

			@Override
			protected String changePassword() {
				try {
					connectWith();
					ResultSet set = state.executeQuery("SELECT password FROM users WHERE login='" + login + "'");

					if (set.next())
						if (set.getString(1).equals(oldPassword)) {
							state.executeUpdate("UPDATE users SET password='" + newPassword + "' WHERE login='" + login + "'");
							return "changed";
						} else
							return "doesn't_match";

					return "unexpected";
				} catch (SQLException e) {
					RServlet.treat(e);
					return "went_wrong";
				}
			}

			@Override
			protected String deleteAccount() {
				try {
					connectWith();
					state.executeUpdate("DELETE FROM users WHERE login='" + login + "'");

					File dir = new File(RServlet.mainWay + "Clients/" + login);
					FileUtils.deleteDirectory(dir);
					return "deleted";
				} catch (SQLException | IOException e) {
					RServlet.treat(e);
					return "went_wrong";
				}
			}

			@Override
			protected String move() {
				File file;
				
				if(currentFolder.equals("root")) 
					file=new File(RServlet.mainWay+"Clients/"+login+"/files/"+filename);
				else
					file=new File(RServlet.mainWay+"Clients/"+login+"/files/"+currentFolder+"/"+filename);
				
				if(targetFolder.equals("root"))
					targetFolder="";
				
					
				if(file.renameTo(new File(RServlet.mainWay+"Clients/"+login+"/files/"+targetFolder+"/"+filename))) 
					return "moved";
				else 
					return "went_wrong";
			}

		};
	}
	

	public String getCurrentSize(long extra) {
		long bytes;
		if(extra==-1) {
			File dir = new File(RServlet.mainWay + "Clients/" + login + "/files");
		    bytes = FileUtils.sizeOfDirectory(dir);
		} else
			bytes=extra;
	
		DecimalFormat format=new DecimalFormat("#.###");

		if (bytes < 1024)
			return format.format(bytes)+ " B /15 GB";

		if (bytes >= 1024 && bytes < 1024 * 1024)
			return format.format((double)bytes / 1024) + " KB /15 GB";

		if (bytes > 1024 * 1024 && bytes < 1024 * 1024 * 1024)
			return format.format((double)bytes / 1024 / 1024) + " MB /15 GB";

		if (bytes > 1024 * 1024 * 1024 && bytes / 1024 / 1024 / 1024 < 15)
			return format.format((double)bytes / 1024 / 1024 / 1024) + " GB /15 GB";

		return "unexpected";
	}
	
	public String getContent(String f) {
		File dir;
		if (f == null)
			dir = new File(RServlet.mainWay + "Clients/" + login + "/files");
		else
			dir = new File(RServlet.mainWay + "Clients/" + login + "/files/" + f);

		String all = "--";
		if (dir.list().length > 0) {
			for (File file : dir.listFiles()) {
				String s = file.getName();
				if (file.isDirectory())
					s = s + "[folder]";
				all = all + s + "--";
			}
		}
		return all;
	}

	public String toCreateFolder(String user, String folder) throws IOException {
		File filefolder = new File(RServlet.mainWay + "Clients/" + user + "/files/" + folder);
		
		HashMap<String, String> map=new HashMap<>();
		map.put("user", user);
		map.put("foldername", folder);
		
		if (!filefolder.exists()) {
			if (filefolder.mkdir()) {
				new FullInfo(filefolder, map).initial("byCreation");
				return "created";
			}
			else
				return "went_wrong";
		} else
			return "existing_folder";
	}

	public String toGetType(String user, String element) {
		File file = new File(RServlet.mainWay + "Clients/" + user + "/files/" + element);
		if (file.isDirectory())
			return "directory";
		else
			return "file";
	}

	private void connectWith() throws SQLException {
		this.connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/clients_bd", "adminUser",
				"serverAMS788!");
		this.state = connect.createStatement();
	}

}













/*
 * try { File j=new File("D:/r.txt"); j.createNewFile(); OutputStream n=new FileOutputStream(j);
 * n.write((RServlet.way+"Clients/"+s1+"/files/"+s2).getBytes()); n.close(); }
 * catch (IOException e) {}
 */
