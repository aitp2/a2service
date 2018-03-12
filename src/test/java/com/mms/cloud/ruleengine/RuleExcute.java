package com.mms.cloud.ruleengine;

import java.io.FileNotFoundException;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;

public class RuleExcute {
	public static void main(String[] args) throws FileNotFoundException {
        //create a person instance (fact)
    	OrderStatusFact orderStatusFact = new OrderStatusFact();
    	orderStatusFact.setOrderCode("00001");orderStatusFact.setOrderStatus("Created");
        Facts facts = new Facts();
        facts.put("orderStatusFact", orderStatusFact);

        // create rules
        MVELRule ageRule = new MVELRule()
                .name("pay yujing rule")
                .description("Check if createdTime>24h,then yujing")
                .priority(1)
                .when("orderStatusFact.orderStatus.equals(\"Created\")")
                .then("orderStatusFact.setPayStatus(\"yujing\");");

        // create a rule set
        Rules rules = new Rules();
        rules.register(ageRule);

        //create a default rules engine and fire rules on known facts
        RulesEngine rulesEngine = new DefaultRulesEngine();

        
        rulesEngine.fire(rules, facts);
        System.out.println(orderStatusFact.getPayStatus());
    }
}
