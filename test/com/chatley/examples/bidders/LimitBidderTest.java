package com.chatley.examples.bidders;

import com.chatley.examples.auction.BidReceiver;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JMock.class)
public class LimitBidderTest
{

    Mockery context = new Mockery();
    BidReceiver receiver = context.mock(BidReceiver.class);

    @Test
    public void bidsImmediatelyAtHalfHisLimitIfNoOneElseHasBid()
    {
        context.checking(new Expectations()
        {{
                one(receiver).bid(15);
            }});
        LimitBidder bidder = new LimitBidder("andy", 30);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void bidsHalfwayBetweenCurrentBidAndLimit()
    {
        context.checking(new Expectations()
        {{
                one(receiver).bid(25);
            }});
        LimitBidder bidder = new LimitBidder("andy", 30);
        bidder.onBidAccepted(20);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void bidsAtLimitWhenCurrentBidReachesLimitMinusOne()
    {
        context.checking(new Expectations()
        {{
                one(receiver).bid(30);
            }});
        LimitBidder bidder = new LimitBidder("andy", 30);
        bidder.onBidAccepted(29);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void doesNotBidWhenCurrentBidReachesLimit()
    {
        LimitBidder bidder = new LimitBidder("andy", 30);
        bidder.onBidAccepted(30);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void doesNotBidWhenCurrentBidExceedsLimit()
    {
        LimitBidder bidder = new LimitBidder("andy", 30);
        bidder.onBidAccepted(31);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void knowsItsName()
    {
        LimitBidder bidder = new LimitBidder("george", 30);
        assertThat(bidder.getName(), is("george"));
    }
}
