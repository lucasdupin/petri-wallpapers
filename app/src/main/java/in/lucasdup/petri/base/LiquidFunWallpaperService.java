package in.lucasdup.petri.base;

import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.google.fpl.liquidfun.Draw;
import com.google.fpl.liquidfun.ParticleSystem;
import com.google.fpl.liquidfun.ParticleSystemDef;
import com.google.fpl.liquidfun.World;

/**
 * Created by lucasdupin on 3/26/16.
 */
public abstract class LiquidFunWallpaperService extends WallpaperService {

    public abstract LiquidFunEngine onCreateLiquidFunEngine();

    @Override
    public void onCreate() {
        super.onCreate();
        // Explicitly load all shared libraries for Android 4.1 (Jelly Bean)
        // Or we could get a crash from dependencies.
        System.loadLibrary("liquidfun");
        System.loadLibrary("liquidfun_jni");
    }

    @Override
    public Engine onCreateEngine() {
        return onCreateLiquidFunEngine();
    }

    public class LiquidFunEngine extends WallpaperService.Engine implements Runnable {
        protected Handler handler;
        protected World world;

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            world = new World(0, 0);
            ParticleSystemDef particleSystemDef = new ParticleSystemDef();
            ParticleSystem particleSystem = world.createParticleSystem(particleSystemDef);
            particleSystemDef.delete();
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
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            world.delete();
        }

        @Override
        public void run() {


            handler.postDelayed(this, 1000/60); // Render loop
        }
    }
}
