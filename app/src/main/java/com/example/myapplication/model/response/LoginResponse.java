package com.example.myapplication.model.response;

public class LoginResponse {
    private String message;
    private Data data;
    private boolean onSuccess;

    // Getters and Setters

    public static class Data {
        private String _id;
        private String fullName;
        private String profilePic;
        private String token;
        private String wallet;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
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

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean isOnSuccess() {
        return onSuccess;
    }

    public void setOnSuccess(boolean onSuccess) {
        this.onSuccess = onSuccess;
    }
}
