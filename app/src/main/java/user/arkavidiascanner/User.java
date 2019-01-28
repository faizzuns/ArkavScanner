package user.arkavidiascanner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("success")
    @Expose
    private boolean success;

    public User(String name, String event, String token) {
        this.name = name;
        this.event = event;
        this.token = token;
    }

    public User() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
