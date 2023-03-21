package org.example;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileNameCsv = "data.csv";
        String fileNameXml = "data.xml";
        String fileNameJson1 = "data1.json";
        String fileNameJson2 = "data2.json";

        // задача №1
        List<Employee> list1 = parseCSV(columnMapping, fileNameCsv);
        String json1 = listToJson(list1);
        writeString(json1, fileNameJson1);

        // задача №2
        List<Employee> list2 = parseXML(fileNameXml);
        String json2 = listToJson(list2);
        writeString(json2, fileNameJson2);

    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> scv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            List<Employee> list = scv.parse();
            return list;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list, listType);
        return json;

    }

    public static void writeString(String json, String nameFile) {

        try (FileWriter file = new
                FileWriter(nameFile)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String nameFile) {
        List<Employee> list = new ArrayList<>();
        long id = 0;
        String firstName = null;
        String lastName = null;
        String country = null;
        int age = 0;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = null;

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new File(nameFile));

        } catch (Exception e) {
            e.printStackTrace();
        }

        Node root = doc.getDocumentElement();
        NodeList rootChilds = root.getChildNodes();

        for (int i = 0; i < rootChilds.getLength(); i++) {

            if (rootChilds.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            NodeList element = rootChilds.item(i).getChildNodes();

            for (int j = 0; j < element.getLength(); j++) {
                if (element.item(j).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                switch (element.item(j).getNodeName()) {
                    case "id" -> {
                        id = Integer.parseInt(element.item(j).getTextContent());
                    }
                    case "firstName" -> {
                        firstName = element.item(j).getTextContent();
                    }
                    case "lastName" -> {
                        lastName = element.item(j).getTextContent();
                    }
                    case "country" -> {
                        country = element.item(j).getTextContent();
                    }
                    case "age" -> {
                        age = Integer.parseInt(element.item(j).getTextContent());
                    }
                }

            }
            Employee employee = new Employee(id, firstName, lastName, country, age);
            list.add(employee);
        }
        return list;
    }

}