package com.econetwireless.epay.business.integrations.api;

import com.econetwireless.in.soap.messages.BalanceResponse;
import com.econetwireless.in.soap.messages.CreditRequest;
import com.econetwireless.in.soap.messages.CreditResponse;
import com.econetwireless.utils.pojo.INBalanceResponse;
import com.econetwireless.utils.pojo.INCreditRequest;
import com.econetwireless.utils.pojo.INCreditResponse;

/**
 * Created by tnyamakura on 17/3/2017.
 */
public interface ChargingPlatform {

    BalanceResponse enquireBalance(final String partnerCode, final String msisdn);
    CreditResponse creditSubscriberAccount(final CreditRequest inCreditRequest);
}
