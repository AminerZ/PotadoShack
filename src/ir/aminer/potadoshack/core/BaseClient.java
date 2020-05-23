package ir.aminer.potadoshack.core;

import java.io.*;

public class BaseClient implements Serializable {
    protected String username;
    protected String password;
    protected String first_name;
    protected String last_name;

    public static BaseClient loadClient(File clientFile) {
        if (! (clientFile.exists() && clientFile.isFile()))
            throw new IllegalStateException("Specified path should exist and be a valid file.");

        ObjectInputStream inputStream;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(clientFile));
        } catch (IOException e) {
            System.err.println("Could not initialize InputStream.");
            return null;
        }

        Object o;
        try {
            o = inputStream.readObject();
        } catch (IOException e) {
            System.err.println("Could not read the user.");
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("User class was not found.");
            return null;
        }

        /* Close the stream */
        try {inputStream.close();} catch (IOException e) {System.err.println("Could not close the stream.");}

        return (BaseClient)o;
    }

    public boolean save(File clientFile){
        if (! (clientFile.exists() && clientFile.isFile()))
            throw new IllegalStateException("Specified path should exist and be a valid file.");

        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(clientFile));
        } catch (IOException e) {
            System.err.println("Could not initialize OutputStream.");
            return false;
        }

        try {
            outputStream.writeObject(this);
        } catch (IOException e) {
            System.err.println("Could not write the user.");
            return false;
        }

        /* Close the stream */
        try {outputStream.close();} catch (IOException e) {System.err.println("Could not close the stream.");}

        return true;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }
}