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
public class PrimeBidderTest
{

    Mockery context = new Mockery();
    BidReceiver receiver = context.mock(BidReceiver.class);

    @Test
    public void startsBiddingAtStart()
    {
        context.checking(new Expectations()
        {{
                one(receiver).bid(2);
            }});
        PrimeBidder bidder = new PrimeBidder("sally", 2, 47);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void bidsAtNextHighestPrimeNumberAboveCurrentBid()
    {
        context.checking(new Expectations()
        {{
                one(receiver).bid(23);
            }});
        PrimeBidder bidder = new PrimeBidder("sally", 2, 47);
        bidder.onBidAccepted(20);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void bidsAtLastPrimeNumberBeforeLimitWhenCurrentBidIsJustBelow()
    {
        context.checking(new Expectations()
        {{
                one(receiver).bid(47);
            }});
        PrimeBidder bidder = new PrimeBidder("sally", 2, 50);
        bidder.onBidAccepted(43);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void bidsAtLimitWhenNoMorePrimeNumbersBetweenCurrentBidAndLimit()
    {
        context.checking(new Expectations()
        {{
                one(receiver).bid(50);
            }});
        PrimeBidder bidder = new PrimeBidder("sally", 2, 50);
        bidder.onBidAccepted(47);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void knowsItsName()
    {
        PrimeBidder bidder = new PrimeBidder("sally", 2, 47);
        assertThat(bidder.getName(), is("sally"));
    }
}
