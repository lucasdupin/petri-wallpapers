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
                return 35;
            }

            @Override
            public int getWormSegments() {
                return 15;
            }

            @Override
            public int getWormSegmentSize() {
                return 40;
            }

            @Override
            public float getWormSpeed() {
                return 0.65f;
            }

            @Override
            public int getStrokeWidth() {
                return 5;
            }

            @Override
            public int getInteractionRadius() {
                return 35;
            }

            @Override
            public int getWormInterestRadius() {
                return 650;
            }
        });
    }
}
