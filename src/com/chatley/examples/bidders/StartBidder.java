package com.chatley.examples.bidders;

/**
 * Created with IntelliJ IDEA.
 * User: zq109
 * Date: 12/11/12
 * Time: 11:15
 * To change this template use File | Settings | File Templates.
 */
public class StartBidder
{

    private String name;
    private int start;

    StartBidder(String name, int start)
    {
        this.name = name;
        this.start = start;
    }

    public PrimeBidder withMax(int max)
    {
        return new PrimeBidder(name, start, max);
    }

    public IncrementingBidder withIncrAndLimit(int incr, int limit)
    {
        return new IncrementingBidder(name, start, incr, limit);
    }


}
