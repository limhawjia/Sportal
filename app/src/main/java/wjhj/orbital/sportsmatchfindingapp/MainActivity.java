package wjhj.orbital.sportsmatchfindingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import wjhj.orbital.sportsmatchfindingapp.repo.SportalDB;
import wjhj.orbital.sportsmatchfindingapp.repo.Game;
import wjhj.orbital.sportsmatchfindingapp.repo.User;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currUser = mAuth.getCurrentUser();

        SportalDB sportalDB = new SportalDB();
        sportalDB.addUser("haw_jiaa",
                User.builder()
                        .setFirstName("hj")
                        .setLastName("l")
                        .setBirthday(LocalDate.of(1997, 2, 6))
                        .setEmail("abc@hi.com")
                        .setGender("male")
                        .setLocation("Singapore")
                        .setPreferences("Football")
                        .build());

        sportalDB.addGame(currUser.getUid(), Game.builder()
                .setEndTime(LocalDateTime.now())
                .setStartTime(LocalDateTime.now())
                .setMaxPlayers(5)
                .setMinPlayers(5)
                .setSkill(Game.Difficulty.BEGINNER)
                .setSport("frisbee")
                .addDetails("frisbeeeee")
                .addLocation("nus science")
                .addName("hahaha")
                .addUserName("haw_jia", "haw_jiaa", "wei_jie")
                .build());
    }
}