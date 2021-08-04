package root.withClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

import root.grantingAccess.Admission;

class HackedObjectInputStream extends ObjectInputStream {

    public HackedObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();

        if (resultClassDescriptor.getName().equals("com.example.loader.client_options.grant_mode.providing.Admission"))
            resultClassDescriptor = ObjectStreamClass.lookup(Admission.class);

        return resultClassDescriptor;
    }
}