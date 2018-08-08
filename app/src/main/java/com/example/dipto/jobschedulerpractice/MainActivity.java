package com.example.dipto.jobschedulerpractice;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class MainActivity extends AppCompatActivity {

    Button startJob ;
    SharedPreferences sharedPreferences ;
    String JOB_TAG= "MY_JOB" ;
    private FirebaseJobDispatcher jobDispatcher ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this)) ;
        startJob = findViewById(R.id.btn_start_job) ;

        startJob.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                startJobSchedule();
            }
        });
    }


    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startJobSchedule() {

        /*ComponentName componentName = new ComponentName(MainActivity.this, ExampleJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(100, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(jobInfo);

        if(resultCode == JobScheduler.RESULT_SUCCESS){
            Log.d("MainActivity", "Job Scheduled") ;
        }
        else {
            Log.d("MainActivity", "Job Scheduled Failed") ;
        }*/

        Job job = jobDispatcher.newJobBuilder()
                .setService(ExampleJobService.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag(JOB_TAG)
                .setTrigger(Trigger.executionWindow(10, 120))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();

        jobDispatcher.mustSchedule(job);

        sharedPreferences.edit().putBoolean("JOBTASK", false).apply();
    }
}
