package wjhj.orbital.sportsmatchfindingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;

import wjhj.orbital.sportsmatchfindingapp.repo.SportalDB;
import wjhj.orbital.sportsmatchfindingapp.repo.Game;
import wjhj.orbital.sportsmatchfindingapp.repo.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SportalDB sportalDB = new SportalDB();
        sportalDB.addUser("haw_jiaa",
                User.builder()
                        .setFirstName("hj")
                        .setLastName("l")
                        .setBirthday(new Date(1997, 2, 6))
                        .setEmail("abc@hi.com")
                        .setGender("male")
                        .setLocation("Singapore")
                        .setPreferences("Football")
                        .build());
        sportalDB.addGame(Game.Builder.startBuilder()
                .setEndTime(new Date(System.currentTimeMillis()))
                .setStartTime(new Date(System.currentTimeMillis()))
                .setMaxPlayers(5)
                .setMinPlayers(5)
                .setSkill("beginner")
                .setSport("frisbee")
                .addDetails("frisbeeeee")
                .addLocation("nus science")
                .addName("hahaha")
                .addUserName("haw_jia", "haw_jiaa", "wei_jie")
                .build());
    }
}