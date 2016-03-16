package b_reel.com.livewallpapercanvas.display;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import b_reel.com.livewallpapercanvas.IWallpaperConfig;

/**
 * Created by user on 3/15/16.
 */
public class Worm {
    public static final String TAG = "Worm";

    private IWallpaperConfig config;
    public static final float HUE_SPEED = 0.03f;
    public static final float MAX_HUE_ROTATION_PER_FRAME = 10f;

    // Worm components
    private PointF[] points;

    // Where to go
    private PointF destinationPoint = new PointF();

    // Randomize next position
    private Point screenBounds;

    // Drawing
    private float[] HSV = new float[] {360f * (float)Math.random(), 0.89f, 0.91f};

    public Worm(IWallpaperConfig config, Point screenBounds) {
        this.screenBounds = screenBounds;
        this.config = config;

        PointF start = new PointF((float)(screenBounds.x * Math.random()), (float) (screenBounds.y * Math.random()));

        points = new PointF[config.getWormSegments()];
        points[0] = start;

        for (int i=1; i < config.getWormSegments(); i++) {
            points[i] = new PointF(start.x + config.getWormSegmentSize() * i, start.y);
        }

        destinationPoint.set(start.x, start.y);
    }

    public void draw(Canvas c) {
        Path path = new Path();
        path.moveTo(points[0].x, points[0].y);
        for (int i=0; i < points.length - 1; i++) {
            path.quadTo(points[i].x, points[i].y, (points[i].x + points[i+1].x)/2, (points[i].y + points[i+1].y)/2);
        }
        path.lineTo(points[points.length-1].x, points[points.length-1].y);

        Paint paint  = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(config.getStrokeWidth());
        paint.setColor(Color.HSVToColor(HSV));
        paint.setStrokeCap(Paint.Cap.ROUND);
        c.drawPath(path, paint);
    }

    public void move() {
        int margin = config.getStrokeWidth();
        destinationPoint.set(
                (int) ((screenBounds.x - margin * 2) * Math.random()) + margin,
                (int) ((screenBounds.y - margin * 2) * Math.random()) + margin
        );
    }

    public void move(int x, int y) {
        destinationPoint.set(x, y);
    }

    public void update() {
        PointF start = points[0];

        // Move head if moved to the target point
        float distX = destinationPoint.x - start.x; float distY = destinationPoint.y - start.y;
        float sqDistanceTravelled = distX*distX + distY*distY;
        final int MIN_DIST = 25;

        if ( sqDistanceTravelled < MIN_DIST*MIN_DIST) {
            move();
        }
        Log.d(TAG, "Will travel " + Math.sqrt(sqDistanceTravelled) + " pixels: " + start.x + "," + start.y + " - " + destinationPoint.x + ":" + destinationPoint.y);

        // Move the head
        start.x += (destinationPoint.x - start.x) * 0.1 * config.getWormSpeed();
        start.y += (destinationPoint.y - start.y) * 0.1 * config.getWormSpeed();

        // Change color
        float hueChange = HUE_SPEED * (float) Math.sqrt(sqDistanceTravelled);
        if (hueChange > MAX_HUE_ROTATION_PER_FRAME) hueChange = MAX_HUE_ROTATION_PER_FRAME;
        HSV[0] += hueChange * config.getWormSpeed();
        if (HSV[0] >= 360) HSV[0] = 0;

        int segmentSize = config.getWormSegmentSize();

        // Move components
        for (int i=0; i < points.length - 1; i++) {
            PointF segment = points[i];
            PointF nextSegment = points[i + 1];
            // Calculate the distance between them
            PointF offset = new PointF(nextSegment.x - segment.x, nextSegment.y - segment.y);
            // Normalize vector to move it to the next position
            double length = Math.sqrt(offset.x*offset.x + offset.y*offset.y);
            offset.x /= length; offset.y /= length;
            // Move it
            nextSegment.set((int) (segment.x + offset.x * segmentSize), (int) (segment.y + offset.y * segmentSize));
        }
    }
}
