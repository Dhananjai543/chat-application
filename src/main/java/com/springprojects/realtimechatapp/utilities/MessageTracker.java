package com.springprojects.realtimechatapp.utilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MessageTracker {

    public static Map<String, Set<String>> map;

    public static boolean checkMessageIfAlreadyConsumed(String username, String messageKey){
        if(map==null){
            map = new HashMap<>();
        }

        if(!map.containsKey(username)){
            map.put(username, new HashSet<>());
        }

        return map.get(username).contains(messageKey);
    }
    
    public static void clearMessages(String user) {
    	if(map!=null && map.containsKey(user)) {
    		map.get(user).clear();
    	}
    }

}
