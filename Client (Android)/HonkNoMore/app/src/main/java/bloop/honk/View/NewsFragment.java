package bloop.honk.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bloop.honk.Controller.NewsController;
import bloop.honk.Model.News;
import bloop.honk.R;


public class NewsFragment extends Fragment {
    private List<News> news = new ArrayList<>();

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private NewsController con;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        getActivity().setTitle("Road Announcements");

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new NewsAdapter(getActivity(), news);
        recyclerView.setAdapter(adapter);
        con = new NewsController(getActivity(), adapter);

        con.fetchNews(news);

        adapter.setClickListener(new NewsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String url = "http://" + adapter.getItem(position).getLink();
                con.launchWebView(NewsFragment.this, url);
            }
        });

        return view;
    }
}