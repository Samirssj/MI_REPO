package pe.edu.repositorio.config;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.file.*;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public final class SupabaseClient {
  private static final ObjectMapper M=new ObjectMapper();
  private SupabaseClient(){}
  private static HttpRequest.Builder req(String path,String token){
    String u=AppConfig.getSupabaseUrl()+"/rest/v1/"+path;
    return HttpRequest.newBuilder(URI.create(u))
      .header("apikey",AppConfig.getPublishableKey())
      .header("Authorization","Bearer "+(token==null||token.isBlank()?AppConfig.getPublishableKey():token));
  }
  public static JsonNode get(String path,String token)throws Exception{
    HttpResponse<String> r=HttpClient.newHttpClient().send(req(path,token).GET().build(),HttpResponse.BodyHandlers.ofString());
    check(r);return M.readTree(r.body());
  }
  public static JsonNode post(String path,JsonNode body,String token)throws Exception{
    HttpResponse<String> r=HttpClient.newHttpClient().send(req(path,token).header("Content-Type","application/json").header("Prefer","return=representation").POST(HttpRequest.BodyPublishers.ofString(body.toString())).build(),HttpResponse.BodyHandlers.ofString());
    check(r);return r.body().isBlank()?NullNode.instance:M.readTree(r.body());
  }
  public static JsonNode patch(String path,JsonNode body,String token)throws Exception{
    HttpResponse<String> r=HttpClient.newHttpClient().send(req(path,token).header("Content-Type","application/json").header("Prefer","return=representation").method("PATCH",HttpRequest.BodyPublishers.ofString(body.toString())).build(),HttpResponse.BodyHandlers.ofString());
    check(r);return r.body().isBlank()?NullNode.instance:M.readTree(r.body());
  }
  public static void delete(String path,String token)throws Exception{
    HttpResponse<String> r=HttpClient.newHttpClient().send(req(path,token).DELETE().build(),HttpResponse.BodyHandlers.ofString());check(r);
  }
  public static String upload(String path,byte[] data,String contentType,String token)throws Exception{
    String u=AppConfig.getSupabaseUrl()+"/storage/v1/object/repository-files/"+path;
    HttpRequest r=HttpRequest.newBuilder(URI.create(u)).header("apikey",AppConfig.getPublishableKey()).header("Authorization","Bearer "+token).header("Content-Type",contentType).header("x-upsert","true").POST(HttpRequest.BodyPublishers.ofByteArray(data)).build();
    HttpResponse<String> x=HttpClient.newHttpClient().send(r,HttpResponse.BodyHandlers.ofString());check(x);
    return AppConfig.getSupabaseUrl()+"/storage/v1/object/public/repository-files/"+path;
  }
  public static void removeFile(String path,String token)throws Exception{
    String u=AppConfig.getSupabaseUrl()+"/storage/v1/object/repository-files/"+path;
    HttpRequest r=HttpRequest.newBuilder(URI.create(u)).header("apikey",AppConfig.getPublishableKey()).header("Authorization","Bearer "+token).method("DELETE",HttpRequest.BodyPublishers.noBody()).build();
    HttpResponse<String>x=HttpClient.newHttpClient().send(r,HttpResponse.BodyHandlers.ofString());check(x);
  }
  private static void check(HttpResponse<String> r)throws IOException{if(r.statusCode()<200||r.statusCode()>=300)throw new IOException("Supabase "+r.statusCode()+": "+r.body());}
  public static String getPublicFileUrl(String path){return AppConfig.getSupabaseUrl()+"/storage/v1/object/public/repository-files/"+path;}
  public static ObjectNode object(){return M.createObjectNode();}
  public static ArrayNode array(){return M.createArrayNode();}
  public static String text(JsonNode n,String k){return n.hasNonNull(k)?n.get(k).asText():"";}
}
