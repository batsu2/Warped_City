package com.warpedcity.game.props;

import com.warpedcity.game.Map;

public class BannerNeon extends Prop
{
    public BannerNeon (Map map, float x, float y, float width, float height)
    {
        super(map, x, y);
        renderWidth = 3;
        renderHeight = 5;
        this.bounds.width = width; //1
        this.bounds.height = height; //2
    }
}
