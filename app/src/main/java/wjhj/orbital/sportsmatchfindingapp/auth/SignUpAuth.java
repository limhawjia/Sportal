package wjhj.orbital.sportsmatchfindingapp.auth;

public class SignUpAuth extends LoginAuth {

    private String displayName;

    SignUpAuth(String email, String password, String displayName) {
        super(email, password);
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }

    public boolean isDisplayNameValid() {
        return displayName != null && !displayName.equals("");
    }
}
