package com.chatley.examples.bidders;

/**
 * Created with IntelliJ IDEA.
 * User: zq109
 * Date: 12/11/12
 * Time: 11:06
 * To change this template use File | Settings | File Templates.
 */
public class NamedBidder
{
    private String name;

    public static NamedBidder named(String name)
    {
        return new NamedBidder(name);
    }

    private NamedBidder(String name)
    {
        this.name = name;
    }

    public LimitBidder withLimit(int limit)
    {
        return new LimitBidder(name, limit);
    }

    public StartBidder withStart(int start)
    {
        return new StartBidder(name, start);
    }

    public DelayedBidder withNumOfBidsToWaitForAndBid(int numberOfBidsToWaitFor, int bid)
    {
        return new DelayedBidder(name, numberOfBidsToWaitFor, bid);
    }
}
