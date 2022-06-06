package com.warpedcity.game.props;

import com.warpedcity.game.Map;

public class SushiSign extends Prop
{
    public SushiSign (Map map, float x, float y, float width, float height)
    {
        super(map, x, y);
        renderWidth = 4;
        renderHeight = 2;
        this.bounds.width = width; //1
        this.bounds.height = height; //1
    }
}
