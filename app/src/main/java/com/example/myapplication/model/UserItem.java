package com.example.myapplication.model;

public class UserItem {
    private String _id;
    private String fullName;

    private String profilePic;

    public UserItem(String fullName, String profilePic, String _id) {
        this._id = _id;
        this.fullName = fullName;
        this.profilePic = profilePic;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}

