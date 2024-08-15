package com.union.main;

import com.union.log.partner.PartnerRealtimeLogQn;
import com.union.util.CommUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RealTimeLogHandle {
	
	private static final String readMe = "HELP! ARGS: \n"
			  + "args[0]: handle partner type \n"
			  + "				    31/32/33\n"
			  + "args[1]: generate spark log ,\n"
			  + "         0: no,  1: yes               \n"
			  + "		   \n";
	
	public static void main(String[] args) throws Exception {
		
		if(args.length == 0 || args.length > 2){
			System.out.println(readMe);
			System.exit(0);
		}
		
		try {
			System.out.println();
			System.out.println(CommUtils.currentlyTime() + "-- Realtime log handle start...");
			
			String resultMsg = "success";
			// handle log
			if(args[0].equals("1")){
				
				resultMsg = PartnerRealtimeLogQn.handleRealtimeLog(new ArrayList(),new HashMap<String, String>(),args[1]);
			}
			System.out.println(CommUtils.currentlyTime() + " .... ResultMsg: " + resultMsg);
			System.out.println(CommUtils.currentlyTime() + " .... End");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

}
