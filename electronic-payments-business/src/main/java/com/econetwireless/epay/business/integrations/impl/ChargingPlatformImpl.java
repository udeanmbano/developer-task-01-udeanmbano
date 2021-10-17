package com.econetwireless.epay.business.integrations.impl;

import com.econetwireless.epay.business.integrations.api.ChargingPlatform;
import com.econetwireless.in.soap.messages.BalanceResponse;
import com.econetwireless.in.soap.messages.CreditRequest;
import com.econetwireless.in.soap.messages.CreditResponse;
import com.econetwireless.in.soap.service.IntelligentNetworkService;

/**
 * Created by tnyamakura on 17/3/2017.
 */
public class ChargingPlatformImpl implements ChargingPlatform{

    private IntelligentNetworkService intelligentNetworkService;

    public ChargingPlatformImpl(IntelligentNetworkService intelligentNetworkService) {
        this.intelligentNetworkService = intelligentNetworkService;
    }

    @Override
    public BalanceResponse enquireBalance(final String partnerCode, final String msisdn) {
        return intelligentNetworkService.enquireBalance(partnerCode, msisdn);
    }

    @Override
    public CreditResponse creditSubscriberAccount(final CreditRequest inCreditRequest) {
        final CreditRequest creditRequest = inCreditRequest;
        return intelligentNetworkService.creditSubscriberAccount(creditRequest);
    }
}
