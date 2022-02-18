package com.example.emomusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    Face[] facesDetected;
    private int selected=0;
    private ImageView emHappy,emAngry,emSad,emNeutral,emSurprise;
    private TextView textView,logout,textView1;
    private ProgressDialog detectionProgressDialog;
    private final String apiEndpoint = "EndPoint";
    private final String subscriptionKey = "KEY";
    Button uploadPicture,playButton;
    int REQUEST_PERMISSION_CODE=101;

    private final FaceServiceClient faceServiceClient =
            new FaceServiceRestClient(apiEndpoint, subscriptionKey);
    private final int PICK_IMAGE = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        textView = (TextView) findViewById(R.id.phoneText);
        FirebaseUser user = auth.getCurrentUser();
        textView.setText("Not "+user.getPhoneNumber()+"?");
        detectionProgressDialog = new ProgressDialog(this);
        logout = findViewById(R.id.logout);
        textView1 = findViewById(R.id.textView1);
        emHappy = findViewById(R.id.image_Happy);
        emAngry = findViewById(R.id.image_Angry);
        emSad = findViewById(R.id.image_Sad);
        emNeutral = findViewById(R.id.image_Neutral);
        emSurprise = findViewById(R.id.image_Surprise);
        playButton = findViewById(R.id.buttonPlay);

        emHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTint();
                emHappy.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                playButton.setVisibility(View.VISIBLE);
                selected =1;

            }
        });
        emSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTint();
                emSad.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                playButton.setVisibility(View.VISIBLE);
                selected =2;
            }
        });
        emNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTint();
                emNeutral.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                playButton.setVisibility(View.VISIBLE);
                selected=3;

            }
        });
        emAngry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTint();
                emAngry.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                playButton.setVisibility(View.VISIBLE);
                selected =4;

            }
        });
        emSurprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTint();
                emSurprise.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                playButton.setVisibility(View.VISIBLE);
                selected=5;

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getApplicationContext(),SendOTP.class));
                finish();
            }
        });
        initViews();
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
        checkSelfPermission(Manifest.permission.WRITE_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET
            },REQUEST_PERMISSION_CODE);
        }
        detectionProgressDialog = new ProgressDialog(this);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (selected){
                    case 1:
                        intent = new Intent(getApplicationContext(),Player.class);
                        intent.putExtra("Genre", "Happy");
                        startActivity(intent);
                        finish();
                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(),Player.class);
                        intent.putExtra("Genre", "Sad");
                        startActivity(intent);
                        finish();
                        break;
                    case 3:
                        intent = new Intent(getApplicationContext(),Player.class);
                        intent.putExtra("Genre", "Neutral");
                        startActivity(intent);
                        finish();
                        break;
                    case 4:
                        intent = new Intent(getApplicationContext(),Player.class);
                        intent.putExtra("Genre", "Anger");
                        startActivity(intent);
                        finish();
                        break;
                    case 5:
                        intent = new Intent(getApplicationContext(),Player.class);
                        intent.putExtra("Genre", "Surprise");
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });

    }

    private  void removeTint(){
        playButton.setVisibility(View.GONE);
        emSurprise.setColorFilter(null);
        emSad.setColorFilter(null);
        emHappy.setColorFilter(null);
        emAngry.setColorFilter(null);
        emNeutral.setColorFilter(null);
    }

    private void initViews() {

        uploadPicture = (Button) findViewById(R.id.buttonUpload);

        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(
                        intent, "Select Picture"), PICK_IMAGE);
            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), uri);
                ImageView imageView = findViewById(R.id.imageview1);
                imageView.setImageBitmap(bitmap);


                detectAndFrame(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private  void detectAndFrame(final Bitmap imageBitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());

        AsyncTask<InputStream, String, Face[]> detectTask =
                new AsyncTask<InputStream, String, Face[]>() {
                    String exceptionMessage = "";

                    @Override
                    protected Face[] doInBackground(InputStream... params) {
                        try {
                            publishProgress("Detecting...");
                            FaceServiceClient.FaceAttributeType[] faceAttr = new FaceServiceClient.FaceAttributeType[]{
                                    FaceServiceClient.FaceAttributeType.HeadPose,
                                    FaceServiceClient.FaceAttributeType.Age,
                                    FaceServiceClient.FaceAttributeType.Gender,
                                    FaceServiceClient.FaceAttributeType.Emotion,
                                    FaceServiceClient.FaceAttributeType.FacialHair
                            };
                            Face[] result = faceServiceClient.detect(
                                    params[0],
                                    true,         // returnFaceId
                                    false,        // returnFaceLandmarks
                                    faceAttr          // returnFaceAttributes:
                                    /* new FaceServiceClient.FaceAttributeType[] {
                                        FaceServiceClient.FaceAttributeType.Age,
                                        FaceServiceClient.FaceAttributeType.Gender }
                                    */
                            );
                            if (result == null){
                                publishProgress(
                                        "Detection Finished. Nothing detected");
                                return null;
                            }
                            publishProgress(String.format(
                                    "Detection Finished. %d face(s) detected",
                                    result.length));
                            return result;
                        } catch (Exception e) {
                            exceptionMessage = String.format(
                                    "Detection failed: %s", e.getMessage());
                            return null;
                        }
                    }

                    @Override
                    protected void onPreExecute() {
                        //TODO: show progress dialog
                        detectionProgressDialog.show();
                    }
                    @Override
                    protected void onProgressUpdate(String... progress) {
                        //TODO: update progress
                        detectionProgressDialog.setMessage(progress[0]);
                    }
                    @Override
                    protected void onPostExecute(Face[] result) {
                        //TODO: update face frames
                        detectionProgressDialog.dismiss();

                        if(!exceptionMessage.equals("")){
                            showError(exceptionMessage);
                        }
                        if (result == null) return;

                        ImageView imageView = findViewById(R.id.imageview1);
                        imageView.setImageBitmap(
                                drawFaceRectanglesOnBitmap(imageBitmap, result));
                        imageBitmap.recycle();
                        if (result != null) {
                            double [] faceValues = new double[5];
                            HashMap<String,Double> map =  new HashMap<>();
                            for (Face face : result) {
                                String happy  =String.valueOf(face.faceAttributes.emotion.happiness);
                                map.put("Happy",face.faceAttributes.emotion.happiness);
                                faceValues[0] = face.faceAttributes.emotion.happiness;
                                textView1.setText("Happiness : "+happy);
                                String angry  =String.valueOf(face.faceAttributes.emotion.anger);
                                map.put("Anger",face.faceAttributes.emotion.anger);
                                faceValues[1] = face.faceAttributes.emotion.anger;
                                textView1.append("\n"+"Anger : "+angry);
                                String neutral  =String.valueOf(face.faceAttributes.emotion.neutral);
                                map.put("Neutral",face.faceAttributes.emotion.neutral);
                                faceValues[2] = face.faceAttributes.emotion.neutral;
                                textView1.append("\n"+"Neutral : "+neutral);
                                String sad  =String.valueOf(face.faceAttributes.emotion.sadness);
                                map.put("Sad",face.faceAttributes.emotion.sadness);
                                faceValues[3] = face.faceAttributes.emotion.sadness;
                                textView1.append("\n"+"Sadness : "+sad);
                                String surprise  =String.valueOf(face.faceAttributes.emotion.surprise);
                                map.put("Surprise",face.faceAttributes.emotion.surprise);
                                faceValues[4] = face.faceAttributes.emotion.surprise;
                                textView1.append("\n"+"Surprise : "+surprise);

                                break;
                            }

                            double max = 0;
                            for(double i : faceValues){
                                max= Math.max(i,max);
                            }
                            if(max ==0){
                                Toast.makeText(getApplicationContext(),"Playing Random Songs",Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getApplicationContext(),Player.class);
                                        intent.putExtra("Genre", "Happy");
                                        startActivity(intent);
                                        finish();
                                    }
                                },2500);
                            }
                            else{
                                String curGenre  = "";
                                for(String key : map.keySet()){
                                    if(max==map.get(key)){
                                        curGenre = key;
                                        break;
                                    }
                                }
                                Toast.makeText(getApplicationContext(),"Playing "+ curGenre+" Songs",Toast.LENGTH_SHORT).show();
                                String finalCurGenre = curGenre;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getApplicationContext(),Player.class);
                                        intent.putExtra("Genre", finalCurGenre.toString());
                                        startActivity(intent);
                                        finish();
                                    }
                                },2500);
                            }


                        }
                    }
                };

        detectTask.execute(inputStream);
    }

    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }})
                .create().show();
    }


    private static Bitmap drawFaceRectanglesOnBitmap(
            Bitmap originalBitmap, Face[] faces) {
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        if (faces != null) {
            for (Face face : faces) {
                FaceRectangle faceRectangle = face.faceRectangle;
                canvas.drawRect(
                        faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.top + faceRectangle.height,
                        paint);
            }
        }
        return bitmap;
    }






}