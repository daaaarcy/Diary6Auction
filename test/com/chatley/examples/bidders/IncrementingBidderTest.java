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
public class IncrementingBidderTest
{

    Mockery context = new Mockery();

    BidReceiver receiver = context.mock(BidReceiver.class);

    @Test
    public void bidsImmediatelyWithStartingBidIfNooneHasBid()
    {
        context.checking(new Expectations()
        {{
                one(receiver).bid(30);
            }});
        IncrementingBidder bidder = new IncrementingBidder("bert", 30, 5, 40);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void bidsAtCurrentBidPlusIncrementIfSomeoneHasBid()
    {
        context.checking(new Expectations()
        {{
                one(receiver).bid(37);
            }});
        IncrementingBidder bidder = new IncrementingBidder("bert", 30, 5, 40);
        bidder.onBidAccepted(32);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void stopsBiddingWhenCurrentBidPlusIncrementExceedsLimit()
    {
        IncrementingBidder bidder = new IncrementingBidder("bert", 30, 5, 40);
        bidder.onBidAccepted(36);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void doesNotBidWhenCurrentBidExceedsLimit()
    {
        IncrementingBidder bidder = new IncrementingBidder("bert", 30, 5, 40);
        bidder.onBidAccepted(41);
        bidder.onYourTurnToBid(receiver);
    }

    @Test
    public void knowsItsName()
    {
        IncrementingBidder bidder = new IncrementingBidder("bert", 30, 5, 40);
        assertThat(bidder.getName(), is("bert"));
    }
}
