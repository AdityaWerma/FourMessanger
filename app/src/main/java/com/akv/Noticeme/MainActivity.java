package com.akv.Noticeme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements ValueEventListener{


    private long pressedTime;

    TextView textView1, textView2,textView3,textView4;
    EditText editText1, editText2;
    Button button1, button2;

    CardView cardView1,cardView2,cardView3;



    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference= firebaseDatabase.getReference();

    private DatabaseReference firstDatabase = databaseReference.child("NEWS1");
    private DatabaseReference secondDatabase = databaseReference.child("pass");



    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            cardView1.setVisibility(View.VISIBLE);
            cardView2.setVisibility(View.INVISIBLE);
            cardView3.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.VISIBLE);
            editText2.setText("");
        }
        pressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel("01", "name", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }



        textView1 = (TextView) findViewById(R.id.first);
        textView2 =(TextView) findViewById(R.id.edit);
        textView3 =(TextView) findViewById(R.id.three);
        textView4=(TextView) findViewById(R.id.textViewNew);
        editText1  = (EditText)findViewById(R.id.second);



        editText2  = (EditText)findViewById(R.id.editTextTextPersonName);
        button1 =(Button)findViewById(R.id.button);
        button2 =(Button)findViewById(R.id.button2);
        cardView1 =(CardView) findViewById(R.id.cardView1);
        cardView2 =(CardView) findViewById(R.id.cardView2);
        cardView3 =(CardView) findViewById(R.id.cardView3);


       if(button2 != null) {
           button2.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
               public final void onClick(View it) {
                   editText2  = (EditText)findViewById(R.id.editTextTextPersonName);

                   if (editText2.getText().toString().equals(textView3.getText())) {
                       cardView2.setVisibility(View.VISIBLE);
                       cardView3.setVisibility(View.INVISIBLE);
                       editText2.setText("");

                   } else {

                   }


               }
           }));
       }



        if (button1 != null) {

            button1.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
                public final void onClick(View it) {

                    firstDatabase.setValue(editText1.getText().toString());
                    cardView1.setVisibility(View.VISIBLE);
                    cardView2.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.VISIBLE);



                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
                    NotificationCompat.Builder ncb = new NotificationCompat.Builder(getApplicationContext(), "01");
                    ncb.setSmallIcon(R.drawable.ic_baseline_text_snippet_24);
                    ncb.setContentTitle("Notice");
                    ncb.setContentText("Tap to see new notice.");
                    ncb.setContentIntent(pendingIntent);
                    ncb.setAutoCancel(true);
                    ncb. setPriority(NotificationCompat.PRIORITY_MAX);
                    NotificationManagerCompat nmc = NotificationManagerCompat.from(MainActivity.this);
                    nmc.notify(1, ncb.build());





                }
            }));
        }

        if (editText1 != null) {
            editText1.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
                public final void onClick(View it) {

                    cardView1.setVisibility(View.INVISIBLE);

                }
            }));
        }

        if (textView2 != null) {
            textView2.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
                public final void onClick(View it) {

                    cardView1.setVisibility(View.INVISIBLE);
                    cardView3.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);


                }
            }));
        }
        final int intervalTime = 10000; // 10 sec
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()  {
            @Override
            public void run() {
                textView1.setHint("Check your network..");
            }
        }, intervalTime);





    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {


        if(snapshot.getValue(String.class) !=null){

            String key= snapshot.getKey();
            if (key.equals("NEWS1")){

                String textView = snapshot.getValue(String.class);
                textView1.setText(textView);
                editText1.setText(textView);





            }

        }


        if(snapshot.getValue(String.class) !=null){

            String key= snapshot.getKey();
            if (key.equals("pass")){

                String textVieww=snapshot.getValue(String.class);
                textView3.setText(textVieww);

            }

        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    protected void onStart(){
        super.onStart();
        firstDatabase.addValueEventListener((ValueEventListener) this);
        secondDatabase.addValueEventListener((ValueEventListener) this);

    }



}