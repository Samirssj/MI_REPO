package pe.edu.repositorio.web;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;
import com.fasterxml.jackson.databind.*;
public abstract class JsonServlet extends HttpServlet{
 protected static final ObjectMapper M=new ObjectMapper();
 protected void json(HttpServletResponse r,Object x)throws IOException{r.setContentType("application/json;charset=UTF-8");r.getWriter().print(x instanceof String?x:M.writeValueAsString(x));}
 protected void error(HttpServletResponse r,int status,String msg)throws IOException{r.setStatus(status);json(r,M.createObjectNode().put("error",msg));}
 protected String token(HttpServletRequest r){Object t=r.getSession(false)==null?null:r.getSession(false).getAttribute("access_token");return t==null?null:t.toString();}
 protected boolean logged(HttpServletRequest r){return token(r)!=null;}
 protected void requireLogin(HttpServletRequest r,HttpServletResponse s)throws IOException{if(!logged(r)){error(s,401,"No autenticado");}}
}
