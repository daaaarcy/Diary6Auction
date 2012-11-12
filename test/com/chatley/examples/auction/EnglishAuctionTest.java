package com.chatley.examples.auction;

import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.action.VoidAction;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JMock.class)
public class EnglishAuctionTest
{

    Mockery context = new Mockery();

    AuctionMouthpiece mouthpiece = context.mock(AuctionMouthpiece.class);
    EnglishAuction auction = new EnglishAuction(mouthpiece);

    @Test
    public void registersBiddersToListenToAuctionMouthpiece() throws Exception
    {

        final Participant bidder = context.mock(Participant.class);

        context.checking(new Expectations()
        {{
                one(mouthpiece).addListener(bidder);
            }});

        auction.accept(bidder);
    }

    @Test
    public void runsARoundWhenTheAuctionStarts() throws Exception
    {

        context.checking(new Expectations()
        {{
                one(mouthpiece).runARound(with(any(Auctioneer.class)));
            }});

        auction.run();
    }

    @Test
    public void runsRoundsUntilTheBidsStop() throws Exception
    {

        context.checking(new Expectations()
        {{
                one(mouthpiece).onBidAccepted(10);
                one(mouthpiece).onBidAccepted(20);
                exactly(3).of(mouthpiece).runARound(with(any(Auctioneer.class)));
                will(onConsecutiveCalls(bid(10), bid(20), nobid()));
            }});

        auction.run();
    }

    @Test
    public void isWonByHighestBidder() throws Exception
    {

        final Ledger result = context.mock(Ledger.class);
        final Participant highBidder = context.mock(Participant.class, "highbidder");
        final Participant lowBidder = context.mock(Participant.class, "lowbidder");
        final int HIGH_BID = 50;

        context.checking(new Expectations()
        {{
                ignoring(mouthpiece);
                one(result).win(highBidder, HIGH_BID);
            }});

        auction.submitBid(lowBidder, 25);
        auction.submitBid(highBidder, HIGH_BID);

        auction.close(result);
    }

    @Test
    public void allowsBiddersToUpdateTheirBidsUntilAuctionCloses() throws Exception
    {

        final Ledger result = context.mock(Ledger.class);
        final Participant bidder1 = context.mock(Participant.class, "bidder1");
        final Participant bidder2 = context.mock(Participant.class, "bidder2");
        final int HIGHEST_BID = 75;

        context.checking(new Expectations()
        {{
                ignoring(mouthpiece);
                one(result).win(bidder1, HIGHEST_BID);
            }});

        auction.submitBid(bidder1, 25);
        auction.submitBid(bidder2, 50);
        auction.submitBid(bidder1, HIGHEST_BID);

        auction.close(result);
    }

    @Test
    public void hasNoWinnerIfReservePriceNotMet() throws Exception
    {

        auction.withReservePrice(100);

        final Ledger result = context.mock(Ledger.class);
        final Participant highBidder = context.mock(Participant.class, "highbidder");
        final Participant lowBidder = context.mock(Participant.class, "lowbidder");

        context.checking(new Expectations()
        {{
                ignoring(mouthpiece);
                one(result).noWinner();
            }});

        auction.submitBid(lowBidder, 25);
        auction.submitBid(highBidder, 50);

        auction.close(result);
    }

    @Test
    public void signalsWhetherBidsWereAcceptedOrNot() throws Exception
    {

        final Participant highBidder = context.mock(Participant.class, "highbidder");
        final Participant lowBidder = context.mock(Participant.class, "lowbidder");

        context.checking(new Expectations()
        {{
                ignoring(mouthpiece);
            }});

        auction.accept(lowBidder);
        auction.accept(highBidder);

        assertTrue(auction.submitBid(lowBidder, 25));
        assertTrue(auction.submitBid(highBidder, 50));
        assertFalse(auction.submitBid(lowBidder, 30));
    }

    @Test
    public void updatesAllBiddersWithNewLeadingBids() throws Exception
    {

        final Participant highBidder = context.mock(Participant.class, "highbidder");
        final Participant lowBidder = context.mock(Participant.class, "lowbidder");

        context.checking(new Expectations()
        {{
                one(mouthpiece).onBidAccepted(25);
                one(mouthpiece).onBidAccepted(50);
            }});

        auction.submitBid(lowBidder, 25);
        auction.submitBid(highBidder, 50);
        auction.submitBid(lowBidder, 30);
    }

    private Action bid(final int bid)
    {
        return new Action()
        {

            public Object invoke(Invocation invocation) throws Throwable
            {
                ((Auctioneer) invocation.getParameter(0)).submitBid(dummyBidder(), bid);
                return null;
            }

            public void describeTo(Description description)
            {
                description.appendText("dummy bidder bidding ").appendValue(bid);
            }

        };
    }

    private Action nobid()
    {
        return VoidAction.INSTANCE;
    }

    private Participant dummyBidder()
    {
        return context.mock(Participant.class, "dummy" + UUID.randomUUID());
    }

}
