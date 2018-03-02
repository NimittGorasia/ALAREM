package fragment;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gercep.alarem.AddAlarm;
import com.gercep.alarem.AlarmAdapter;
import com.gercep.alarem.Globals;
import com.gercep.alarem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dataClass.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment {
    private FirebaseAuth mAuth;

    ArrayList<TextView> mTextViews = new ArrayList<TextView>(); //empty list of TextViews
    private String mEmail;
    private  SharedPreferences userPref;
    private SharedPreferences.Editor userPrefEditor;
    private DatabaseReference mDatabase;
    private final Context context = getActivity();
    private View view;

    private RecyclerView mLayout;
    protected AlarmAdapter mAlarmAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<String> mWaktus;

    private FloatingActionButton mDelButton;

    public AlarmFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_alarm, container, false);
        FloatingActionButton addAlarmButton = view.findViewById(R.id.mAddAlarm);
        mDelButton = view.findViewById(R.id.mDelAlarm);

        userPref = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        userPrefEditor = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
        mEmail = userPref.getString("email", "12345");

        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAlarmIntent = new Intent(getContext(), AddAlarm.class);
                startActivityForResult(addAlarmIntent,1);
            }
        });

        mDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlaram();
                showDisplay();
            }
        });
        mLayout = (RecyclerView) view.findViewById(R.id.listOfAlarm);
        mLayoutManager = new LinearLayoutManager(this.getActivity());

        mWaktus = new ArrayList<>();

        showDisplay();
        return view;
    }

    public void showDisplay() {
        getAlarm();
        mAlarmAdapter = new AlarmAdapter(mWaktus);
        mLayout.setLayoutManager(mLayoutManager);
        mLayout.setAdapter(mAlarmAdapter);
        Log.d("HAHAHAAHA", new Integer(this.getId()).toString());
    }

    public void getAlarm() {
        Integer waktuSize = userPref.getInt("waktuSize", 12);
        mWaktus.clear();
        Log.d("DEBUG WAKTU SIZE", waktuSize.toString());
        for(Integer i = 0; i < waktuSize; i++) {
            String alarmTime = userPref.getString("waktu-" + i.toString(), "12345");
            Log.d("DEBUG ALARM TIME", alarmTime);
            mWaktus.add(alarmTime);
        }
    }

    public void deleteAlaram() {
        String email = userPref.getString("email", "12345");
        Float latitude = userPref.getFloat("latitude", new Float(0.01));
        Float langitude = userPref.getFloat("langitude", new Float(0.01));

        User newUser = new User(email, langitude, latitude);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(email.replace(".", "%")).setValue(newUser);

        userPrefEditor.putInt("waktuSize", 1);
        userPrefEditor.putString("waktu-0", "06:30");

        userPrefEditor.apply();

        Globals g = Globals.getInstance();
        g.deleteAlarm();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLayout = (RecyclerView) getView().findViewById(R.id.listOfAlarm);


        Log.d("DEBUG EMAIL", mEmail);

        Globals g = Globals.getInstance();

        Integer waktuSize = userPref.getInt("waktuSize", 12);
        for(Integer i = 0; i < waktuSize; i++) {
            String alarmTime = userPref.getString("waktu-" + i.toString(), "12345");
            Log.d("tambahin ke global", alarmTime); //alarmtime = aa:bb

            int isMenit = 0;
            for (String retval: alarmTime.split(":")) {
                if (isMenit == 0) {
                    g.addListJam(Integer.parseInt(retval));
                } else { // isMenit == 1
                    g.addListMenit(Integer.parseInt(retval));
                }
                isMenit++;
            }
//            mWaktus.add(alarmTime);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showDisplay();
    }
}
