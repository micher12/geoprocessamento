package components;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.mysql.cj.jdbc.Blob;

public class MySql {

    private static final String URL = "jdbc:mysql://localhost:3306/geoprocessamento";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    public MySql(){
        conectar();
    }   

    private Connection conectar() {
        Connection con;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
                        
        }catch(Exception e){
            con = null;
            JOptionPane.showMessageDialog(null, "Não foi possível conectar com o banco de dados.");
        }

        return con;
    }


    public void insertInto(String table, int alertCode, String detectado, String publicado, InputStream before, InputStream after){
        try {
            
            Connection con = conectar();

            String q = "INSERT INTO " + table + " VALUES(null,?,?,?,?,?)";

            PreparedStatement stmt = con.prepareStatement(q);

            stmt.setInt(1, alertCode);
            stmt.setString(2, detectado);
            stmt.setString(3, publicado);
            stmt.setBlob(4, before);
            stmt.setBlob(5, after);
            stmt.executeUpdate();

            stmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null,"Error ao inserir dados");
        }
    }

    public void insertIntoWithId(String table, int id,int alertCode, String[] data, InputStream blobBefore, InputStream blobAfter){
        try {
            
            Connection con = conectar();

            String q = "INSERT INTO " + table + " VALUES(?,?,?,?,?,?)";

            PreparedStatement stmt = con.prepareStatement(q);

            stmt.setInt(1, id);
            stmt.setInt(2, alertCode);
            stmt.setString(3, data[0]);
            stmt.setString(4, data[1]);
            stmt.setBlob(5, blobBefore);
            stmt.setBlob(6, blobAfter);
            stmt.executeUpdate();
            
            stmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null,"Error ao inserir dados");
        }
    }


    public List<String> selectAll(String table){
        List<String> responseList = new ArrayList<>();

        try {

            Connection con = conectar();

            String query = "SELECT img.id, img.alertCode,img.detectado,img.publicado FROM "+ table+" as img";
            PreparedStatement stmt = con.prepareStatement(query);

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                String respsonse = "id:"+result.getInt(1)+" alertCode:"+result.getInt(2)+" Detectado:"+result.getString(3)+" Publicado:"+result.getString(4);

                responseList.add(respsonse);
            }

            stmt.close();
            con.close();

        } catch (Exception e) {

            //System.out.println(e);
            JOptionPane.showMessageDialog(null,"Error ao recuperar dados.");

        }



        return responseList;
    }


    public List<Integer> getAllNumbers(String column){
        List<Integer> resultado = new ArrayList<>();
        int val = 0;

        try {
            Connection con = conectar();
            String query = "SELECT img."+column+" from images as img ";

            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                 
                    val = rs.getInt(1);
                    
                resultado.add(val);
            }

            st.close();
            con.close();


        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Falha ao recuperar Números");
        }

        return resultado;
    } 


    public List<byte[]> takeImage(int id){

        List<byte[]> finalImages = new ArrayList<>();

        try {
            Connection con = conectar();

            String query = "SELECT img.before, img.after from `images` as `img` WHERE id = ? ";

            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Blob antes = (Blob) rs.getBlob("before");
                Blob depois = (Blob) rs.getBlob("after");

                
                finalImages.add(antes.getBytes(1, (int) antes.length()));
                finalImages.add(depois.getBytes(1, (int) depois.length()));

            }


            st.close();
            con.close();


        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Falha ao recuperar imagem");
        }

        return finalImages;
    }

    public List<String> selectAllDatas(String column){
        List<String> finalResponse = new ArrayList<>();

        try {
            Connection con = conectar();

            String query = "SELECT img."+column+", img.id from images img";

            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String response = rs.getString(1)+":"+rs.getInt(2);
                finalResponse.add(response);
            }

            st.close();
            con.close();

        } catch (Exception e) {
            
        }

        return finalResponse;
    }

    public void deleteItem(int id){
        try {
            Connection con = conectar();

            String query = "delete from `images` WHERE id = ?";

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            stmt.close();
            con.close();


        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Não foi possível deletar o item!", "ERRO !", 0);
        }
    }

}
