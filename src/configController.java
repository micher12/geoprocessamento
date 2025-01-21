import components.ConfigSearch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class configController {


    @FXML
    private Label atualConfig;

    @FXML
    private ToggleGroup typSearch;

    private String estrutura;

    public void initialize(){

        typSearch.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedRadioButton = (RadioButton) newValue;
                String textValue = selectedRadioButton.getText(); 
                if(textValue.equals("TimSort")){
                    estrutura = "timsort";
                    new ConfigSearch(estrutura, null);
                }
                if(textValue.equals("HeapSort")){
                    estrutura = "heapsort";
                    new ConfigSearch(estrutura, null);
                }
                if(textValue.equals("MergeSort")){
                    estrutura = "mergesort";
                    new ConfigSearch(estrutura, null);
                }
                
            }
        });
    }


    @FXML
    void orderByAlertCode(ActionEvent event) {
        atualConfig.setText("Ordernar pelo: \nCódigo de alerta");
        new ConfigSearch(null, "alertCode");
    }

    @FXML
    void orderByDetectado(ActionEvent event) {
        atualConfig.setText("Ordernar pela: \nData detectada");
        new ConfigSearch(null, "orderDetectado");
    }

    @FXML
    void orderById(ActionEvent event) {
        atualConfig.setText("Ordernar pelo: ID");
        new ConfigSearch(null, "orderId");
    }

    @FXML
    void orderByPostado(ActionEvent event) {
        atualConfig.setText("Ordernar pela: \nData publicada");
        new ConfigSearch(null, "orderPublicado");
    }


}
