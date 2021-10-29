package sample;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class IndexFileStructure{
    //файлы блоков и индексный файл
    private static final String INDEX_FILE_NAME = "IndexFile";
    private static final String BLOCK_NAME = "Block";

    //количество строк и блоков
    public static int blocksAmount;
    public static int linesInBlock;


    public IndexFileStructure() throws IOException {
        File indexFile = new File(INDEX_FILE_NAME + ".txt");
        if(!indexFile.exists()) {
            indexFile.createNewFile();
            this.linesInBlock = 100;
            this.blocksAmount = 10;
            indexListToFile(new ArrayList<>());

            for (int i = 1; i <= blocksAmount; i++) {
                File dataFile = new File(BLOCK_NAME + i + ".txt");
                if(!dataFile.exists()) {
                    dataFile.createNewFile();
                }
            }
        }
        else {
            try(Scanner reader = new Scanner(indexFile)) {
                String line = reader.nextLine();
                String[] values = line.split("-");
                linesInBlock = Integer.parseInt(values[0].trim());
                blocksAmount = Integer.parseInt(values[1].trim());
            }
        }
    }

    public static void insert(String key, String value) throws IOException {
        int blockNum = key.hashCode() % blocksAmount + 1;
        Integer intkey = Integer.parseInt(key);

        ArrayList<String> indexList = indexFileToList();
        //System.out.println(indexList);
        //Iterator<String> indexListIterator= indexList.iterator();
        int i = 0;

        while(i < indexList.size()) {    //Проверка наличия ключа, в случае нахождения, замена существующего значения
            Integer _key = Integer.parseInt(indexList.get(i));
            i++;
            if(_key.equals(intkey)) {
                int _blockNum = Integer.parseInt(indexList.get(i));
                i++;
                System.out.println(intkey + " " + _key);
                System.out.println("index exists");
                ArrayList<String> dataList = dataFileToList(_blockNum);
                ArrayList<String> dataArr = new ArrayList<>(dataList);
                int index = binarySearch(dataArr, key);
                dataList.set(index + 1, value);
                dataListToFile(dataList, _blockNum);
                return;
            }
            i++;
        }
        indexList.add(key);
        indexList.add(blockNum + "");
        indexListToFile(indexList);

       ArrayList<String> dataList = dataFileToList(blockNum);
        //System.out.println(dataList);
        int blockLinesCount = 0;
        int LinesAmount = dataList.size()/2 + 1;
        i = 0;
        //Iterator<String> dataListIterator = dataList.iterator();
        boolean added = false;
        //int intkey = Integer.parseInt(key);
        while (i < dataList.size()) { //Нахождение нужного места для вставки ключа-значения
            int _key = Integer.parseInt(dataList.get(i));
            System.out.println("compare: " + _key + " " + key);
            i++;
            if( _key > intkey) {
                dataList.add(blockLinesCount * 2, key);
                dataList.add(blockLinesCount * 2 + 1, value);
                //dataList.add(blockLinesCount * 2 , value);
                added = true;
                break;
            }
            i++;
            blockLinesCount++;
        }
        System.out.println();
        if(!added) {
            dataList.add(key);
            dataList.add(value);
        }
        System.out.println(dataList);
        //dataList.add(key);
        //dataList.add(value);
        dataListToFile(dataList, blockNum);
        //System.out.println(dataList);
        if(LinesAmount >= linesInBlock) {
            generateNewIndexes();
        }
    }


    public void remove(String key) throws  IOException {
        ArrayList<String> indexList = indexFileToList();
        //Iterator<String> iterator = indexList.iterator();
        int i = 0;
        int ind = 0;


        while (ind < indexList.size()) {
            String _key = indexList.get(ind);
            ind++;
            if (_key.equals(key)) {
                System.out.println(key + " " + _key);
                System.out.println("index exists");
                int _blockNum = Integer.parseInt(indexList.get(ind));
                ArrayList<String> dataList = dataFileToList(_blockNum);
                ArrayList<String> dataArr = new ArrayList<>(dataList);


                int index = binarySearch(dataArr, key);
                dataList.remove(index);
                dataList.remove(index);
                dataListToFile(dataList, _blockNum);
                indexList.remove(ind - 1);
                indexList.remove(ind - 1);
                indexListToFile(indexList);
                return;
            }
            ind++;
            //iterator.next();
        }
    }


    public String find(String key) throws IOException {
        ArrayList<String> indexList = indexFileToList();
        int i = 0;
        //Iterator<String> iterator = indexList.iterator();
        while(i < indexList.size()) {
            String _key = indexList.get(i);
            i++;
            if(_key.equals(key)) {
                int _blockNum = Integer.parseInt(indexList.get(i));
                i++;
                ArrayList<String> dataList = dataFileToList(_blockNum);
                //System.out.println(dataList);
                int index = binarySearch(dataList, key);
                return dataList.get(index + 1);
            }
            i++;
        }
        return "not found";
    }

    public static int binarySearch(ArrayList<String> arr, String keyString) throws IOException {
        int key = Integer.parseInt(keyString);
        int iter = 0;
        int n = arr.size();
        int d = n / 2;
        int i = n / 2 + 1;
        while (d != 0) {
            iter++;
            int cmp;
            if (i % 2 == 1)
                i++;
            if (i >= arr.size())
                cmp = 1;
            else if (i < 0)
                cmp = -1;
            else{
                int item = Integer.parseInt(arr.get(i));
                if (item < key)
                    cmp = -1;
                else if (item > key)
                        cmp = 1;
                else
                    cmp = 0;
            }

            if (cmp == 0) {
                //FileWriter writer = new FileWriter("research.txt",true);
                //writer.write("Iter: " + iter + '\n');
                System.out.println("Iter: " + iter);
                return i;
            } else if (cmp < 0)
                i += (d / 2 + 1);
            else
                i -= (d / 2 + 1);
            d/=2;
        }
        return -1;
    }


    private static void generateNewIndexes() throws IOException {
        ArrayList<String> newIndexList = new ArrayList<>();
        for (int i = blocksAmount + 1; i <= blocksAmount * 2 ; i++) {
            File dataFile = new File(BLOCK_NAME + i  + ".txt");
            if(!dataFile.exists()) {
                dataFile.createNewFile();
            }

            ArrayList<String> sourceList = dataFileToList(i - blocksAmount);
            ArrayList<String> destList = new ArrayList<>();
            int count = sourceList.size()/4;
            for (int j = 0; j < count; j++) {// Добавление второй половины элементов в новый блок и новый индексный файл
                String tempData = sourceList.remove(sourceList.size()-1);
                String tempKey = sourceList.remove(sourceList.size()-1);
                destList.add(0,tempData);
                destList.add(0,tempKey);
                newIndexList.add(tempKey);
                newIndexList.add(i + "");
            }
            dataListToFile(destList, i);

            Iterator<String> iterator = sourceList.iterator();
            while(iterator.hasNext()) { // добавление первой половины элементов в новый индексный файл
                newIndexList.add(iterator.next());
                newIndexList.add((i - blocksAmount) + "");
                iterator.next();
            }
            dataListToFile(sourceList, (i - blocksAmount));
        }
        indexListToFile(newIndexList);
        blocksAmount *= 2;
        linesInBlock *= 2;
    }


    private static ArrayList<String> indexFileToList() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        try(Scanner reader = new Scanner(new File(INDEX_FILE_NAME + ".txt"))) {
            if (reader.hasNextLine()) {
                String line = reader.nextLine();
            }
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                String[] values = line.split("-");
                list.add(values[0].trim());
                list.add(values[1].trim());
            }
        }
        return list;
    }


    private static ArrayList<String> dataFileToList(int blockNum) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        try(Scanner reader = new Scanner(new File(BLOCK_NAME + blockNum + ".txt"))) {
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                String[] values = line.split("-");
                list.add(values[0].trim());
                list.add(values[1].trim());
            }
        }
        return list;
    }

   /* private static TreeSet<String> dataFileToSet(int blockNum) throws IOException {
        TreeSet<String> list = new TreeSet<>();
        try(Scanner reader = new Scanner(new File(BLOCK_NAME + blockNum + ".txt"))) {
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                String[] values = line.split("-");
                list.add(values[0].trim());
                list.add(values[1].trim());
            }
        }
        return list;
    }*/

    private static void indexListToFile(ArrayList<String> list) throws IOException {
        FileWriter writer1 = new FileWriter(INDEX_FILE_NAME + ".txt");
        writer1.write("");
        writer1.close();

        try(FileWriter writer = new FileWriter(INDEX_FILE_NAME + ".txt", true)) {

            String lines = linesInBlock + "";
            String amountBlocks = blocksAmount  + "";
            writer.write(lines + "-" + amountBlocks + '\n');

            for (int i = 0; i < list.size(); i += 2) {
                String key = list.get(i);
                String blockNum = list.get(i + 1);
                writer.write(key + "-" + blockNum  + '\n');
            }
            writer.flush();
        }
    }

    private static void dataListToFile(ArrayList<String> list, int blockNum) throws IOException {
        FileWriter writer1 = new FileWriter(BLOCK_NAME + blockNum + ".txt");
        writer1.write("");
        writer1.close();

        try(FileWriter writer = new FileWriter(BLOCK_NAME + blockNum + ".txt",true)) {
            for (int i = 0; i < list.size(); i+=2) {
                String key = list.get(i);
                String data = list.get(i + 1);
                writer.write(key + "-" + data + '\n');
            }
            writer.flush();
        }
    }

   /* private static void dataSetToFile(TreeSet<String> list, int blockNum) throws IOException {
        FileWriter writer1 = new FileWriter(BLOCK_NAME + blockNum + ".txt");
        writer1.write("");
        writer1.close();

        Iterator iter = list.iterator();

        try(FileWriter writer = new FileWriter(BLOCK_NAME + blockNum + ".txt",true)) {
            while(iter.hasNext()) {
                String key = (String) iter.next();
                if (iter.hasNext()) {
                    String data = (String) iter.next();
                    writer.write(key + "-" + data + '\n');
                }
            }
            writer.flush();
        }
    }*/
}