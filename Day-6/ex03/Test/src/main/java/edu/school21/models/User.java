package edu.school21.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private long id;
    private String login;
    private String password;
    private boolean auth_status;

    public User(long id, String login, String password, boolean auth_status){
        this.id = id;
        this.login = login;
        this.password = password;
        this.auth_status = auth_status;
    }

    public void setAuthStatus(boolean auth_status){
        this.auth_status = auth_status;
    }
    public boolean getAuthStatus(){
        return auth_status;
    }
}
