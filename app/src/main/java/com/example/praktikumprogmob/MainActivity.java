package com.example.praktikumprogmob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.praktikumprogmob.APIHelper.BaseApiService;
import com.example.praktikumprogmob.APIHelper.UtilsApi;
import com.example.praktikumprogmob.FragmentMenu.HomeFragment;
import com.example.praktikumprogmob.FragmentMenu.NotificationFragment;
import com.example.praktikumprogmob.FragmentMenu.ProfileFragment;
import com.example.praktikumprogmob.FragmentMenu.PromoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TESTAA", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = token;
                        Log.d("TESTAA", msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApiService = UtilsApi.getAPIService();

        loadFragment(new HomeFragment());
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        assert getSupportActionBar() != null;   //null check
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_dashboard:
                fragment = new PromoFragment();
                break;
            case R.id.navigation_notifications:
                fragment = new NotificationFragment();
                break;
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Close Application");
        builder.setMessage("Are you sure want to exit app?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something when user want to exit the app
                // Let allow the system to handle the event, such as exit the app
                MainActivity.super.onBackPressed();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        // Create the alert dialog using alert dialog builder
        AlertDialog dialog = builder.create();

        // Finally, display the dialog when user press back button
        dialog.show();
    }
}
