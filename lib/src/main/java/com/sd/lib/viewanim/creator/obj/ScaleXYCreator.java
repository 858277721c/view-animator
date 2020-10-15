package com.sd.lib.viewanim.creator.obj;

/**
 * 缩放xy
 */
public class ScaleXYCreator extends CombineCreator
{
    public ScaleXYCreator()
    {
        super(new ScaleXCreator(), new ScaleYCreator());
    }
}
