package wjhj.orbital.sportsmatchfindingapp.auth;

import android.net.Uri;

public class SignUpAuth extends LoginAuth {

    private String displayName;
    private Uri displayPic;

    SignUpAuth(String email, String password, String displayName, Uri displayPic) {
        super(email, password);
        this.displayName = displayName;
        this.displayPic = displayPic;
    }

    String getDisplayName() {
        return displayName;
    }

    Uri getDisplayPic() {
        return displayPic;
    }

    public boolean isDisplayNameValid() {
        return displayName != null;
    }
}
