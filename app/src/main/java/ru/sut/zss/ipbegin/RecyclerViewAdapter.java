package ru.sut.zss.ipbegin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<QuestionViewHolder> {

    private final List<Question> questionList;
    private final LayoutInflater mInflater;

    public RecyclerViewAdapter(Context context, List<Question> questionList) {
        mInflater = LayoutInflater.from(context);
        this.questionList = getCloneList(questionList);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        final View view = mInflater.inflate(R.layout.item, viewGroup, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder questionViewHolder, int position) {
        final Question question = questionList.get(position);

        questionViewHolder.bind(question);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public void animateTo(List<Question> questionList) {
        applyAndAnimateRemovals(questionList);
        applyAndAnimateAdditions(questionList);
        applyAndAnimateMovedItems(questionList);
    }

    private void applyAndAnimateRemovals(List<Question> newQuestion) {
        for (int i = this.questionList.size() - 1; i >= 0; i--) {
            final Question quetion = questionList.get(i);
            if (!newQuestion.contains(quetion)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Question> newQuestions) {
        for (int i = 0, count = newQuestions.size(); i < count; i++) {
            final Question quetion = newQuestions.get(i);
            if (!questionList.contains(quetion)) {
                addItem(i, quetion);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Question> newQuestions) {
        for (int toPosition = newQuestions.size() - 1; toPosition >= 0; toPosition--) {
            final Question quetion = newQuestions.get(toPosition);
            final int fromPosition = questionList.indexOf(quetion);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Question removeItem(int position) {
        final Question question = questionList.remove(position);
        notifyItemRemoved(position);
        return question;
    }

    public void addItem(int position, Question question) {
        questionList.add(position, question);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Question question = questionList.remove(fromPosition);
        questionList.add(toPosition, question);
        notifyItemMoved(fromPosition, toPosition);
    }

    private List<Question> getCloneList(List<Question> questionList) {
        List<Question> newQuestionList = new ArrayList<>(questionList.size());
        for (Question question : questionList) {
            newQuestionList.add(question);
        }
        return newQuestionList;
    }

}
