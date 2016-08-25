package com.allhis.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ljy on 15/6/19.
 * ok
 */
public class LoginFilter extends OncePerRequestFilter {

    public LoginFilter() {
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String clientip = request.getRemoteAddr();

        //把客户端ip加入请求包
//        Map<String,String[]> m = new HashMap<String,String[]>(request.getParameterMap());
//        m.put("ip", new String[]{clientip});
//        request = new ParameterRequestWrapper(request,m);

        //通过抛出异常可以起到拦截作用
//        if(!clientip.equals("211.95.73.111")){
//            throw new AccessDeniedException("Access has been denied for your IP address: "+request.getRemoteAddr());
//        }
        //交给filterChain的其它过滤器继续处理（如果还有后续过滤器的话）
        filterChain.doFilter(request, response);
    }
}

