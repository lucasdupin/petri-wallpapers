package in.lucasdup.petri.base;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

import in.lucasdup.petri.IWormConfig;
import in.lucasdup.petri.display.Worm;

/**
 * Simple bacteria wallpaper, not cilia, no flagellum
 * Created by lucasdupin on 3/14/16.
 */
public class WormWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        throw new Error("Override this method");
    }

    protected class Engine extends WallpaperService.Engine implements Runnable {
        private IWormConfig config;
        private Handler handler = new Handler();
        private List<Worm> worms = new ArrayList<>();
        private final Object lock = new Object();

        private int lastWidth;
        private int lastHeight;

        public Engine(@NonNull IWormConfig config) {
            this.config = config;
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            synchronized (lock) {
                super.onSurfaceCreated(holder);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            synchronized (lock) {
                super.onSurfaceChanged(holder, format, width, height);
                createWorms(width, height);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            synchronized (lock) {
                super.onSurfaceDestroyed(holder);
                handler.removeCallbacks(this);
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            synchronized (lock) {
                super.onVisibilityChanged(visible);
                if (visible) {
                    run();
                } else {
                    createWorms(lastWidth, lastHeight);
                }
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                for (Worm worm : worms) {
                    double distanceFromTouch = Math.pow(event.getX() - worm.getX(), 2) + Math.pow(event.getY() - worm.getY(), 2);
                    if (distanceFromTouch < config.getWormInterestRadius()*config.getWormInterestRadius())
                        worm.move((int) event.getX(), (int) event.getY());
                }

            }
        }

        public void createWorms(int width, int height) {
            worms.clear();
            while (worms.size() < config.getWormCount()) {
                worms.add(new Worm(config, new Point(width, height)));
            }
            lastWidth = width;
            lastHeight = height;
        }

        public void moveWorms() {
            for (Worm worm : worms) {
                worm.update();
            }
        }

        @Override
        public void run() {
            draw();
            if (this.isVisible())
                handler.postDelayed(this, 1000 / 30);
        }

        public void draw() {
            this.moveWorms();

            synchronized (lock) {
                SurfaceHolder holder = getSurfaceHolder();
                Canvas canvas;
                try {
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        canvas.drawColor(Color.BLACK);
                        for (Worm worm : worms) {
                            worm.draw(canvas);
                        }
                    }
                    holder.unlockCanvasAndPost(canvas);
                } catch (IllegalArgumentException e){
                    Log.d("WS", "Invalid canvas");
                }
            }
        }
    }
}
