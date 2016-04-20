package ru.sut.zss.ipbegin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JSONParse {

    private JSONArray jsonArray;
    private List<Question> questionTestList = new ArrayList<>();
    private List<Question> questionModelList = new ArrayList<>();

    public JSONParse(InputStream inputStream) {

        jsonArray = openJSONFile(inputStream);
        parseJSON();

    }

    private void parseJSON() {

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Question question = new Question();
                JSONArray jsonArray = jsonObject.getJSONArray("answers");
                String answer = jsonArrayToString(jsonArray);
                question.setQuestion((String) jsonObject.get("question"));
                question.setAnswer(answer);

                if (jsonObject.getString("type").contains("test")) {
                    questionTestList.add(question);
                } else {
                    questionModelList.add(question);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public List<Question> getQuestionTestList() {
        return questionTestList;
    }

    public List<Question> getQuestionModelList() {
        return questionModelList;
    }

    private String jsonArrayToString(JSONArray jsonArray) throws JSONException {
        StringBuilder answer = new StringBuilder();

        for (int answer_count = 0; answer_count < jsonArray.length(); answer_count++) {
            answer.append(jsonArray.getString(answer_count) + '\n');
        }
        return answer.toString();
    }

    private JSONArray openJSONFile(InputStream inputStream) {
        byte[] buffer;
        String bufferString;

        try {
            int size = inputStream.available();
            buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            bufferString = new String(buffer);

            return new JSONArray(bufferString);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
