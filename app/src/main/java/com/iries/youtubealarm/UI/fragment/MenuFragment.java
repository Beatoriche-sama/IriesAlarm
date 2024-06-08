package com.iries.youtubealarm.UI.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.iries.youtubealarm.R;
import com.iries.youtubealarm.data.SharedModel;
import com.iries.youtubealarm.data.database.ChannelsRepo;
import com.iries.youtubealarm.databinding.MenuFragmentBinding;
import com.iries.youtubealarm.util.youtube.YoutubeAuth;

public class MenuFragment extends Fragment {
    private MenuFragmentBinding binding;
    private NavController navController;
    private Context context;
    private Intent signInIntent;
    private ActivityResultLauncher<Intent> signInLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        context = getContext();

        signInIntent = YoutubeAuth.getSignInClient(context).getSignInIntent();
        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> redirectToYTSearch());

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = MenuFragmentBinding.inflate(inflater,
                container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(MenuFragment.this);

        Button alarmButton = binding.alarmButton;
        alarmButton.setOnClickListener(v
                -> navController.navigate(R.id.action_MenuFragment_to_AlarmFragment));

       /* new ViewModelProvider(requireActivity())
                .get(SharedModel.class).getAllChannels()
                .observe(getViewLifecycleOwner(), e -> {
                    if (!e.isEmpty()) return;
                    alarmButton.setEnabled(false);
                    showWarningDialog();
                });*/

        int size = new ChannelsRepo(context).getChannelsCount();
        if (size == 0) {
            alarmButton.setEnabled(false);
            showWarningDialog();
        }

        Button ytButton = binding.ytSearchButton;
        ytButton.setOnClickListener(e -> {
            GoogleSignInAccount signedInAccount
                    = GoogleSignIn.getLastSignedInAccount(context);
            if (signedInAccount == null)
                signInLauncher.launch(signInIntent);
            else redirectToYTSearch();
        });

    }

    private void showWarningDialog() {
        AlertDialog.Builder dialog
                = new AlertDialog.Builder(getContext())
                .setMessage("Please, choose YouTube channels first.")
                .setPositiveButton("Okay.", null);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void redirectToYTSearch() {
        navController.navigate(R.id.action_MenuFragment_to_YTSearchFragment);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}