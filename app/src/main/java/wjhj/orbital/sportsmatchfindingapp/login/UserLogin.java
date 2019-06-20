package wjhj.orbital.sportsmatchfindingapp.login;

import org.apache.commons.validator.routines.EmailValidator;

class UserLogin {

    private String email;
    private String password;

    UserLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    String getEmail() {
        return email;
    }

    String getPassword() {
        return password;
    }

    boolean isEmailValid() {
        return email != null && EmailValidator.getInstance().isValid(email);
    }

    boolean isPasswordValid() {
        return password != null && password.length() >= 8;
    }

}
