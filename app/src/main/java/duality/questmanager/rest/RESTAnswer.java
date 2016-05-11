package duality.questmanager.rest;

/**
 * Created by olegermakov on 09.05.16.
 */
public class RESTAnswer {
    private int status;
    private String message;


    public RESTAnswer(int status)
    {
        this.status = status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
