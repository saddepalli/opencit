/*
 * Copyright (C) 2014 Intel Corporation
 * All rights reserved.
 */
package com.intel.mtwilson.shiro.authc.host;

import com.intel.dcsg.cpg.net.Hostname;
import com.intel.dcsg.cpg.net.IPv4Address;
import com.intel.dcsg.cpg.net.InternetAddress;
import java.util.HashSet;

/**
 *
 * @author jbuhacoff
 */
public class HostAllowCsvFilter implements HostFilter {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HostAllowCsvFilter.class);

    private String allow;
    private HashSet<String> allowIp4 = new HashSet<>();
    private HashSet<String> allowHost = new HashSet<>();
    
    public HostAllowCsvFilter(String allow) {
        setAllow(allow);
    }
    
    private void setAllow(String csv) {
        this.allow = csv;
        String[] list = csv.replace(" ", "").split(",");
        for (String item : list) {
            log.debug("Allow from {}", item);
            InternetAddress address = new InternetAddress(item);
            if (address.isHostname()) {
                allowHost.add(address.toString());
            } else if (address.isIPv4()) {
                allowIp4.add(address.toString());
            } else {
                // TODO:  support for CIDR address blocks for ips like 192.168.1.0/24 and wildcards for hostnames like *.intel.com
                log.error("Invalid address '{}'", item);
                throw new IllegalArgumentException("Address must be hostname or IPv4");
            }
        }        
    }
    public String getAllow() { return allow; }
    
    @Override
    public boolean accept(String address) {
        if( IPv4Address.isValid(address) && allowIp4.contains(address) ) {
            return true;
        }
        if( Hostname.isValid(address) && allowHost.contains(address) ) {
            return true;
        }
        return false;
    }
    
}
