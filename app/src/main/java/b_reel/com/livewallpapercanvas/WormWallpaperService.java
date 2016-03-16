package b_reel.com.livewallpapercanvas;

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

import b_reel.com.livewallpapercanvas.display.Worm;

/**
 * Created by user on 3/14/16.
 */
public class WormWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        throw new Error("Override this method");
    }

    protected class Engine extends WallpaperService.Engine implements Runnable {
        private IWallpaperConfig config;
        private Handler handler = new Handler();
        private List<Worm> worms = new ArrayList<>();

        private int lastWidth;
        private int lastHeight;

        public Engine(@NonNull IWallpaperConfig config) {
            this.config = config;
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            createWorms(width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            handler.removeCallbacks(this);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                run();
            } else {
                createWorms(lastWidth, lastHeight);
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                for (Worm worm : worms)
                    worm.move((int) event.getX(), (int) event.getY());
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
            SurfaceHolder holder = getSurfaceHolder();
            this.moveWorms();

            try {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.BLACK);
                    for (Worm worm : worms) {
                        worm.draw(canvas);
                    }
                }
                holder.unlockCanvasAndPost(canvas);
            } catch (IllegalArgumentException e){
                Log.e("WS", "CANVAS Illegal arg");
                e.printStackTrace();
            }
        }
    }
}
