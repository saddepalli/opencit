/*
 * Copyright (C) 2013 Intel Corporation
 * All rights reserved.
 */
package com.intel.mtwilson.policy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XXX TODO this class really isn't needed, other than to have a "something" into which
 * both a host report and any policy may be input, and a result obtained.
 * maybe move the convenience methods applyAll and applyAny
 * into a utility class?      PolicyUtil.applyAll(HostReport hostReport, TrustPolicy... policies) and
 * PolicyUtil.applyAny(HostReport hostReport, TrustPolicy... policies)  ??
 * 
 * @author jbuhacoff
 */
public class PolicyEngine {
    private Logger log = LoggerFactory.getLogger(getClass());
    
    // this is the normal case - given a list of policies, apply them all, and combine the results into one report.
    public List<RuleResult> applyAll(HostReport hostReport, Rule... rules) {
        ArrayList<RuleResult> list = new ArrayList<RuleResult>();
        for(Rule rule : rules) {
            RuleResult result = rule.apply(hostReport);
            list.add(result);
        }
        return list;
    }
    
    public List<RuleResult> applyAll(HostReport hostReport, Set<Rule> rules) {
        ArrayList<RuleResult> list = new ArrayList<RuleResult>();
        for(Rule rule : rules) {
            RuleResult result = rule.apply(hostReport);
            list.add(result);
        }
        return list;
    }
    
    // this was formerly called "applyAny" because if ANY ONE of the policies says isTrusted then it's fine. 
    // however,  this is not the right spot to check that... so where do we check it ??? in the app?? that
    // would then be a feature of the app. which is fine.
    public List<TrustReport> applyAll(HostReport hostReport, Policy... policies) {
        ArrayList<TrustReport> list = new ArrayList<TrustReport>();
        for(Policy policy : policies) {
            TrustReport report = apply(hostReport, policy);
            list.add(report);
        }
        return list;
    }
    
    /*
    public TrustReport applyAny(HostReport hostReport, TrustPolicy... policies) {
        RequireAny requireAny = new RequireAny(Arrays.asList(policies));
        TrustReport report = requireAny.apply(hostReport);
        return report;
    }*/
    
    public TrustReport apply(HostReport hostReport, Policy policy) {
        TrustReport policyReport = new TrustReport(policy.getName());
        List<RuleResult> results = applyAll(hostReport, policy.getRules());
        Iterator<RuleResult> it = results.iterator();
        while(it.hasNext()) {
            RuleResult result = it.next();
            policyReport.addResult(result);
        }
        return policyReport;
    }
    
}
