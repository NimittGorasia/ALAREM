package fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.gercep.alarem.AddAlarm;
import com.gercep.alarem.AlarmAdapter;
import com.gercep.alarem.Globals;
import com.gercep.alarem.MainActivity;
import com.gercep.alarem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import dataClass.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Button resetButton;
    private SharedPreferences userPref;
    private SharedPreferences.Editor userPrefEditor;
    private String mEmail;

    private DatabaseReference mDatabase;
    public SettingFragment() {}
    private Integer id = 0;

    private Button getAutoLatitude;
    private Button getAutoLangitude;

    private Button setLatitudeButton;
    private Button setLangitudeButton;

    private TextView dataLatitude;
    private TextView dataLongitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        userPref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        userPrefEditor = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Button mSignOut = (Button) view.findViewById(R.id.logoutButton);

        getAutoLatitude = (Button) view.findViewById(R.id.getLatitudeButton);
        getAutoLangitude = (Button) view.findViewById(R.id.getLongitudeButton);

        setLatitudeButton = (Button) view.findViewById(R.id.setLatitudeButton);
        setLangitudeButton = (Button) view.findViewById(R.id.setLongitudeButton);

        dataLatitude = (TextView) view.findViewById(R.id.setLatitude);
        dataLongitude = (TextView) view.findViewById(R.id.setLongitude);

        mEmail = userPref.getString("email", "12345");

        final Globals g = Globals.getInstance();

        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                getActivity().finish();
            }
        });

        getAutoLatitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float latitude = new Float(g.getCurrentLatitude());
                g.setRingsLatitude(latitude);
                userPrefEditor.putFloat("latitude", latitude);
                userPrefEditor.apply();
                mDatabase.child("users").child(mEmail.replace(".", "%")).child("latitude").setValue(latitude);
            }
        });

        getAutoLangitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float longitude = new Float(g.getCurrentLongitude());
                g.setRingsLongitude(longitude);
                userPrefEditor.putFloat("langitude", longitude);
                userPrefEditor.apply();
                mDatabase.child("users").child(mEmail.replace(".", "%")).child("langitude").setValue(longitude);
            }
        });

        setLatitudeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float latitude = Float.valueOf(dataLatitude.getText().toString());
                g.setRingsLatitude(latitude);
                userPrefEditor.putFloat("latitude", latitude);
                userPrefEditor.apply();
                mDatabase.child("users").child(mEmail.replace(".", "%")).child("latitude").setValue(latitude);
            }
        });

        setLangitudeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float longitude = Float.valueOf(dataLongitude.getText().toString());
                g.setRingsLongitude(longitude);
                userPrefEditor.putFloat("langitude", longitude);
                userPrefEditor.apply();
                mDatabase.child("users").child(mEmail.replace(".", "%")).child("langitude").setValue(longitude);
            }
        });


        return view;
    }





    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
