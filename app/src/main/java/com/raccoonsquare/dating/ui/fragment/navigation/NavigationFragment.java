package com.thinkin_service.provider.ui.fragment.navigation;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.thinkin_service.provider.R;
import com.thinkin_service.provider.ui.activity.main.MainActivity;
import javax.annotation.Nullable;

public class NavigationFragment extends DialogFragment {

    private MainActivity mainAct;

    public NavigationFragment() {
    }

    public static NavigationFragment newInstance(String title) {
        NavigationFragment fragment = new NavigationFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigation, container,false);

        ImageButton bt_waze = (ImageButton) view.findViewById(R.id.button_waze);
        ImageButton bt_maps = (ImageButton) view.findViewById(R.id.button_maps);

        bt_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lat = getArguments().getString("lat");
                String longi = getArguments().getString("long");

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat +","+ longi);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        bt_waze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lat = getArguments().getString("lat");
                String longi = getArguments().getString("long");

                try
                {
                    String url = "https://waze.com/ul?ll="+lat+","+ longi +"&navigate=yes";
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                    startActivity( intent );
                }
                catch ( ActivityNotFoundException ex  )
                {
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = getArguments().getString("title", "Selecione o App");
        getDialog().setTitle(title);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

}
