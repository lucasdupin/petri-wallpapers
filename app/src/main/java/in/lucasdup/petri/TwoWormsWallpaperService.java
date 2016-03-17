package in.lucasdup.petri;

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
                return 0.25f;
            }

            @Override
            public int getStrokeWidth() {
                return 60;
            }

            @Override
            public int getInteractionRadius() {
                return 150;
            }

            @Override
            public int getWormInterestRadius() {
                return 650;
            }
        });
    }
}
