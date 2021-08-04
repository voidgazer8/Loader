package root.grantingAccess;

import java.io.Serializable;

public class Admission implements Serializable {
	
	private static final long serialVersionUID=23434349234L;

    private String filename,admitted,fromUser;

    public String getFileName() {
        return this.filename;
    }

    public String getAdmitted() {
        return this.admitted;
    }

    public String getFrom() {
        return this.fromUser;
    }
}
