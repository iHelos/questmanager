package duality.questmanager.rest;

import java.util.ArrayList;

/**
 * Created by olegermakov on 09.05.16.
 */
public class RESTAnswer {
    private int status;
    private ArrayList<String> messages;


    public RESTAnswer(int status)
    {
        messages = new ArrayList<String>();
        this.status = status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public String getMessages(int index) {
        return messages.get(index);
    }
}
