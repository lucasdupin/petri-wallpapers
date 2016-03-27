package in.lucasdup.petri.base;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceHolder;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.ParticleSystem;
import com.google.fpl.liquidfun.ParticleSystemDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.World;

import java.util.List;

import in.lucasdup.petri.display.IDrawableBody;
import in.lucasdup.petri.display.LiquidFunDebugDraw;

/**
 * Base class for creating physics wallpapers
 * Created by lucasdupin on 3/26/16.
 */
public abstract class LiquidFunWallpaperService extends WallpaperService {
    public static final String TAG = "LW";
    public static final boolean DEBUG = true;

    public abstract LiquidFunEngine onCreateLiquidFunEngine();

    @Override
    public void onCreate() {
        super.onCreate();

        // Require our libs
        System.loadLibrary("liquidfun");
        System.loadLibrary("liquidfun_jni");
    }

    @Override
    public Engine onCreateEngine() {
        return onCreateLiquidFunEngine();
    }

    public abstract class LiquidFunEngine extends WallpaperService.Engine implements Runnable {
        protected Handler handler;

        // Physics world
        protected World world;
        // World boundaries
        protected Body mBoundaryBody;
        private static final float BOUNDARY_THICKNESS = 1.0f;
        // Physics update variables
        private static final int VELOCITY_ITERATIONS = 6;
        private static final int POSITION_ITERATIONS = 2;
        private static final int PARTICLE_ITERATIONS = 5;
        private static final float WORLD_HEIGHT = 3f;

        // World size in px
        private float surfaceWidth;
        private float surfaceHeight;
        // World size in meters
        private float worldWidth;
        private float worldHeight;

        // All visible elements
        private List<IDrawableBody> drawables;
        // Debug what's being drawn
        private LiquidFunDebugDraw debugDraw;

        /**
         * Implement this method to receive a callback on the
         * render loop
         */
        public abstract void onUpdate();

        /**
         * Create all your drawable items, attaching them to rigid bodies
         * @return list of all drawable elements
         */
        public abstract @NonNull List<IDrawableBody> onCreateDrawable();

        private void initBoundaries() {

            // clean up previous Body if exists
            if (mBoundaryBody != null) {
                world.destroyBody(mBoundaryBody);
            }

            // Create native objects
            BodyDef bodyDef = new BodyDef();
            bodyDef.setType(BodyType.staticBody);
            PolygonShape boundaryPolygon = new PolygonShape();

            mBoundaryBody = world.createBody(bodyDef);

            // boundary definitions
            // top
            boundaryPolygon.setAsBox(
                    worldWidth,
                    BOUNDARY_THICKNESS,
                    worldWidth / 2,
                    worldHeight + BOUNDARY_THICKNESS,
                    0);
            mBoundaryBody.createFixture(boundaryPolygon, 0.0f);
            // bottom
            boundaryPolygon.setAsBox(
                    worldWidth,
                    BOUNDARY_THICKNESS,
                    worldWidth / 2,
                    -BOUNDARY_THICKNESS,
                    0);
            mBoundaryBody.createFixture(boundaryPolygon, 0.0f);
            // left
            boundaryPolygon.setAsBox(
                    BOUNDARY_THICKNESS,
                    worldHeight,
                    -BOUNDARY_THICKNESS,
                    worldHeight / 2,
                    0);
            mBoundaryBody.createFixture(boundaryPolygon, 0.0f);
            // right
            boundaryPolygon.setAsBox(
                    BOUNDARY_THICKNESS,
                    worldHeight,
                    worldWidth + BOUNDARY_THICKNESS,
                    worldHeight / 2,
                    0);
            mBoundaryBody.createFixture(boundaryPolygon, 0.0f);

            Log.d(TAG, "Created at: " + 200+","+
                            +200+","+
                            +(surfaceWidth / 2 - 100)+","+
                            +(surfaceWidth/ 2 - 100));

            // Clean up native objects
            bodyDef.delete();
            boundaryPolygon.delete();

        }

        private void initDrawables() {
            if (drawables != null) {
                for (IDrawableBody drawableBody : drawables) {
                    world.destroyBody(drawableBody.getBody());
                    drawableBody.getBody().delete();
                }
            }
            // Get drawable elements
            drawables = onCreateDrawable();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            handler = new Handler();
            world = new World(0, 10);
//            ParticleSystemDef particleSystemDef = new ParticleSystemDef();
//            ParticleSystem particleSystem = world.createParticleSystem(particleSystemDef);
//            particleSystemDef.delete();

            debugDraw = new LiquidFunDebugDraw();
            world.setDebugDraw(debugDraw);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                handler.post(this);
            } else {
                handler.removeCallbacks(this);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
//            worldHeight = WORLD_HEIGHT;
//            worldWidth = width * WORLD_HEIGHT / height;
            surfaceWidth = width;
            surfaceHeight = height;
            worldWidth = width;
            worldHeight = height;
            Log.d(TAG, "World size: " + worldWidth + "x" + worldHeight);

            // Reset the boundary
            initBoundaries();
            initDrawables();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            if (drawables != null) {
                for (IDrawableBody drawableBody : drawables) {
                    world.destroyBody(drawableBody.getBody());
                    drawableBody.getBody().delete();
                }
            }
            world.destroyBody(mBoundaryBody);
            mBoundaryBody.delete();
            world.delete();
        }

        @Override
        public void run() {
            onUpdate();

            int frameInterval = 1000/60;
            world.step( frameInterval,
                    VELOCITY_ITERATIONS,
                    POSITION_ITERATIONS, PARTICLE_ITERATIONS);

            Canvas canvas = getSurfaceHolder().lockCanvas();
            if (canvas != null) {
                try {
                    // Background
                    canvas.drawColor(android.graphics.Color.BLACK);
                    canvas.save();
                    Log.d(TAG, "Scale " + (surfaceHeight / worldHeight));
//                    canvas.scale(surfaceWidth / worldWidth,
//                            surfaceHeight / worldHeight);

                    // Each bacteria
                    for (IDrawableBody drawable :  drawables) {
                        drawable.draw(canvas);
                    }

                    if (DEBUG) {
                        debugDraw.setCanvas(canvas);
                        world.drawDebugData();
                    }

                    canvas.restore();
                } finally {
                    getSurfaceHolder().unlockCanvasAndPost(canvas);
                }
            }

            handler.postDelayed(this, frameInterval); // Render loop
        }

        public float getSurfaceWidth() {
            return surfaceWidth;
        }

        public float getSurfaceHeight() {
            return surfaceHeight;
        }
    }
}
