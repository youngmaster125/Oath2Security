package org.sid.Oath2Security;

import jakarta.servlet.http.HttpServletRequest;

public class Utility {
    public  static  String getSiteUrl(HttpServletRequest request){
            String siteUrl=request.getRequestURL().toString();
            return siteUrl.replace(request.getServletPath(),"");
    }
}
