package ru.sut.zss.ip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class JSONParse {

    private JSONArray jsonArray;
    private List<Question> questionList = new ArrayList<>();
    private Map<String, String> menu_answers;

    public JSONParse(InputStream inputStream) {

        jsonArray = openJSONFile(inputStream);
        parseJSON();

    }

    private void parseJSON() {

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject.getString("type").contains("quest")) {
                    testingParse(jsonObject.getString("question"),
                                 jsonObject.getString("answers"),
                                 jsonObject.getString("correct_answer"));
                } else if (jsonObject.getString("type").contains("lab")) {
                    modelingParse(jsonObject.getString("question"),
                                  jsonObject.getString("answers"),
                                  jsonObject.getString("correct_answer"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void testingParse(String question_text, String answers, String correct_answer) {

        int correct_answer_number = 0;

        String[] answer = answers.split("\\|");

        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(correct_answer);

        while (matcher.find()) {
            correct_answer_number = Integer.parseInt(matcher.group());
        }

        Question question = new Question();
        question.setQuestion(question_text.trim());
        question.setAnswer(answer[correct_answer_number].trim());

    }

    private void modelingParse(String question_text, String answers, String correct_answer) {

        Question question = new Question();
        question.setQuestion(question_text);

        XMLParse(answers);
        correctAnswerParse(correct_answer);

    }

    private void correctAnswerParse(String correct_answer) {

        if (correct_answer.contains(")|(")) {
            String[] correct_answer_list_split = correct_answer.split("\\)\\|\\(");
            for (String split_answer : correct_answer_list_split) {
                parseCorrectAnswer(split_answer);
            }
        } else {
            parseCorrectAnswer(correct_answer);
        }

    }

    private List<String> parseCorrectAnswer(String correct_answer) {

        List<String> answers = new ArrayList<>();

        String[] correct_answer_split = correct_answer.split("\\|");
        for (String correct : correct_answer_split) {
            if (correct.contains("\"")) {
                answers.add(correct);
            } else {
                Pattern pattern = Pattern.compile("(\\w+)");
                Matcher matcher = pattern.matcher(correct);

                while (matcher.find()) {
                    answers.add();
                }
            }
        }
        return answers;
    }

    private void XMLParse(String xml) {

        menu_answers = new HashMap<>();

        Document doc = loadXMLFromString(xml);
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getDocumentElement().getChildNodes();

        readXML(nodeList);
    }

    private void readXML(NodeList nodeList) {

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);

            Element element = (Element) node;

            System.out.println(element.getAttribute("txt"));

            NodeList nodeListChild = node.getChildNodes();

            if (nodeList != null) {
                readXML(nodeListChild);
            }

        }

    }

    private Document loadXMLFromString(String xml) {
        String xml_final = "<MENU>\n" + xml + "</MENU>";
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xml_final));
            return dBuilder.parse(inputSource);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONArray openJSONFile(InputStream inputStream) {
        byte[] buffer;
        String bufferString = null;

        try {
            int size = inputStream.available();
            buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            bufferString = new String(buffer);

            JSONArray array = new JSONArray(bufferString);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Question> questionList() {
        return questionList;
    }

}
