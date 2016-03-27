package in.lucasdup.petri.display;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.google.fpl.liquidfun.Color;
import com.google.fpl.liquidfun.Draw;
import com.google.fpl.liquidfun.Transform;
import com.google.fpl.liquidfun.Vec2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;

/**
 * Drawing physics data do help debug problems
 * Created by lucasdupin on 3/26/16.
 */
public class LiquidFunDebugDraw extends Draw {

    private static final String TAG = "DebugDraw";
    Canvas canvas;

    public LiquidFunDebugDraw() {
        super();
        setFlags(Draw.SHAPE_BIT | Draw.AABB_BIT | Draw.JOINT_BIT | Draw.PARTICLE_BIT | Draw.PAIR_BIT | Draw.CENTER_OF_MASS_BIT);
    }


    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void drawPolygon(byte[] vertices, int vertexCount, Color color) {

        Paint paint = new Paint();
        paint.setColor(android.graphics.Color.argb(255, (int)(color.getR()*255), (int)(color.getG()*255), (int)(color.getB()*255)));
        paint.setStyle(Paint.Style.STROKE);

        FloatBuffer buffer = ByteBuffer.wrap(vertices).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
        if (buffer.remaining() > 2) {
            Path path = new Path();

            path.moveTo(buffer.get(), buffer.get());
            while (buffer.hasRemaining()) {
                float x = buffer.get(); float y = buffer.get();
//                Log.d(TAG, "Vertex " + new DecimalFormat("#.##").format(x) + ", " + new DecimalFormat("#.##").format(y));
                path.lineTo(x, y);
            }

            canvas.drawPath(path, paint);
            paint.setColor(android.graphics.Color.argb(100, (int)(color.getR()*255), (int)(color.getG()*255), (int)(color.getB()*255)));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, paint);

        } else if (buffer.remaining() == 2) {
            canvas.drawPoint(buffer.get(), buffer.get(), paint);
        }

    }

    public void drawSolidPolygon(byte[] vertices, int vertexCount, Color color) {

        Paint paint = new Paint();
        paint.setColor(android.graphics.Color.argb(100, (int)(color.getR()*255), (int)(color.getG()*255), (int)(color.getB()*255)));
        paint.setStyle(Paint.Style.FILL);

        FloatBuffer buffer = ByteBuffer.wrap(vertices).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
        if (buffer.remaining() > 2) {
            Path path = new Path();
            path.moveTo(buffer.get(), buffer.get());
            while (buffer.hasRemaining()) {
                float x = buffer.get(); float y = buffer.get();
//                Log.d(TAG, "Vertex " + new DecimalFormat("#.##").format(x) + ", " + new DecimalFormat("#.##").format(y));
                path.lineTo(x, y);
            }
            path.close();

            canvas.drawPath(path, paint);
        } else if (buffer.remaining() == 2) {
            canvas.drawPoint(buffer.get(), buffer.get(), paint);
        }
    }

    public void drawCircle(Vec2 center, float radius, Color color) {
        Paint paint = new Paint();
        paint.setColor(android.graphics.Color.argb(255, (int)(color.getR()*255), (int)(color.getG()*255), (int)(color.getB()*255)));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(center.getX(), center.getY(), radius, paint);

        paint = new Paint();
        paint.setColor(android.graphics.Color.argb(50, (int)(color.getR()*255), (int)(color.getG()*255), (int)(color.getB()*255)));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(center.getX(), center.getY(), radius, paint);
    }

    public void drawSolidCircle(Vec2 center, float radius, Vec2 axis, Color color) {
        Paint paint = new Paint();
        paint.setColor(android.graphics.Color.argb(255, (int)(color.getR()*255), (int)(color.getG()*255), (int)(color.getB()*255)));
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(center.getX(), center.getY(), radius, paint);
    }

    public void drawParticles(byte[] centers, float radius, byte[] colors, int count) {
        Paint paint = new Paint();
        // COLOR pick correct color from array
        paint.setColor(android.graphics.Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        FloatBuffer buffer = ByteBuffer.wrap(centers).asFloatBuffer();
//        canvas.drawPoints(buffer.array(), paint);
        while (buffer.hasRemaining()) {
            float centerX = buffer.get();
            float centerY = buffer.get();
            canvas.drawCircle(centerX, centerY, radius, paint);
        }
    }

    public void drawSegment(Vec2 p1, Vec2 p2, Color color) {
        Log.d(TAG, "draw segment");
        Paint paint = new Paint();
        paint.setColor(android.graphics.Color.argb(255, (int)(color.getR()*255), (int)(color.getG()*255), (int)(color.getB()*255)));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        canvas.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY(), paint);
    }

    public void drawTransform(Transform xf) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(android.graphics.Color.MAGENTA);

        canvas.save();

        canvas.translate(xf.getPositionX(), xf.getPositionY());
        float angle = (float) (Math.atan2(xf.getRotationSin(), xf.getRotationCos()) / Math.PI * 180.0);
        canvas.rotate(angle);

        canvas.drawLine(0, 0, 50, 0, paint);
        canvas.drawCircle(50, 0, 10, paint);

        canvas.restore();
    }

}
