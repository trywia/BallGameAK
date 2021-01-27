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
    private int[] a;
    private int[] b;
    private int[] x; // położenie
    private int[] y;
    private int viewWidth; // szerokość ekranu
    private int viewHeight; // wysokość ekranu
    private long lastTime;
    private int radius = 15;
    private int ballX = 50;
    private int ballY = 50;
    private Context context;
    private TextView tvEnd;

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


    public RectangleView(Context context, int[] a, int[] b, int[] x, int[] y) {
        super(context); // wywołanie kostruktora klasy bazowej
        this.x = x;
        this.y = y;
        this.a = a;
        this.b = b;

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
        // tvEnd = new TextView(super(context));

        for (int i = 0; i < a.length; i++) {
            Rect rect = new Rect(x[i], y[i], x[i] + a[i], y[i] + b[i]);
            canvas.drawRect(rect, rectPaint);
        }
        invalidate(); // odświeżenie ekranu
        // przy eskperymentowaniu z przemieszczaniem się kulki

        canvas.drawRect((viewWidth - 100), (viewHeight - 100), (viewWidth - 50), (viewHeight - 50), rectPaint);

        rectPaint.setTextSize(32);
        canvas.drawText("START", 20, 30, rectPaint);

        canvas.drawCircle(ballX, ballY, radius, ballPaint);
        invalidate(); // odświeżenie ekranu
    }
}
