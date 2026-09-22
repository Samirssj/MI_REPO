package pe.edu.repositorio.config;
public final class AppConfig {
  private AppConfig(){}
  public static String getSupabaseUrl(){return env("SUPABASE_URL");}
  public static String getPublishableKey(){return env("SUPABASE_PUBLISHABLE_KEY");}
  private static String env(String k){String v=System.getenv(k);return v==null?"":v.replaceAll("/$","");}
}
