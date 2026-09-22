package pe.edu.repositorio.web;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import pe.edu.repositorio.config.SupabaseClient;
@WebServlet("/api/weeks")
public class WeeksServlet extends JsonServlet{
 protected void doGet(HttpServletRequest q,HttpServletResponse r)throws IOException{try{json(r,SupabaseClient.get("weeks?select=*&order=week_number.asc",token(q)));}catch(Exception e){error(r,500,e.getMessage());}}
}
