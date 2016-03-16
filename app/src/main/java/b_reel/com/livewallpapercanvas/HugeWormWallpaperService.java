package b_reel.com.livewallpapercanvas;

/**
 * Created by user on 3/16/16.
 */
public class HugeWormWallpaperService extends WormWallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new WormWallpaperService.Engine(new IWallpaperConfig() {
            @Override
            public int getWormCount() {
                return 1;
            }

            @Override
            public int getWormSegments() {
                return 20;
            }

            @Override
            public int getWormSegmentSize() {
                return 300;
            }

            @Override
            public float getWormSpeed() {
                return 1;
            }

            @Override
            public int getStrokeWidth() {
                return 200;
            }

            @Override
            public int getInteractionRadius() {
                return 25;
            }

            @Override
            public int getWormInterestRadius() {
                return 650;
            }
        });
    }
}
