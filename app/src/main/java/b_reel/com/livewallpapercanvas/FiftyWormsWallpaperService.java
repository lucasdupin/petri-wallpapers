package b_reel.com.livewallpapercanvas;

/**
 * Created by user on 3/16/16.
 */
public class FiftyWormsWallpaperService extends WormWallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new WormWallpaperService.Engine(new IWallpaperConfig() {
            @Override
            public int getWormCount() {
                return 50;
            }

            @Override
            public int getWormSegments() {
                return 25;
            }

            @Override
            public int getWormSegmentSize() {
                return 35;
            }

            @Override
            public float getWormSpeed() {
                return 0.9f;
            }

            @Override
            public int getStrokeWidth() {
                return 5;
            }
        });
    }
}
