package com.example.afinal.Fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.afinal.R;

public class BookingFragment extends Fragment {

    private static final String ARG_PACKAGE_NAME = "package_name";

    public static BookingFragment newInstance(String packageName) {
        BookingFragment fragment = new BookingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PACKAGE_NAME, packageName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        TextView packageNameTextView = view.findViewById(R.id.packageNameTextView);
        if (getArguments() != null) {
            String packageName = getArguments().getString(ARG_PACKAGE_NAME);
            packageNameTextView.setText(packageName);
        }

        return view;
    }
}
