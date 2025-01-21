import java.io.*;

import javax.swing.*;
import components.ConfigSearch;
import components.Estrutura;
import components.ImageData;
import components.MySql;
import components.Tempo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.util.concurrent.ThreadLocalRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.*;
import java.util.*;

public class mainController {

    int section = 0; //equivale a resultado.
    File afterFileImage = null, beforeFileImage = null;
    byte[] beforeImageBlob, afterImageBlob;

    private List<String> res;

    @FXML
    private TableColumn<ImageData, Integer> alertCodeTable;

    @FXML
    private TableColumn<ImageData, Void> actionTable;

    @FXML
    private Button btnBeforeImage;

    @FXML
    private Button orderBtn;

    @FXML
    private Button cadastrarDado;

    @FXML
    private TableColumn<ImageData, String> detectadoTable;

    @FXML
    private TableColumn<ImageData, Integer> idTable;

    @FXML
    private Button linear;

    @FXML
    private ToggleGroup orderList;

    @FXML
    private TableColumn<ImageData, String> publicadoTable;

    @FXML
    private Label sizeAfter;

    @FXML
    private Label sizeBefore;

    @FXML
    private TableView<ImageData> table;

    @FXML
    private Label timerResults;

    @FXML
    private Label title;

    @FXML
    private TextField txAlertCode;

    @FXML
    private TextField txDetected;

    @FXML
    private TextField txID;

    @FXML
    private TextField txPosted;

    @FXML
    private MenuItem updateData;


    private MySql mysql;

    public void initialize(){
        idTable.setCellValueFactory(new PropertyValueFactory<>("id"));
        publicadoTable.setCellValueFactory(new PropertyValueFactory<>("publicado"));
        detectadoTable.setCellValueFactory(new PropertyValueFactory<>("detectado"));
        alertCodeTable.setCellValueFactory(new PropertyValueFactory<>("alertCode"));
    }

    public mainController(){
        mysql = new MySql();
    
        Platform.runLater(()->{
            buscarData();
        });
    }


    @FXML
    void updateController(ActionEvent event) {
        atualizarDados();
    }

    @FXML
    void configurarBusca(ActionEvent event){

        try {
            Parent root2 = FXMLLoader.load(getClass().getResource("/assets/configOrder.fxml"));

            Stage stage = new Stage();
            stage.setTitle("Configurar busca");
            stage.setScene(new Scene(root2));
            Image iconConfig = new Image(getClass().getResourceAsStream("/assets/iconConfig.png"));
            stage.getIcons().add(iconConfig);
            stage.show();
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Erro ao abrir janela para configurar busca");
        }
    }

    
    @FXML 
    void search(ActionEvent event) {

        try {

            String estrutura = ConfigSearch.getEstrutura();
            String orderBy = ConfigSearch.getOrderBy();
            String listOrdener;
            String estruturaEscolhida = "";
            RadioButton ascOrDesc = (RadioButton) orderList.getSelectedToggle();
            if(ascOrDesc.getText().equals("Crescente"))
                listOrdener = "asc";
            else
                listOrdener = "desc";
            
            //config padrão de busca.
            if(estrutura == null)
                estrutura = "heapsort";
            if(orderBy == null)
                orderBy = "orderId";
            

            switch (orderBy) {
                case "alertCode","orderId": 

                    if(orderBy.equals("alertCode")){
                        List<Integer> alertCode = mysql.getAllNumbers("alertCode");
                        Long tempoFinal = null;

                        switch (estrutura) {
                            case "timsort":
                                Tempo timer = new Tempo();
                                Estrutura.timSortNumber(alertCode);
                                tempoFinal = timer.getTime();

                                if(!listOrdener.equals("asc"))
                                    Collections.reverse(alertCode);

                                estruturaEscolhida = "Estrutura por TimSort";

                                break;

                            case "heapsort":
                                Tempo timer2 = new Tempo();
                                Estrutura.HeapSortNumber(alertCode);
                                tempoFinal = timer2.getTime();

                                if(!listOrdener.equals("asc"))
                                    Collections.reverse(alertCode);

                                estruturaEscolhida = "Estrutura por HeapSort";
 
                                break;
                            case "mergesort":
                                Tempo timer3 = new Tempo();
                                Estrutura.MergeSortNumber(alertCode);
                                tempoFinal = timer3.getTime();

                                if(!listOrdener.equals("asc"))
                                    Collections.reverse(alertCode);

                                estruturaEscolhida = "Estrutura por Merge Sort";
                                
                                break;

                        }

                        timerResults.setText("Tempo de ordenação: "+(tempoFinal/1000)+"s "+tempoFinal%1000+"ms - "+estruturaEscolhida);
                        addAlertCodeTable(alertCode);


                    }else{
                        List<Integer> id = mysql.getAllNumbers("id");
                        Long tempoFinal = null;


                        switch (estrutura) {
                            case "timsort":

                                Tempo timer = new Tempo();
                                Estrutura.timSortNumber(id);
                                tempoFinal = timer.getTime();

                                if(!listOrdener.equals("asc"))
                                    Collections.reverse(id);

                                estruturaEscolhida = "Estrutura por TimSort";

                                break;

                            case "heapsort":
                                Tempo timer2 = new Tempo();
                                Estrutura.HeapSortNumber(id);
                                tempoFinal = timer2.getTime();

                                if(!listOrdener.equals("asc"))
                                    Collections.reverse(id);

                                estruturaEscolhida = "Estrutura por HeapSort";

                                break;

                            case "mergesort":
                                Tempo timer3 = new Tempo();
                                Estrutura.MergeSortNumber(id);
                                tempoFinal = timer3.getTime();

                                if(!listOrdener.equals("asc"))
                                    Collections.reverse(id);

                                estruturaEscolhida = "Estrutura por MergeSort";
                                
                                break;

                        }

                        timerResults.setText("Tempo de ordenação: "+(tempoFinal/1000)+"s "+tempoFinal%1000+"ms - "+estruturaEscolhida);
                        addIdTable(id);
                    }

                    break; //fim alertCode, id;

                case "orderDetectado", "orderPublicado":
                    List<String> data = new ArrayList<>();
                    Long tempoFinal = null;
                    String type = "";

                    if(orderBy.equals("orderDetectado")){
                        data = mysql.selectAllDatas("detectado"); //detectado,postado
                        type = "detectado";

                    }else{
                        data = mysql.selectAllDatas("publicado");
                        type = "publicado";
                    }

                    
                    switch (estrutura) {
                        case "timsort":
                            Tempo timer = new Tempo();
                            Estrutura.timSortData(data);
                            tempoFinal = timer.getTime();
                            if(listOrdener.equals("desc"))
                                Collections.reverse(data);

                            estruturaEscolhida = "Estrutura por TimSort";

                            break;

                        case "heapsort":
                            Tempo timer2 = new Tempo();
                            Estrutura.HeapSortData(data);
                            tempoFinal = timer2.getTime();
                            if(listOrdener.equals("desc"))
                                Collections.reverse(data);

                            estruturaEscolhida = "Estrutura por HeapSort";

                            break;

                        case "mergesort":
                            Tempo timer3 = new Tempo();
                            Estrutura.MergeSortData(data);
                            tempoFinal = timer3.getTime();

                            if(!listOrdener.equals("asc"))
                                Collections.reverse(data);

                            estruturaEscolhida = "Estrutura por MergeSort";
                            
                            break;
                    }

                    
                    timerResults.setText("Tempo de ordenação: "+(tempoFinal/1000)+"s "+tempoFinal%1000+"ms - "+estruturaEscolhida);
                    addDataTable(data, type);

                    break; //fim detectado, postado.

               
            }

        } catch (Exception e) {
            System.out.println(e);
            Platform.runLater(()->{
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao ordernar os dados");
            });
        }

    }


    @FXML
    void listarDados(ActionEvent event) {
        buscarData();
    }


    @FXML
    void addAfterImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Imagens (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(filter);

        afterFileImage = fileChooser.showOpenDialog(btnBeforeImage.getScene().getWindow());

        if(afterFileImage != null){
            try {
                //Transformando em blob.
                InputStream inputStream = new FileInputStream(afterFileImage);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                afterImageBlob = outputStream.toByteArray();

                int tamanhoDoBlobBytes = afterImageBlob.length;
                double tamanhoDoBlobMB = tamanhoDoBlobBytes / (1024.0 * 1024.0);
                sizeAfter.setText(String.format("Tamanho da imagem: %.2f MB%n", tamanhoDoBlobMB));
                
                //Mostrando a imagem.
                Image image = new Image(afterFileImage.toURI().toString());

                ImageView newImageView = new ImageView(image);
                newImageView.setPreserveRatio(true);
                newImageView.setFitWidth(400);
                newImageView.setFitHeight(400);
    
                StackPane root = new StackPane(newImageView);
                Scene scene = new Scene(root, 500, 500);
                
                Stage newStage = new Stage();
                newStage.setTitle("Visualização da Imagem");
                newStage.setScene(scene);
                newStage.show();

                inputStream.close();
                outputStream.close();

            } catch (Exception e) {
                System.out.println("erro: "+e);

                Platform.runLater(()->{
                    
                    JOptionPane.showMessageDialog(null, "Não foi possível carregar a imagem.");
                });

            }

        }
    }
    

    @FXML
    void addBeforeImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Imagens (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(filter);

        beforeFileImage = fileChooser.showOpenDialog(btnBeforeImage.getScene().getWindow());

        if(beforeFileImage != null){
            try {
                //Transformando em blob.
                InputStream inputStream = new FileInputStream(beforeFileImage);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                beforeImageBlob = outputStream.toByteArray();

                int tamanhoDoBlobBytes = beforeImageBlob.length;
                double tamanhoDoBlobMB = tamanhoDoBlobBytes / (1024.0 * 1024.0);
                sizeBefore.setText(String.format("Tamanho da imagem: %.2f MB%n", tamanhoDoBlobMB));
                
                //Mostrando a imagem.
                Image image = new Image(beforeFileImage.toURI().toString());

                ImageView newImageView = new ImageView(image);
                newImageView.setPreserveRatio(true);
                newImageView.setFitWidth(400);
                newImageView.setFitHeight(400);
    
                StackPane root = new StackPane(newImageView);
                Scene scene = new Scene(root, 500, 500);
                
                Stage newStage = new Stage();
                newStage.setTitle("Visualização da Imagem");
                newStage.setScene(scene);
                newStage.show();

                inputStream.close();
                outputStream.close();


            } catch (Exception e) {
                System.out.println("erro: "+e);

                Platform.runLater(()->{
                    
                    JOptionPane.showMessageDialog(null, "Não foi possível carregar a imagem.");
                });

            }

        }

    }



    @FXML
    void cadastrarDado(ActionEvent event) {
        String dateDetected = txDetected.getText();

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(dateDetected, formatter);

            //verificarImagens.
            if(afterFileImage == null || beforeFileImage == null){
                Platform.runLater(()->{
                    JOptionPane.showMessageDialog(null, "É precisar adicionar todas as imagens antes de cadastrar.");
                });

                return;
            }

            if(LocalDate.parse(dateDetected).isAfter(LocalDate.parse(txPosted.getText()))){
                throw new Exception("invalid before date");
            }

            if((afterFileImage.length() / 1024) > 54 || (beforeFileImage.length() / 1024) > 54 ){
                throw new Exception("invalid size image");
            }

            cadastrarDado.setDisable(true);

            int atualId = Integer.parseInt(txID.getText());
            int alertCode = Integer.parseInt(txAlertCode.getText());
            String dateDetectado = txDetected.getText();
            String datePosted = txPosted.getText();
            String[] data = {dateDetectado, datePosted};

            InputStream imageBefore = new ByteArrayInputStream(beforeImageBlob); 
            InputStream imageAfter = new ByteArrayInputStream(afterImageBlob); 

            mysql.insertIntoWithId("images", atualId, alertCode, data, imageBefore, imageAfter);

            Platform.runLater(()->{
                cadastrarDado.setDisable(false);
                txDetected.setText("");
                sizeAfter.setText("");
                sizeBefore.setText("");
                afterFileImage = beforeFileImage = null;
                beforeImageBlob = afterImageBlob = null;
                buscarData();
                JOptionPane.showMessageDialog(null, "Cadastrado com sucesso!");
            });

   
        } catch (Exception e) {
            String err = ""+e;
            if(err.split(":")[0].equals("java.time.format.DateTimeParseException")){
                Platform.runLater(()->{
                    JOptionPane.showMessageDialog(null, "A data não está no formato correto!\n YYYY-MM-DD: 2012-02-20");
                });
            }else if(e.getMessage().equals("invalid before date")){
                Platform.runLater(()->{
                    JOptionPane.showMessageDialog(null, "A data em que foi detectado não pode ser maior que a de hoje");
                });
            }else if(e.getMessage().equals("invalid size image")){
                Platform.runLater(()->{
                    JOptionPane.showMessageDialog(null, "A Imagem escolhida ultrapassa limite maximo de 64 kb");
                });
            }else{
                Platform.runLater(()->{
                    System.out.println(e);
                    JOptionPane.showMessageDialog(null, "Algo deu errado");
                });
                
            }
            cadastrarDado.setDisable(false);
        }
        
    }


    @FXML
    void tabCadastro(Event event) {
        section++;
        if(section%2 != 0){
            txID.setText(""+(res.size()+1));
            txAlertCode.setText(""+gerarNumeroAleatorio());
            txPosted.setText(""+LocalDate.now());
        }
    }

    @FXML
    void excluirDado(ActionEvent event) {
        if(table.getSelectionModel().getSelectedIndex() == -1){
            Platform.runLater(()->{
                JOptionPane.showMessageDialog(null, "É preciso estar selecionado algum item!", "ERRO !", 0);
            });
            return;
        }
        
        int index = table.getSelectionModel().getSelectedIndex();
        int id = table.getItems().get(index).getId();

        mysql.deleteItem(id);

        Platform.runLater(()->{
            JOptionPane.showMessageDialog(null, "Item deletado com sucesso!");
        });

        buscarData();
  
    }

    private Object buscarImagem(int id) {
        List<byte[]> images = mysql.takeImage(id);
        
        if(images != null && images.size() == 2){

            Image beforeImage = new Image(new ByteArrayInputStream(images.get(0)));
            Image afterImage = new Image(new ByteArrayInputStream(images.get(1)));

            ImageView beforeView = new ImageView(beforeImage);
            beforeView.setFitWidth(400);
            beforeView.setPreserveRatio(true);

            ImageView afterView = new ImageView(afterImage);
            afterView.setFitWidth(400);
            afterView.setPreserveRatio(true);

            HBox imageBox = new HBox(20);
            imageBox.setAlignment(Pos.CENTER);
            imageBox.getChildren().addAll(beforeView, afterView);

            Stage stage = new Stage();
            stage.setTitle("Imagens Antes e Depois");
            Image icon = new Image(getClass().getResourceAsStream("/assets/icon.png"));
            stage.getIcons().add(icon);

            // Cria uma Scene e define-a no Stage
            Scene scene = new Scene(imageBox, 850, 400); // Ajuste o tamanho conforme necessário
            stage.setScene(scene);

            // Mostra a nova janela
            stage.show();

        }

        return null;
    }


    private void atualizarDados(){
        String caminhoRelativo = new File(".").getAbsolutePath();
      
        String imagePath = caminhoRelativo+"/imagens/antesmin.jpg";
        String imagePath2 = caminhoRelativo+"/imagens/deposmin.jpg";
        
        try {
            
            int i = 1;
            while (true) {
                
                File imageFile = new File(imagePath);
                File imageFile2 = new File(imagePath2);
                FileInputStream image = new FileInputStream(imageFile);
                FileInputStream image2 = new FileInputStream(imageFile2);



                
                int alertCode = gerarNumeroAleatorio();
                LocalDate aux;
                LocalDate dataAntiga = LocalDate.parse(generateRandomDate());
                LocalDate dataNova = LocalDate.parse(generateRandomDate());

                if (dataAntiga.isAfter(dataNova)) {
                    aux = dataAntiga;
                    dataAntiga = dataNova;
                    dataNova = aux;
                }

                mysql.insertInto("images",alertCode,""+dataAntiga,""+dataNova,image,image2);

                if(i >= 100000)
                    break;

                i++;
            }
                
        } catch (Exception e) {
            Platform.runLater(()->{
                System.out.println(e);
                JOptionPane.showMessageDialog(null, "Erro: "+e);
            });
        }
    }

    
    private void addAlertCodeTable(List<Integer> listCode){

        Map<Integer, String> resMap = new HashMap<>();
        for (String item : res) {
            int alertCodeId = Integer.parseInt(item.split("alertCode:")[1].split(" ")[0]);
            resMap.put(alertCodeId, item);
        }

        ObservableList<ImageData> data = FXCollections.observableArrayList();

        for(int alertCodeId: listCode){
            String additionalInfo = resMap.get(alertCodeId);
            if(additionalInfo != null){
                
                String[] parts = additionalInfo.split(" ");
                int id = Integer.parseInt(parts[0].split(":")[1]);
                int alertCode = alertCodeId;
                String detectado = parts[2].split(":")[1];
                String publicado = parts[3].split(":")[1];
                
                data.add(new ImageData(id, alertCode, detectado, publicado));
            }
        }

        table.setItems(data);

    }


    private void addDataTable(List<String> data, String type){
        Map<String, String> resMap = new HashMap<>();

        if(type == "publicado"){
            for (String item : res) {
                String date = item.split("Publicado:")[1].split(" ")[0];
                int id = Integer.parseInt(item.split(" ")[0].split(":")[1]);
                resMap.put(date+":"+id, item);
            }
        }else{
            for (String item : res) {
                String date = item.split("Detectado:")[1].split(" ")[0];
                int id = Integer.parseInt(item.split(" ")[0].split(":")[1]);
                resMap.put(date+":"+id, item);
            }
        }

        ObservableList<ImageData> tableItems = FXCollections.observableArrayList();


        for(String item: data){
            String additionalInfo = resMap.get(item);
            if(additionalInfo != null){
                String[] parts = additionalInfo.split(" ");
                int id = Integer.parseInt(parts[0].split(":")[1]);
                int alertCode = Integer.parseInt(parts[1].split(":")[1]);
                String detectado = parts[2].split(":")[1];
                String publicado = parts[3].split(":")[1];

                tableItems.add(new ImageData(id, alertCode, detectado, publicado));
            }
        }

        table.setItems(tableItems);

    }


    private void addIdTable(List<Integer> idList) {
        // Map para associar IDs e as informações completas.

        Map<Integer, String> resMap = new HashMap<>();
        for (String item : res) {
            int id = Integer.parseInt(item.split(" ")[0].split(":")[1]);
            resMap.put(id, item);
        }


        ObservableList<ImageData> data = FXCollections.observableArrayList();

        for (Integer id: idList) {
            String additionalInfo = resMap.get(id); 
            if (additionalInfo != null) {

                String[] parts = additionalInfo.split(" ");
                int alertCode = Integer.parseInt(parts[1].split(":")[1]);
                String detectado = parts[2].split(":")[1];
                String publicado = parts[3].split(":")[1];
    
                // Cria um objeto ImageData com os valores
                data.add(new ImageData(id, alertCode, detectado, publicado));
            }
        }
    
        table.setItems(data);
    }


    private void buscarData(){
        res = mysql.selectAll("images");

        ObservableList<ImageData> data = FXCollections.observableArrayList();

        for (String item : res) {
            String[] parts = item.split(" ");


            int id = Integer.parseInt(parts[0].split("id:")[1]); 
            Integer alertCode = Integer.parseInt(parts[1].split("alertCode:")[1]);
            String detectado = parts[2].split("Detectado:")[1];
            String publicado = parts[3].split("Publicado:")[1];

            data.add(new ImageData(id, alertCode, detectado, publicado));

        }

        int size = res.size();
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
        String formattedSize = numberFormat.format(size); //formatando em 3 casas ex 1.000 / 10.000


        title.setText("Resultados encontrados: "+formattedSize);

        // Defina os dados na TableView
        table.setItems(data);

        Platform.runLater(() -> {
            JOptionPane.showMessageDialog(null, "Dados listados na TableView");
        });

        addButtonToTable();

    }


    private void addButtonToTable() {
        Callback<TableColumn<ImageData, Void>, TableCell<ImageData, Void>> cellFactory = new Callback<>() {

            public TableCell<ImageData, Void> call(final TableColumn<ImageData, Void> param) {
                final TableCell<ImageData, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Abrir imagem");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            ImageData data = getTableView().getItems().get(getIndex());
                            buscarImagem(data.getId());
                        });
                    }

             
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        actionTable.setCellFactory(cellFactory);
    }

    public static int gerarNumeroAleatorio() {
        Random random = new Random();

        // Gera um número com até 7 dígitos
        int numeroAleatorio = random.nextInt(1_000_000);

        // Adiciona um valor adicional para garantir que seja de até 7 dígitos
        numeroAleatorio += 1_000_000; 

        return numeroAleatorio;
    }

    public static String generateRandomDate() {
        LocalDate startDate = LocalDate.of(2000, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 9, 30);

        long randomEpochDay = ThreadLocalRandom.current().nextLong(startDate.toEpochDay(), endDate.toEpochDay());

        LocalDate randomDate = LocalDate.ofEpochDay(randomEpochDay);

        // Formata a data no formato YYYY-MM-DD
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return randomDate.format(formatter);
    }

    @FXML
    void closeWindow(ActionEvent e){
        System.exit(0);
    }

}
