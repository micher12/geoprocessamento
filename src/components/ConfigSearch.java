package components;

public class ConfigSearch {
    
    private static String estrutura;
    private static String orderBy;

    public ConfigSearch(String dataEstruture, String order){
        if(dataEstruture != null)
            estrutura = dataEstruture;
        if(order != null)
            orderBy = order;
            
    }

    public static String getEstrutura(){
        return estrutura;
    }

    public static String getOrderBy(){
        return orderBy;
    }

}
