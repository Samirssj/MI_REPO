package pe.edu.repositorio.web;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.net.URLEncoder;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pe.edu.repositorio.config.SupabaseClient;
@WebServlet("/api/resources/*")
@MultipartConfig(maxFileSize=50*1024*1024)
public class ResourcesServlet extends JsonServlet{
 protected void doGet(HttpServletRequest q,HttpServletResponse r)throws IOException{try{
  String type=q.getParameter("type"),week=q.getParameter("week"),path="resources?select=*,weeks(week_number,title)&order=created_at.desc";
  if(type!=null)path+="&type=eq."+enc(type); if(week!=null){var w=SupabaseClient.get("weeks?select=id&week_number=eq."+enc(week),token(q));if(w.size()>0)path+="&week_id=eq."+w.get(0).path("id").asText();}
  var x=SupabaseClient.get(path,token(q));if(q.getParameter("week")!=null){var out=M.createObjectNode();var w=SupabaseClient.get("weeks?select=*&week_number=eq."+enc(week),token(q));out.set("week",w.get(0));out.set("resources",normalize(x));json(r,out);}else json(r,normalize(x));
 }catch(Exception e){error(r,500,e.getMessage());}}
 protected void doPost(HttpServletRequest q,HttpServletResponse r)throws IOException{save(q,r,null);}
 protected void doPut(HttpServletRequest q,HttpServletResponse r)throws IOException{save(q,r,q.getPathInfo()==null?null:q.getPathInfo().substring(1));}
 protected void doDelete(HttpServletRequest q,HttpServletResponse r)throws IOException{requireLogin(q,r);try{String id=q.getPathInfo().substring(1);var x=SupabaseClient.get("resources?id=eq."+enc(id)+"&select=file_path",token(q));if(x.size()>0&&!x.get(0).path("file_path").isNull()&&!x.get(0).path("file_path").asText().isBlank())SupabaseClient.removeFile(x.get(0).path("file_path").asText(),token(q));SupabaseClient.delete("resources?id=eq."+enc(id),token(q));json(r,M.createObjectNode().put("ok",true));}catch(Exception e){error(r,500,e.getMessage());}}
 private void save(HttpServletRequest q,HttpServletResponse r,String id)throws IOException{requireLogin(q,r);try{
  ObjectNode b=SupabaseClient.object().put("title",q.getParameter("title")).put("description",q.getParameter("description")).put("week_id",q.getParameter("week_id")).put("type",q.getParameter("type")).put("external_url",q.getParameter("external_url"));
  Part f=q.getPart("file");if(f!=null&&f.getSize()>0){String path="resources/"+java.util.UUID.randomUUID()+"-"+safe(f.getSubmittedFileName());b.put("file_path",path);b.put("file_name",safe(f.getSubmittedFileName()));b.put("external_url","");SupabaseClient.upload(path,f.getInputStream().readAllBytes(),f.getContentType(),token(q));}
  if(id==null){b.put("created_by",getUserId(q));json(r,SupabaseClient.post("resources",b,token(q)));}else{b.put("updated_at",java.time.OffsetDateTime.now().toString());json(r,SupabaseClient.patch("resources?id=eq."+enc(id),b,token(q)));}
 }catch(Exception e){error(r,500,e.getMessage());}}
 private String getUserId(HttpServletRequest q)throws Exception{return (String)q.getSession(false).getAttribute("user_id");}
 private ArrayNode normalize(JsonNode x){var a=M.createArrayNode();for(JsonNode n:x){ObjectNode o=n.deepCopy();JsonNode w=o.remove("weeks");if(w!=null&&!w.isNull()){o.set("week_number",w.path("week_number"));}if(o.path("external_url").asText("").isBlank()&&!o.path("file_path").asText("").isBlank())o.put("public_url",SupabaseClient.getPublicFileUrl(o.path("file_path").asText()));a.add(o);}return a;}
 private String enc(String s)throws UnsupportedEncodingException{return URLEncoder.encode(s,"UTF-8");} private String safe(String s){return s==null?"file":s.replaceAll("[^A-Za-z0-9._-]","_");}
}
