package edu.ib.ballgameadamczykkroczak;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;

public class RectangleView extends View {
    private Paint rectPaint;
    private Paint ballPaint;
    private Paint finishPaint;
    private Paint diamondPaint;
    private int[] a; // rozmiar przeszkód
    private int[] b;
    private int[] x; // położenie przeszkód
    private int[] y;
    private int[] pointX; // położenie diamentów
    private int[] pointY;
    private int viewWidth; // szerokość ekranu
    private int viewHeight; // wysokość ekranu
    private int radius = 15; // rozmiar piłki
    private int radiusDiamond = 5; // rozmiar diamentu
    private int ballX = 20; // początkowe położenie
    private int ballY = 20;
    private boolean ifWon = false;

    public void setIfWon(boolean ifWon) {
        this.ifWon = ifWon;
    }

    public boolean isIfWon() {
        return ifWon;
    }


    public int getBallX() {
        return ballX;
    }

    public int getBallY() {
        return ballY;
    }

    public void setBallX(int ballX) {
        this.ballX = ballX;
    }

    public void setBallY(int ballY) {
        this.ballY = ballY;
    }

    public void setA(int[] a) {
        this.a = a;
    }

    public void setB(int[] b) {
        this.b = b;
    }

    public void setX(int[] x) {
        this.x = x;
    }

    public void setY(int[] y) {
        this.y = y;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public RectangleView(Context context, int[] a, int[] b, int[] x, int[] y, int[] pointX, int[] pointY) {
        super(context); // wywołanie kostruktora klasy bazowej
        this.x = x;
        this.y = y;
        this.a = a;
        this.b = b;
        this.pointX = pointX;
        this.pointY = pointY;

        diamondPaint = new Paint();
        diamondPaint.setColor(Color.BLUE);

        finishPaint = new Paint();
        finishPaint.setColor(Color.GREEN);

        rectPaint = new Paint();
        rectPaint.setColor(Color.BLACK);

        ballPaint = new Paint();
        ballPaint.setColor(Color.RED);
    }

    // metody do wyświetlania
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        viewWidth = w;
        viewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        viewWidth = canvas.getWidth();
        viewHeight = canvas.getHeight();

        // przeszkody
        for (int i = 0; i < a.length; i++) {
            Rect rect = new Rect(x[i], y[i], x[i] + a[i], y[i] + b[i]);
            canvas.drawRect(rect, rectPaint);
        }

        for (int i = 0; i < pointX.length; i++) {
            canvas.drawCircle(pointX[i], pointY[i], radiusDiamond, diamondPaint);
        }

        invalidate(); // odświeżenie ekranu

        // punkt końcowy
        canvas.drawRect((viewWidth - 100), (viewHeight - 100), (viewWidth - 50), (viewHeight - 50), finishPaint);

        // start
        //rectPaint.setTextSize(32);
        //canvas.drawText("START", 20, 30, rectPaint);

        // piłka
        canvas.drawCircle(ballX, ballY, radius, ballPaint);

        invalidate(); // odświeżenie ekranu
    }
}
