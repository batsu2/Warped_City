package com.warpedcity.game.props;

import com.warpedcity.game.Map;

public class CokeSign extends Prop
{
    public CokeSign (Map map, float x, float y, float width, float height)
    {
        super(map, x, y);
        renderWidth = 2;
        renderHeight = 4;
        this.bounds.width = width; //1
        this.bounds.height = height; //1
    }
}
