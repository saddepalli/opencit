/**
 * 
 */
package com.intel.mountwilson.controller;

import com.intel.mountwilson.constant.HelperConstant;
import com.intel.mtwilson.datatypes.HostVMMType;
import com.intel.mtwilson.datatypes.HostWhiteListTarget;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * @author yuvrajsx
 *
 */
@Controller
public class ManagementConsoleViewController extends MultiActionController{
	
	private static final Logger logger = LoggerFactory.getLogger(ManagementConsoleViewController.class.getName());
	
	public ModelAndView getWhiteListConfigurationPage(HttpServletRequest req,HttpServletResponse res){
		ModelAndView modelAndView = new ModelAndView("WhiteListConfiguration");
		try {
			
			List<String> hostNameList = new ArrayList<String>();
			
			HostVMMType[] hostVMMTypes = HostVMMType.values();
			for (HostVMMType hostVMMType : hostVMMTypes) {
				hostNameList.add(hostVMMType.getValue());
			}
					
			List<Map<String, Object>> hostList = new ArrayList<Map<String,Object>>(); 
			for (String hostName : hostNameList) {
				Map<String, Object> map = new HashMap<String, Object>();
				boolean isVmm = false;
				if (hostName.toLowerCase().contains(HelperConstant.VMWARE_TYPE.toLowerCase())) {
					isVmm = true;
				}
				map.put("hostName", hostName);
				map.put("isVMM", isVmm);
				map.put("pcrs", getHostVMMTypeFromValue(hostName));
				hostList.add(map);
			}
			modelAndView.addObject("hostTypeList",hostList);
			List<String> wlBiosList = new ArrayList<String>();
			wlBiosList.add(HostWhiteListTarget.BIOS_OEM.getValue());
			wlBiosList.add(HostWhiteListTarget.BIOS_HOST.getValue());
			modelAndView.addObject("BIOSWhiteList",wlBiosList);
			
			List<String> wlVMMList = new ArrayList<String>();
			wlVMMList.add(HostWhiteListTarget.VMM_OEM.getValue());
			wlVMMList.add(HostWhiteListTarget.VMM_HOST.getValue());
			wlVMMList.add(HostWhiteListTarget.VMM_GLOBAL.getValue());			
                        modelAndView.addObject("vmmWhiteList",wlVMMList);
			
		} catch (Exception e) {
			logger.error("Error while Getting Host Entry from Config file."+e.getMessage());
			modelAndView.addObject("result",false);
			modelAndView.addObject("message","Error while Getting Host Entry from HostVMMType Class.");
			return modelAndView;
		}
		modelAndView.addObject("result",true);
		return modelAndView;
	}
	
	private String getHostVMMTypeFromValue(String hostName) {
		for (HostVMMType hostType : HostVMMType.values()) {
			if (hostType.getValue().equals(hostName)) {
				return hostType.getPcrs();
			}
		}
		return "";
	}

	public ModelAndView getRegisterHostPage(HttpServletRequest req,HttpServletResponse res ) {
		return new ModelAndView("RegisterHost");
	}

	public ModelAndView getApproveRequestPage(HttpServletRequest req,HttpServletResponse res ) {
		return new ModelAndView("ApproveRequest");
	}
	public ModelAndView getApproveRejectPage(HttpServletRequest req,HttpServletResponse res ) {
		return new ModelAndView("ApproveReject");
	}
	public ModelAndView getViewExpiringPage(HttpServletRequest req,HttpServletResponse res ) {
		return new ModelAndView("ViewExpiring");
	}
	
	public ModelAndView getRekeyApiClient(HttpServletRequest req,HttpServletResponse res ) {
		return new ModelAndView("RekeyApiClient");
	}
	
	public ModelAndView getDeleteRegistrationPage(HttpServletRequest req,HttpServletResponse res ) {
		return new ModelAndView("DeleteRegistration");
	}
	
	public ModelAndView getViewRequestPage(HttpServletRequest req,HttpServletResponse res ) {
		return new ModelAndView("ViewRequest");
	}
    public ModelAndView getRegisterPage(HttpServletRequest req,HttpServletResponse res) {
	logger.info("WLMViewController.getRegisterPage >>");
	return new ModelAndView("Register");
    }	
   /*--Begin Added by Soni on 18/10/12 for New Screen for CA */
    public ModelAndView getCAStatusPage(HttpServletRequest req,HttpServletResponse res ) {
		return new ModelAndView("CAStatus");
	}
    
    /*--End Added by Soni on 18/10/12 for New Screen for CA */
    
    /*--Begin Added by Soni on 18/10/12 for New Screen for CA */
    public ModelAndView getSAMLCertificatePage(HttpServletRequest req,HttpServletResponse res ) {
    	logger.info("MGViewController.getSAMLCertificatePage >>");
		return new ModelAndView("SAMLDownload");
	
	}
    
    /*--End Added by Soni on 18/10/12 for New Screen for CA */
    
    /*--Begin Added by stdalex on 1/8/13 mc fingerprint */
    public ModelAndView getViewCertPage(HttpServletRequest req,HttpServletResponse res ) {
    	logger.info("MGViewController.getViewCertPage >>");
        return new ModelAndView("CertDownload");
    }
    
    /*--End Added by stdale MC fingerprint 1/8/13 */
    
    /*public ModelAndView getDefineWhiteListConfig(HttpServletRequest req,HttpServletResponse res) {
    	logger.info("WLMViewController.getDefineWhiteListConfig >>");
    	return new ModelAndView("DefineWhiteListConfig");
    }*/
}
