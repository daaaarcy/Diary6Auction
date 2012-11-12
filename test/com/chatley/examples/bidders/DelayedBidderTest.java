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
public class DelayedBidderTest
{

    Mockery context = new Mockery();

    BidReceiver receiver = context.mock(BidReceiver.class);

    @Test
    public void doesNotBidIfThereHaveBeenNoBids()
    {
        DelayedBidder bidder = new DelayedBidder("sue", 1, 50);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void bidsImmediatelyIfThereAreNoBidsToWaitFor()
    {
        context.checking(new Expectations()
        {{
                one(receiver).bid(50);
            }});
        DelayedBidder bidder = new DelayedBidder("sue", 0, 50);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void bidsAfterOneBidAcceptedIfConfiguredToWaitForOneBid()
    {
        DelayedBidder bidder = new DelayedBidder("sue", 1, 50);
        bidder.onYourTurnToBid(receiver);
        bidder.onBidAccepted(10);

        context.checking(new Expectations()
        {{
                one(receiver).bid(50);
            }});

        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void stopsBiddingWhenCurrentBidMatchesOwnBid()
    {
        DelayedBidder bidder = new DelayedBidder("sue", 0, 20);
        bidder.onBidAccepted(20);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void stopsBiddingWhenCurrentBidExceedsOwnBid()
    {
        DelayedBidder bidder = new DelayedBidder("sue", 0, 20);
        bidder.onBidAccepted(30);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void knowsItsName()
    {
        DelayedBidder bidder = new DelayedBidder("fred", 0, 0);
        assertThat(bidder.getName(), is("fred"));
    }
}
