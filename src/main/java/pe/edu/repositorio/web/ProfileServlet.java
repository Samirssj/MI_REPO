package pe.edu.repositorio.web;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pe.edu.repositorio.config.SupabaseClient;
@WebServlet("/api/profile")
@MultipartConfig(maxFileSize=10*1024*1024)
public class ProfileServlet extends JsonServlet{
 protected void doGet(HttpServletRequest q,HttpServletResponse r)throws IOException{try{var x=SupabaseClient.get("profiles?select=*&limit=1",token(q));if(x.size()==0){error(r,404,"Perfil no configurado");return;}json(r,x.get(0));}catch(Exception e){error(r,500,e.getMessage());}}
 protected void doPut(HttpServletRequest q,HttpServletResponse r)throws IOException{requireLogin(q,r);if(r.isCommitted())return;try{
  String uid=getProfileId(q);ObjectNode b=SupabaseClient.object();b.put("name",q.getParameter("name"));b.put("career",q.getParameter("career"));b.put("description",q.getParameter("description"));b.put("updated_at",java.time.OffsetDateTime.now().toString());
  Part p=q.getPart("photo");if(p!=null&&p.getSize()>0){String path="profiles/"+uid+"/photo-"+System.currentTimeMillis()+"-"+safe(p.getSubmittedFileName());b.put("photo_path",path);b.put("photo_url",SupabaseClient.upload(path,p.getInputStream().readAllBytes(),p.getContentType(),token(q)));}
  json(r,SupabaseClient.patch("profiles?id=eq."+uid,b,token(q)));
 }catch(Exception e){error(r,500,e.getMessage());}}
 private String getProfileId(HttpServletRequest q)throws Exception{return (String)q.getSession(false).getAttribute("user_id");}
 private String safe(String s){return s==null?"file":s.replaceAll("[^A-Za-z0-9._-]","_");}
}
