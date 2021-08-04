package com.example.loader.data;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;

public class HackedObjectInputStream extends ObjectInputStream {

    private final String searchingClass;
    private final Class<?> target;

    public HackedObjectInputStream(InputStream in,String searchingClass,Class<?> target) throws IOException {
        super(in);
        this.searchingClass=searchingClass;
        this.target=target;
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();

        if (resultClassDescriptor.getName().equals(searchingClass))
            resultClassDescriptor = ObjectStreamClass.lookup(target);

        return resultClassDescriptor;
    }
}