package electronicbusinesstests;

import com.econetwireless.epay.business.config.EpayBusinessConfig;
import com.econetwireless.epay.business.config.IntegrationsConfig;
import com.econetwireless.epay.business.config.RootConfig;
import com.econetwireless.in.soap.messages.CreditRequest;
import com.econetwireless.utils.enums.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by Udean Mbano on 17/3/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
        @ContextConfiguration(classes = {RootConfig.class}),
        @ContextConfiguration(classes = {IntegrationsConfig.class}),
        @ContextConfiguration(classes = {EpayBusinessConfig.class})
})
@WebAppConfiguration
public class ChargingPlatformUnitTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockChargingMvc;

    public String partnerCode;
    public String msisdn;
    public String referenceNumber;
    public double amount;
    @Before
    public void setup() {
        this.mockChargingMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        partnerCode = "hot-recharge";
        msisdn="773303584";
        referenceNumber="CREDIT-REF-0001";
        amount=5.00;
    }
    @Test
    public void chargingPlatformEnquireBalanceShouldReturnResponseCodeSUCCESSIfAllOtherSystemsAreUp() throws Exception {
        this.mockChargingMvc.perform(get("/intelligent-network-api/IntelligentNetworkService/enquireBalance/{partnerCode}/balances/{mobileNumber}", partnerCode, msisdn).accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).
                andExpect(status().isOk()).
                andExpect(content().contentType("application/json;charset=UTF-8")).
                andExpect(jsonPath("$.responseCode").value(ResponseCode.SUCCESS.getCode())).
                andExpect(jsonPath("$.amount").value(is(greaterThan(0d))));
    }
    @Test
    public void creditSubscriberAccountShouldReturnResponseCodeSUCCESSIfAllOtherSystemsAreUp() throws Exception {
        final CreditRequest creditRequest = new CreditRequest();
        creditRequest.setAmount(amount);
        creditRequest.setMsisdn(msisdn);
        creditRequest.setPartnerCode(partnerCode);
        creditRequest.setReferenceNumber(referenceNumber);
        this.mockChargingMvc.perform(post("/intelligent-network-api/IntelligentNetworkService/creditSubscriberAccount").content(asJsonString(creditRequest)).
                contentType(MediaType.parseMediaType("application/json;charset=UTF-8")).accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).
                andExpect(status().isOk()).
                andExpect(content().contentType("application/json;charset=UTF-8")).
                andExpect(jsonPath("$.responseCode").value(ResponseCode.SUCCESS.getCode())).
                andExpect(jsonPath("$.balance").value(is(greaterThan(creditRequest.getAmount()))));
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
