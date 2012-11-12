package com.chatley.examples.bidders;

/**
 * Created with IntelliJ IDEA.
 * User: zq109
 * Date: 12/11/12
 * Time: 10:57
 * To change this template use File | Settings | File Templates.
 */
public class Money
{
    private int amount;

    private Money(int amount)
    {
        this.amount = amount;
    }

    public static Money gbp(int n)
    {
        return new Money(n);
    }

}
