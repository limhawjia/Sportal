package wjhj.orbital.sportsmatchfindingapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Calendar;

import wjhj.orbital.sportsmatchfindingapp.database.Data;
import wjhj.orbital.sportsmatchfindingapp.database.Game;
import wjhj.orbital.sportsmatchfindingapp.database.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Data.addUser("haw_jiaa",
                User.Builder.startBuilder()
                        .setFirstName("hj")
                        .setLastName("l")
                        .setBirthday(new Date(1997, 2, 6))
                        .setEmail("abc@hi.com")
                        .setGender("male")
                        .setLocation("Singapore")
                        .setPreferences("Football")
                        .build());
        Data.addGame(Game.Builder.startBuilder()
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