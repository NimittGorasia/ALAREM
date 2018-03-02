package fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gercep.alarem.AddAlarm;
import com.gercep.alarem.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    public AboutFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        Button mVisitButton = view.findViewById(R.id.visitButton);

        mVisitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "http://gitlab.informatika.org/IF3111-2018-GERCEP/android";
                Intent visitSiteIntent = new Intent(Intent.ACTION_VIEW);
                visitSiteIntent.setData(Uri.parse(data));
                startActivity(visitSiteIntent);
            }
        });

       return view;
    }

}
