package com.example.signalstrength;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MyCustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;

    MyCustomInfoWindowAdapter(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        myContentsView = inflater.inflate(
                R.layout.info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView tvTitle = ((TextView) myContentsView
                .findViewById(R.id.txtTitle));
        TextView tvSnippet = ((TextView) myContentsView
                .findViewById(R.id.txtSnippet));

        tvTitle.setText(marker.getTitle());
        tvSnippet.setText(marker.getSnippet());

        return myContentsView;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }
}