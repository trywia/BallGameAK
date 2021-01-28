package edu.ib.ballgameadamczykkroczak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private RectangleView gameView;
    private static float NSS = (float) Math.pow(10, -9); // konwersja na sekundy
    private int[] a = new int[25];   // długość boku przeszkody
    private int[] b = new int[25];   // długość boku przeszkody
    private int[] x = new int[25]; // położenie przeszkody
    private int[] y = new int[25];
    private int[] pointX = new int[30];
    private int[] pointY = new int[30];
    private int score = 0; // punkty
    private float vx = 0;
    private float vy = 0;
    private int px = 50;
    private int py = 50;
    boolean flag = true;
    private static final String TAG = "EDUIB"; // tag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // chęć korzystania z czujników sprzętowych
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // korzystanie z akcelerometru - przyspieszenie telefonu, wraz ze składową przyspieszenia ziemskiego

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        boolean flagDraw = false;
        do { // pętla ustalająca współrzędne pierwszego elementu labiryntu
            int r = (int) (Math.random() * 2);

            if (r == 1) { // losowa orientacja prostokątnej przeszkody
                a[0] = (int) (Math.random() * (500 - 250) + 250); // długości boków przeszkody
                b[0] = 50;
            } else {
                a[0] = 50;
                b[0] = (int) (Math.random() * (500 - 250) + 250);
            }

            x[0] = (int) (Math.random() * (width - 50) + 50); // wymiary
            y[0] = (int) (Math.random() * (height - 50) + 50);

            if ((x[0] + a[0] <= width) && (y[0] + b[0] <= height - 250)) { // sprawdzenie, czy przeszkoda znajduje się w odpowiednim obszarze, nie zbyt nisko
                flagDraw = true;
            }

        } while (flagDraw == false);

        for (int i = 1; i < x.length; i++) { // pętla tworząca przeszkody / labirynt
            int r = (int) (Math.random() * 2);

            if (r == 1) { // losowa orientacja prostokątnych przeszkód
                a[i] = (int) (Math.random() * (500 - 250) + 250); // długości boków przeszkód
                b[i] = 50;
            } else {
                a[i] = 50;
                b[i] = (int) (Math.random() * (500 - 250) + 250);
            }

            x[i] = (int) (Math.random() * (width - 50) + 50); // współrzędne przeszkody
            y[i] = (int) (Math.random() * (height - 50) + 50);

            for (int j = 0; j < i; j++) { // pętla zagnieżdżona niepozwalająca na nachodzenie na siebie przeszkód i na utrzymanie ich w oknie graficznym
                if ((x[i] + a[i] >= x[j] - 50) && (x[i] <= x[j] + 50 + a[j]) && (y[i] + b[i] >= y[j] - 50) && (y[i] <= y[j] + 50 + b[j]) || (x[i] + a[i] >= width) || (y[i] + b[i] >= height - 250)) {
                    i--;
                    break;
                }
            }
        }

        for (int i = 0; i < pointX.length; i++) { // pętla losująca współrzędne punktów
            pointX[i] = (int) (Math.random() * (width - 220) + 100);
            pointY[i] = (int) (Math.random() * (height - 250) + 100);

            for (int j = 0; j < x.length; j++) { // pętla zagnieżdżona niepozwalająca, by znajdowały się one na przeszkodach
                if ((pointX[i] >= x[j] - 5) && (pointX[i] <= x[j] + a[j] + 5) && (pointY[i] >= y[j] - 5) && (pointY[i] <= y[j] + b[j] + 5)) {
                    i--;
                    break;
                }
            }
        }

        gameView = new RectangleView(this, a, b, x, y, pointX, pointY);
        // setContentView(gameView);

        RectangleView[] rectTable = new RectangleView[a.length];

        for (int i = 0; i < a.length; i++) {
            //rectTable[i] = new RectangleView(this, a, b, x, y);
            rectTable[i] = gameView;
            setContentView(rectTable[i]);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // odczytanie bieżących wskazań

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height;
//        int width;


        float timeStamp = event.timestamp; // zwracanie czasu pomiaru, ns * 10^-9
        float actualTime = timeStamp * NSS;

        //vx = 0;
        //vy = 1;


        float[] values = event.values;
        int ax = (int) values[0];
        int ay = (int) values[1];
        Log.d(TAG, String.valueOf(vx + " " + vy));

        int width = gameView.getWidth();
        int height = gameView.getHeight();

        vx -= ax / 3;
        vy += ay / 3;

        for (int i = 0; i < pointX.length; i++) {
            if (gameView.getBallX() >= pointX[i] - 15 && gameView.getBallX() <= pointX[i] + 15 && gameView.getBallY() >= pointY[i] - 15 && gameView.getBallY() <= pointY[i] + 15) { //jeśli pacman najedzie na punkt, punkt znika, a gracz otrzymuje punkt
                pointX[i] = -100;
                pointY[i] = -100;
                score++;
            }
        }

        for (int i = 0; i < x.length; i++) { //kolizje pacmana z przeszkodami
            if (gameView.getBallX() + 15 >= x[i] && gameView.getBallX() - 15 <= x[i] + a[i] && gameView.getBallY() + 15 >= y[i] && gameView.getBallY() - 15 <= y[i] + b[i]) {
                if (px + 15 >= x[i] && px - 15 <= x[i] + a[i]) {
                    vy -= 1.5 * vy;
                } else if (py + 15 >= y[i] && py - 15 <= y[i] + b[i]) {
                    vx -= 1.5 * vx;
                } else {
                    vx -= 1.5 * vx;
                    vy -= 1.5 * vy;
                }
                gameView.setBallY(py);
                gameView.setBallX(px);
            }
        }

        if (gameView.getBallX() < 15 || gameView.getBallX() > width - 15) { //kolizje pacmana z brzegami ekranu
            vx -= 1.5 * vx; //-vx * (float) 1.5;
            gameView.setBallX(px);
        }
        if (gameView.getBallY() < 15 || gameView.getBallY() > height - 15) { //kolizje pacmana z brzegami ekranu
            vy -= 1.5 * vy; //-vy * (float) 1.5;
            gameView.setBallY(py);
        }

        px = gameView.getBallX();
        py = gameView.getBallY();

        gameView.setBallX(gameView.getBallX() + (int) vx / 3); //ruch pacmana
        gameView.setBallY(gameView.getBallY() + (int) vy / 3);

        // (viewWidth - 100), (viewHeight - 100), (viewWidth - 50), (viewHeight - 50)

        if (gameView.getBallX() + 15 >= (width - 100) && gameView.getBallX() - 15 <= (width - 50) && gameView.getBallY() + 15 >= (height - 100) && gameView.getBallY() - 15 <= (height - 50)) {
            gameView.setIfWon(true);
        }

        if (!gameView.isIfWon()) {
            setContentView(gameView);
        } else if (gameView.isIfWon() && flag) {
            setContentView(R.layout.activity_main);
            TextView points = (TextView) findViewById(R.id.tvPoints);
            points.setText("Score: " + score + "/30");
            flag = false;
        }
    }

    // metody decydujące o tym, że nie sprawdzamy czujników, kiedy aplikacja nie jest użytkowana
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME); // zczytujemy wskazania akcelerometru
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this); // stopujemy wskazania
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onBtnRestartClick(View view) {
        if (gameView.isIfWon()) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}