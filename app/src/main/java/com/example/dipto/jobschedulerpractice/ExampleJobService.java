package com.example.dipto.jobschedulerpractice;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ExampleJobService extends JobService implements OnResponseListener {

    private static final String TAG = "ExampleJobService" ;
    private Boolean jobCanclled = false ;
    private JobParameters parameters = null;
    private SharedPreferences sharedPreferences ;

    private void startBackgroundJob() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0 ; i < 10 ; i++){
                    if(jobCanclled){
                        return;
                    }
                    Log.d(TAG, "value of i :"+i) ;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                jobFinished(parameters, false);
                sharedPreferences.edit().putBoolean("JOBTASK", true).apply();
            }
        }).start();
    }


    @Override
    public void onResponseSuccess(String msg) {
        Log.d(TAG, "Job Finished") ;
        jobFinished(parameters, false);
    }

    @Override
    public void onRequestError(String msg) {
        Log.d(TAG, "Job Errored") ;
        jobFinished(parameters, false);
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d(TAG, "Job Start");
        sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE) ;
        this.parameters = job ;
        startBackgroundJob();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d(TAG, "Job Cancled Before Finished") ;
        jobCanclled = true ;
        return true;
    }
}
