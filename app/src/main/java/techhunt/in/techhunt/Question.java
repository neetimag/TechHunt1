package techhunt.in.techhunt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.github.tbouron.shakedetector.library.ShakeDetector;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class Question extends AppCompatActivity {

    private static final int REQUEST_CODE_QR_SCAN = 101;
    private Button scan,scan1,submit;
    private TextView question,qno;
    private EditText answer;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ImageView image;
    private Animation fadeIn;
    private ProgressDialog mProgress;
    private String position; // The current question number that a user is on.
    private DatabaseReference mDatabase;
    private int x;


    private String[] questions={
            "Shuruwat ki ye tasweer hai,tareekh ye 27-10-12 hai yha pahuchna tumhari taqdeer hai",
            "A court of all love,with balls from above,the way you must get is spilt with a net",
            "Can it be this easy?", // Image
            "Water water everywhere, make sure it is PH balanced here in.",
            "Ocimum basilicum\n", // Shake and textview
            "Yahan pakde gaye the kaafi sare log aur ki gayi thi kaafiyon ki jeb khaali.",
            "I am very easy to get into but very hard to get out of?",
            "Yaad hai vo din jab ghisai katai thi majboori, konsi thi vo jagah jahan karte the hum sabhi mjdoori?",
            "Pankaj ka hai ye ghar,andar gaye toh jaoge mar",
            "Are you getting tired? Dont get sad take a seat while looking for the next clue.Its where you sit with your lad.",
            "Chot lag jaye kabhi to chale jana yahan,\nDawai mile na mile uski khushboo zaroor milegi.",
            "16-13-3",
            "Yaad hai vo 3310 wale din?\n 2-3-444-8-444",
            "This has a net but cannot catch a fish. Your next clue can be found with your player's swish.",
            "Find your luck in navratna."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        scan = (Button) findViewById(R.id.scan);
        scan1 = (Button) findViewById(R.id.scan1);
        submit = (Button) findViewById(R.id.submit);
        question = (TextView) findViewById(R.id.question);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = getSharedPreferences("pref", MODE_PRIVATE).edit();
        image = (ImageView) findViewById(R.id.image);
        answer = (EditText) findViewById(R.id.answer);
        qno = (TextView) findViewById(R.id.qno);

        mProgress = new ProgressDialog(this);

        mProgress.setTitle("Loading...Be patient");
        mProgress.setMessage("Please keep your internet ON\nThis can take a while");
        mProgress.setCanceledOnTouchOutside(false);
        fadeIn = new AlphaAnimation((float)0.2 , 1);
        fadeIn.setDuration(1000);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        position = pref.getString("position","1");
        question.setText(questions[Integer.parseInt(position)-1]);
        qno.setText("Level " + position);

        startHints();

        if(position.equals("16")){

            Intent i = new Intent(Question.this,Winner.class);
            startActivity(i);
            finish();

        }


        if(position.equals("3")){

            image.setVisibility(View.VISIBLE);
            scan1.setVisibility(View.VISIBLE);
            scan.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.INVISIBLE);
            answer.setVisibility(View.INVISIBLE);
        }
        else if(position.equals("5")){

            image.setVisibility(View.INVISIBLE);
            scan.setVisibility(View.VISIBLE);
            scan1.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.INVISIBLE);
            answer.setVisibility(View.INVISIBLE);
        }

        else if(position.equals("10")||position.equals("12")||position.equals("13")||position.equals("14")||position.equals("15")){

            answer.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
            image.setVisibility(View.INVISIBLE);
            scan.setVisibility(View.INVISIBLE);
            scan1.setVisibility(View.INVISIBLE);
        }
        else{
            answer.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.INVISIBLE);
            image.setVisibility(View.INVISIBLE);
            scan.setVisibility(View.VISIBLE);
            scan1.setVisibility(View.INVISIBLE);

        }


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Question.this,QrCodeActivity.class);
                startActivityForResult( i,REQUEST_CODE_QR_SCAN);

            }
        });

        scan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Question.this,QrCodeActivity.class);
                startActivityForResult( i,REQUEST_CODE_QR_SCAN);

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {


                if(position.equals("5")&&answer.getText().toString().trim().equalsIgnoreCase("sakhtlaunda")){

                    mProgress.show();
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("5").child(pref.getString("username","m")).setValue("solved").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mProgress.dismiss();
                            int x = Integer.parseInt(position);
                            x = x + 1;
                            position = String.valueOf(x);
                            question.setText(questions[x-1]);
                            qno.setText("Level " + position);
                            editor.putString("position",position);
                            editor.commit();

                            question.startAnimation(fadeIn);

                            answer.setVisibility(View.INVISIBLE);
                            submit.setVisibility(View.INVISIBLE);
                            scan.setVisibility(View.VISIBLE);
                            image.setVisibility(View.INVISIBLE);
                            scan1.setVisibility(View.INVISIBLE);
                            answer.setText("");

                        }
                    });





                }
                else if(position.equals("10")&&answer.getText().toString().trim().equalsIgnoreCase("Apple")){


                    mProgress.show();
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("10").child(pref.getString("username","m")).setValue("solved").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mProgress.dismiss();
                            int x = Integer.parseInt(position);
                            x = x + 1;
                            position = String.valueOf(x);
                            question.setText(questions[x-1]);
                            qno.setText("Level " + position);
                            editor.putString("position",position);
                            editor.commit();

                            question.startAnimation(fadeIn);

                            answer.setVisibility(View.INVISIBLE);
                            submit.setVisibility(View.INVISIBLE);
                            image.setVisibility(View.INVISIBLE);
                            scan.setVisibility(View.VISIBLE);
                            scan1.setVisibility(View.INVISIBLE);
                            answer.setText("");

                        }
                    });



                }


                else if(position.equals("12")&&answer.getText().toString().trim().equalsIgnoreCase("themassenergyrelationnerd")){


                    mProgress.show();
                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    mDatabase.child("12").child(pref.getString("username","m")).setValue("solved").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mProgress.dismiss();
                            int x = Integer.parseInt(position);
                            x = x + 1;
                            position = String.valueOf(x);
                            question.setText(questions[x-1]);
                            qno.setText("Level " + position);
                            editor.putString("position",position);
                            editor.commit();

                            question.startAnimation(fadeIn);

                            answer.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                            image.setVisibility(View.INVISIBLE);
                            scan.setVisibility(View.INVISIBLE);
                            scan1.setVisibility(View.INVISIBLE);
                            answer.setText("");
                            answer.setHint("4 letters");

                        }
                    });



                }

                else if(position.equals("13")&&answer.getText().toString().trim().equalsIgnoreCase("BOMB")){

                    mProgress.show();
                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    mDatabase.child("13").child(pref.getString("username","m")).setValue("solved").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mProgress.dismiss();
                            int x = Integer.parseInt(position);
                            x = x + 1;
                            position = String.valueOf(x);
                            question.setText(questions[x-1]);
                            qno.setText("Level " + position);
                            editor.putString("position",position);
                            editor.commit();

                            question.startAnimation(fadeIn);

                            answer.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                            image.setVisibility(View.INVISIBLE);
                            scan.setVisibility(View.INVISIBLE);
                            scan1.setVisibility(View.INVISIBLE);
                            answer.setText("");
                            answer.setHint("7 letters");

                        }
                    });



                }

                else if(position.equals("14")&&answer.getText().toString().trim().equalsIgnoreCase("fission")){

                    mProgress.show();
                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    mDatabase.child("14").child(pref.getString("username","m")).setValue("solved").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mProgress.dismiss();
                            int x = Integer.parseInt(position);
                            x = x + 1;
                            position = String.valueOf(x);
                            question.setText(questions[x-1]);
                            qno.setText("Level " + position);
                            editor.putString("position",position);
                            editor.commit();

                            question.startAnimation(fadeIn);

                            answer.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                            image.setVisibility(View.INVISIBLE);
                            scan.setVisibility(View.INVISIBLE);
                            scan1.setVisibility(View.INVISIBLE);
                            answer.setText("");
                            answer.setHint("5 letters");


                        }
                    });


                }

                else if(position.equals("15")&&answer.getText().toString().trim().equalsIgnoreCase("peace")){


                    mProgress.show();
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    final Date currentTime = Calendar.getInstance().getTime();

                    mDatabase.child("15").child(pref.getString("username","m")).setValue(currentTime.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mProgress.dismiss();

                            editor.putString("time",currentTime.toString());
                            editor.putString("position","16");
                            editor.commit();
                            Intent i = new Intent(Question.this,Winner.class);
                            startActivity(i);
                            finish();

                        }
                    });

                }

                else
                    Toast.makeText(getApplicationContext(),"Invalid Answer!",Toast.LENGTH_SHORT).show();

            }
        });


        ShakeDetector.create(this, new ShakeDetector.OnShakeListener() {

            @Override
            public void OnShake() {

                if(position.equals("5")){

                    answer.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    answer.setHint("No spaces. 11 characters");
                    scan1.setVisibility(View.INVISIBLE);
                    image.setVisibility(View.INVISIBLE);
                    scan.setVisibility(View.INVISIBLE);

                    answer.startAnimation(fadeIn);
                    submit.startAnimation(fadeIn);

                }

            }
        });



    }

    private void startHints() {

        if(position.equals("10"))
            answer.setHint("5 letters");
        else if(position.equals("12"))
            answer.setHint("No spaces");
        else if(position.equals("13"))
            answer.setHint("4 letters");
        else if(position.equals("14"))
            answer.setHint("7 letters");
        else if(position.equals("15"))
            answer.setHint("5 letters");

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK)
        {
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if( result!=null)
            {
                Toast.makeText(getApplicationContext(),"Scan error!",Toast.LENGTH_SHORT).show();
            }
            return;

        }
        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            showQuestion(result);

        }
    }

    private void showQuestion(String result) {

        x = Integer.parseInt(position);


        if(result.equals("techhunt" + position)) {

            mProgress.show();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(position).child(pref.getString("username","m")).setValue("solved").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    mProgress.dismiss();
                    x = x + 1;
                    position = String.valueOf(x);
                    question.setText(questions[x-1]);
                    question.startAnimation(fadeIn);
                    qno.setText("Level " + position);
                    editor.putString("position",position);
                    editor.commit();


                    if(position.equals("3")){
                        image.setVisibility(View.VISIBLE);
                        image.setImageResource(R.drawable.que3);
                        scan1.setVisibility(View.VISIBLE);
                        scan.setVisibility(View.INVISIBLE);
                    }

                    else if(position.equals("10")){

                        scan.setVisibility(View.INVISIBLE);
                        scan1.setVisibility(View.INVISIBLE);
                        answer.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        answer.setHint("5 letters");
                    }

                    else if(position.equals("12")){

                        scan.setVisibility(View.INVISIBLE);
                        scan1.setVisibility(View.INVISIBLE);
                        answer.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        answer.setHint("No spaces");
                    }

                    else{

                        image.setVisibility(View.INVISIBLE);
                        scan1.setVisibility(View.INVISIBLE);
                        scan.setVisibility(View.VISIBLE);
                    }


                }
            });





        }

        else if(position.equals("5"))
            Toast.makeText(getApplicationContext(),"Think out of the box!",Toast.LENGTH_LONG).show();

        else
            Toast.makeText(getApplicationContext(),"Invalid QR code!",Toast.LENGTH_SHORT).show();


    }


    @Override
    protected void onResume() {
        super.onResume();
        ShakeDetector.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShakeDetector.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShakeDetector.destroy();
    }

}
