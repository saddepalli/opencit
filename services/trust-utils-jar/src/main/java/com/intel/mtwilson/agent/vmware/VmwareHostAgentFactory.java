/*
 * Copyright (C) 2012 Intel Corporation
 * All rights reserved.
 */
package com.intel.mtwilson.agent.vmware;

import com.intel.mtwilson.datatypes.ConnectionString;
import com.intel.mtwilson.agent.HostAgent;
import com.intel.mtwilson.agent.VendorHostAgentFactory;
import com.intel.mtwilson.model.InternetAddress;
import com.intel.mtwilson.tls.TlsConnection;
import com.intel.mtwilson.tls.TlsPolicy;
import java.io.IOException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The VmwareHostAgentFactory creates instances of VmwareHostAgent. It does
 * not create instances of VmwareClient. It uses 
 * @author jbuhacoff
 */
public class VmwareHostAgentFactory implements VendorHostAgentFactory {
    private Logger log = LoggerFactory.getLogger(getClass());
    protected static VMwareConnectionPool pool = new VMwareConnectionPool(new VmwareClientFactory()); 
    
    @Override
    public VmwareHostAgent getHostAgent(InternetAddress hostAddress, String vendorConnectionString, TlsPolicy tlsPolicy) throws IOException {
        try {
            VMwareClient client = pool.getClientForConnection(new TlsConnection(vendorConnectionString, tlsPolicy)); //pool.getClientForConnection(key(vendorConnectionString, tlsPolicy));
            return new VmwareHostAgent(client, hostAddress.toString());
        }
        catch(Exception e) {
            throw new IOException("Cannot get vmware client for host: "+hostAddress.toString()+": "+e.toString(), e);
        }
    }

    @Override
    public HostAgent getHostAgent(String vendorConnectionString, TlsPolicy tlsPolicy) throws IOException {
        try {
            log.debug("getHostAgent {}", vendorConnectionString);
            VMwareClient client = pool.getClientForConnection(new TlsConnection(vendorConnectionString, tlsPolicy)); //pool.getClientForConnection(key(vendorConnectionString, tlsPolicy));
            ConnectionString.VmwareConnectionString vmware = ConnectionString.VmwareConnectionString.forURL(new URL(vendorConnectionString));
            log.debug("vmware host = {}", vmware.getHost().toString());
            log.debug("vmware port = {}", vmware.getPort());
            log.debug("vmware username = {}", vmware.getUsername());
            log.debug("vmware password = {}", vmware.getPassword());
            log.debug("vmware vcenter = {}", vmware.getVCenter().toString());
            log.debug("vmware toURL = {}", vmware.toURL());
            return new VmwareHostAgent(client, vmware.getHost().toString());
        }
        catch(Exception e) {
            throw new IOException("Cannot get vmware client for host connection: "+vendorConnectionString+": "+e.toString(), e);
        }
    }
}