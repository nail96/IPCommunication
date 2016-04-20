package ru.sut.zss.ipbegin;

import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements SearchView.OnQueryTextListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private RecyclerView recyclerView;
        private RecyclerViewAdapter recyclerViewAdapter;
        private List<Question> questionList;

        public PlaceholderFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, List<Question> questions) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();

            args.putParcelableArrayList("questions", (ArrayList<? extends Parcelable>) questions);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
            questionList = getArguments().getParcelableArrayList("questions");

            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            setHasOptionsMenu(true);

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewAdapter = new RecyclerViewAdapter(getContext(), questionList);
            recyclerView.setAdapter(recyclerViewAdapter);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);

            inflater.inflate(R.menu.menu_main, menu);

            final MenuItem item = menu.findItem(R.id.action_search);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setOnQueryTextListener(this);
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            final List<Question> filteredModelList = filter(questionList, newText);
            recyclerViewAdapter.animateTo(filteredModelList);
            recyclerView.scrollToPosition(0);
            return true;
        }

        private List<Question> filter(List<Question> questions, String query) {
            query = query.toLowerCase();

            final List<Question> filteredModelList = new ArrayList<>();
            for (Question question : questions) {
                final String text = question.getQuestion().toLowerCase();
                if (text.contains(query)) {
                    filteredModelList.add(question);
                }
            }
            return filteredModelList;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private JSONParse jsonParse;
        private List<Question> questionTestList;
        private List<Question> questionModelList;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            try {
                InputStream inputStream = getAssets().open("tasks.json");
                jsonParse = new JSONParse(inputStream);
                questionTestList = jsonParse.getQuestionTestList();
                questionModelList = jsonParse.getQuestionModelList();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return PlaceholderFragment.newInstance(position, questionTestList);
            } else {
                return PlaceholderFragment.newInstance(position, questionModelList);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Тестирование";
                case 1:
                    return "Моделирование";
            }
            return null;
        }
    }
}
