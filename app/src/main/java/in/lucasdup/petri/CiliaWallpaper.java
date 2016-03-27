package in.lucasdup.petri;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Transform;
import com.google.fpl.liquidfun.World;

import java.util.ArrayList;
import java.util.List;

import in.lucasdup.petri.base.LiquidFunWallpaperService;
import in.lucasdup.petri.display.IDrawableBody;

/**
 * Created by lucasdupin on 3/26/16.
 */
public class CiliaWallpaper extends LiquidFunWallpaperService {

    @Override
    public LiquidFunWallpaperService.LiquidFunEngine onCreateLiquidFunEngine() {
        return new CiliaEngine();
    }

    private class CiliaEngine extends LiquidFunWallpaperService.LiquidFunEngine {

        @Override
        public void onUpdate() {

        }

        @Override
        public @NonNull List<IDrawableBody> onCreateDrawable() {
            List<IDrawableBody> list = new ArrayList<>();
            list.add(new CiliaProtozoan(world));
            return list;
        }

        private class CiliaProtozoan implements IDrawableBody {
            private Body mBody;
            float size = 100f;

            CiliaProtozoan(World world) {
                BodyDef bodyDef = new BodyDef();
                bodyDef.setPosition(getSurfaceWidth()/2, getSurfaceHeight()/2);
                bodyDef.setType(BodyType.dynamicBody);
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(size, size);

                mBody = world.createBody(bodyDef);
                mBody.createFixture(shape, 0);

                shape.delete();
                bodyDef.delete();
            }

            @Override
            public Body getBody() {
                return mBody;
            }

            @Override
            public void draw(Canvas canvas) {
            Paint p = new Paint();
            p.setStyle(Paint.Style.STROKE);
            p.setColor(Color.RED);
            p.setStrokeWidth(10);

            canvas.drawRect(mBody.getPositionX() - size/2, mBody.getPositionY() - size/2,
                    mBody.getPositionX() + size/2, mBody.getPositionY() + size/2, p);
            }
        }
    }
}
