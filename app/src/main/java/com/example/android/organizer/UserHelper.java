/// Java file which creates the constructor the user has to send to firebase
package com.example.android.organizer;

public class UserHelper {
    String Email,Password,Phone,Profession;

    public UserHelper() {
    }

    public UserHelper(String email, String password, String phone, String profession) {
        Email = email;
        Password = password;
        Phone = phone;
        Profession = profession;

    }

    public String getEmail() {
        return Email;
    }

    public void setUsername(String username) {
        Email = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getProfession() {
        return Profession;
    }

    public void setProfession(String profession) {
        Profession = profession;
    }

}