package com.warpedcity.game.props;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.warpedcity.game.Map;

public abstract class Prop
{
    public Rectangle bounds = new Rectangle();
    public float stateTime = 0;
    public Vector2 pos = new Vector2();
    public float renderWidth;
    public float renderHeight;
    Map map;

    public Prop (Map map, float x, float y)
    {
        this.map = map;
        this.bounds.x = x;
        this.bounds.y = y;
        this.pos.set(x, y);
    }


    public void update(float deltaTime)
    {
        stateTime += deltaTime;
    }
}
