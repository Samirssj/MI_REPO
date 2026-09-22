package pe.edu.repositorio.web;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import com.fasterxml.jackson.databind.*;
import pe.edu.repositorio.config.SupabaseClient;
@WebServlet("/api/auth/*")
public class AuthServlet extends JsonServlet{
 protected void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException{
  try{
   String p=req.getPathInfo();
   JsonNode b=M.readTree(req.getReader());
   if("/session".equals(p)){
    String t=b.path("access_token").asText("");
    if(t.isBlank())throw new IOException("Token vacío");
    SupabaseClient.get("profiles?id=eq."+java.net.URLEncoder.encode(getUserId(t),"UTF-8"),t);
    req.getSession(true).setAttribute("access_token",t);req.getSession(true).setAttribute("user_id",getUserId(t));json(res,M.createObjectNode().put("ok",true));
   }else if("/logout".equals(p)){if(req.getSession(false)!=null)req.getSession(false).invalidate();json(res,M.createObjectNode().put("ok",true));}
  }catch(Exception e){error(res,401,e.getMessage());}
 }
 protected void doGet(HttpServletRequest req,HttpServletResponse res)throws IOException{
  if(!logged(req)){error(res,401,"No autenticado");return;}json(res,M.createObjectNode().put("ok",true));
 }
 private String getUserId(String token)throws Exception{
  String u=pe.edu.repositorio.config.AppConfig.getSupabaseUrl()+"/auth/v1/user";
  var rq=java.net.http.HttpRequest.newBuilder(java.net.URI.create(u)).header("apikey",pe.edu.repositorio.config.AppConfig.getPublishableKey()).header("Authorization","Bearer "+token).GET().build();
  var rr=java.net.http.HttpClient.newHttpClient().send(rq,java.net.http.HttpResponse.BodyHandlers.ofString());
  if(rr.statusCode()!=200)throw new IOException("Token inválido");
  return M.readTree(rr.body()).path("id").asText();
 }
}
