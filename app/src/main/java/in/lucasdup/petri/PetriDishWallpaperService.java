package in.lucasdup.petri;

import in.lucasdup.petri.base.WormWallpaperService;

/**
 * Created by user on 3/16/16.
 */
public class PetriDishWallpaperService extends WormWallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new WormWallpaperService.Engine(new IWormConfig() {
            @Override
            public int getWormCount() {
                return 50;
            }

            @Override
            public int getWormSegments() {
                return 5;
            }

            @Override
            public int getWormSegmentSize() {
                return 10;
            }

            @Override
            public float getWormSpeed() {
                return 0.1f;
            }

            @Override
            public int getStrokeWidth() {
                return 10;
            }

            @Override
            public int getInteractionRadius() {
                return 200;
            }

            @Override
            public int getWormInterestRadius() {
                return 450;
            }
        });
    }
}
