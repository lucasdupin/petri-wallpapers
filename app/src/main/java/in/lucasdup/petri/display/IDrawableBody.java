package in.lucasdup.petri.display;

import android.graphics.Canvas;

import com.google.fpl.liquidfun.Body;

/**
 * Created by lucasdupin on 3/26/16.
 */
public interface IDrawableBody {
    void draw(Canvas canvas);
    Body getBody();
}
