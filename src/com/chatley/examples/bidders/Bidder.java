package com.chatley.examples.bidders;

/**
 * Created with IntelliJ IDEA.
 * User: zq109
 * Date: 12/11/12
 * Time: 10:56
 * To change this template use File | Settings | File Templates.
 */
public interface Bidder
{
    LimitBidder withLimit(int limit);

    StartBidder withStart(int start);
}
