package com.iries.youtubealarm.UI.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.api.services.youtube.YouTube;
import com.iries.youtubealarm.R;
import com.iries.youtubealarm.UI.adapter.YTChannelsAdapter;
import com.iries.youtubealarm.databinding.YtSearchFragmentBinding;
import com.iries.youtubealarm.util.youtube.YoutubeAuth;
import com.iries.youtubealarm.util.youtube.YoutubeSearch;
import com.iries.youtubealarm.data.entity.youtube.YTChannel;

import com.iries.youtubealarm.data.SharedModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class YTSearchFragment extends Fragment {
    private YtSearchFragmentBinding binding;
    private ProgressBar progressBar;
    private Context context;
    private YTChannelsAdapter dataAdapter;
    private SharedModel sharedModel;
    private YouTube youTube;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedModel = new ViewModelProvider(requireActivity())
                .get(SharedModel.class);
        context = requireContext();
        youTube = YoutubeAuth.getYoutube(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = YtSearchFragmentBinding.inflate(inflater,
                container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        dataAdapter = new YTChannelsAdapterImpl(
                context, R.layout.yt_search_fragment);

        ListView listView = binding.ytChannelsView;
        listView.setAdapter(dataAdapter);

        binding.findAllSelectedChannels.setOnClickListener(v
                -> dataAdapter.updateListView(
                dataAdapter.getCheckedChannels()));

        binding.mySubsButton.setOnClickListener(e ->
                callApiAndUpdateUI(() ->
                        YoutubeSearch.getSubscriptions(youTube)));

        progressBar = binding.loadingDataProgress;
    }

    private void callApiAndUpdateUI(Supplier<ArrayList<YTChannel>> getChannels) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            ArrayList<YTChannel> channels = getChannels.get();
            handler.post(() -> {
                progressBar.setVisibility(View.INVISIBLE);
                progressBar.setIndeterminate(false);
                dataAdapter.updateListView(channels);
            });
        });
        executor.shutdown();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callApiAndUpdateUI(() ->
                        YoutubeSearch.findChannelByKeyword(youTube, query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private class YTChannelsAdapterImpl extends YTChannelsAdapter {

        public YTChannelsAdapterImpl(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            sharedModel.getAllChannels()
                    .observe(getViewLifecycleOwner(), this::setCheckedChannels);
        }

        @Override
        public void updateChannelsList(YTChannel channel, boolean isChecked) {
            if (isChecked) sharedModel.remove(channel);
             else sharedModel.insert(channel);
        }
    }
}
