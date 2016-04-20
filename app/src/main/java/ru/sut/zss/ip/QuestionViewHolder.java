package ru.sut.zss.ip;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class QuestionViewHolder extends RecyclerView.ViewHolder {

    private CardView cardView;
    private TextView questionText;
    private TextView answerText;

    public QuestionViewHolder(View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.card_view);
        questionText = (TextView) itemView.findViewById(R.id.question_text);
        answerText = (TextView) itemView.findViewById(R.id.answer_text);
    }

    public void bind(Question question) {
        questionText.setText(question.getQuestion());
        answerText.setText(question.getAnswer());
    }
}
