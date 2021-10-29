package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.IndexFileStructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Controller {

    @FXML
    private TableView<String> blockFilesTable;

    @FXML
    private Button deleteButton;

    @FXML
    private Button findButton;

    @FXML
    private TableView<Node> indexFileTable;

    @FXML
    private Label findLabel;

    @FXML
    private TextField inputKey;

    @FXML
    private TextField inputValue;

    @FXML
    private Button insertButton;

    static Random random = new Random();

    @FXML
    void initialize() throws IOException {
        //findLabel.setText(" ");
        refreshTable();

        IndexFileStructure struc = new IndexFileStructure();

        insertButton.setOnAction(event -> {
            try {
                struc.insert(inputKey.getText(), inputValue.getText());
                refreshTable();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        deleteButton.setOnAction(event -> {
            try {
                struc.remove(inputKey.getText());
                refreshTable();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        findButton.setOnAction(onclick -> {
            try {
                String value = struc.find(inputKey.getText());
                //System.out.println(value);
                findLabel.setText(value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        for (int i=1; i<=1000; i++){
            struc.insert( i + "", random.nextInt(1000) + "");
        }

        for (int i = 0; i < 15; i ++)
        {
            int key = random.nextInt(999) + 1;
            System.out.println('\n' +"Key: " + key);
            String value = struc.find(String.valueOf(key));
            System.out.println("Value: " + value + '\n');
        }
    }

     public void refreshTable(){
        ArrayList<String> list = new ArrayList<>();
        try(Scanner reader = new Scanner(new File("IndexFile.txt"))) {
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                String[] values = line.split("-");
                list.add(values[0].trim());
                list.add(values[1].trim());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ObservableList<Node> data = FXCollections.observableArrayList();
        for (int i=0;i < list.size();i+=2){
            data.add(new Node(list.get(i), list.get(i + 1)));
        }

       indexFileTable = new TableView<Node>(data);

        // столбец для вывода имени
        TableColumn<Node, String> keyColumn = new TableColumn<Node, String>("Key");
        // определяем фабрику для столбца с привязкой к свойству name
        keyColumn.setCellValueFactory(new PropertyValueFactory<Node, String>("key"));
        // добавляем столбец


        // столбец для вывода возраста
        TableColumn<Node, Integer> valueColumn = new TableColumn<Node, Integer>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<Node, Integer>("value"));

        indexFileTable.setItems(data);
        indexFileTable.getColumns().addAll(keyColumn, valueColumn);
        //indexFileTable.setItems(ageColumn);
        //indexFileTable.refresh();
    }

    private class Node{
        String key;
        String value;

        Node(String key,String value){
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
