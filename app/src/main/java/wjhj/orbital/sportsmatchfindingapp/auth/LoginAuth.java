package wjhj.orbital.sportsmatchfindingapp.auth;

import org.apache.commons.validator.routines.EmailValidator;

class LoginAuth {

    private String email;
    private String password;

    LoginAuth(String email, String password) {
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
