package b_reel.com.livewallpapercanvas;

/**
 * Created by user on 3/16/16.
 */
public class TwoWormsWallpaperService extends WormWallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new WormWallpaperService.Engine(new IWallpaperConfig() {
            @Override
            public int getWormCount() {
                return 2;
            }

            @Override
            public int getWormSegments() {
                return 20;
            }

            @Override
            public int getWormSegmentSize() {
                return 30;
            }

            @Override
            public float getWormSpeed() {
                return 1.5f;
            }

            @Override
            public int getStrokeWidth() {
                return 60;
            }
        });
    }
}
