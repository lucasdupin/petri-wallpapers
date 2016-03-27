package in.lucasdup.petri;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.fpl.liquidfun.World;

import in.lucasdup.petri.base.WormWallpaperService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, WormWallpaperService.class));
        startActivity(intent);

    }
}
