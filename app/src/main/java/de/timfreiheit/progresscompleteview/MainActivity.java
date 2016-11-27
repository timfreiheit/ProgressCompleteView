package de.timfreiheit.progresscompleteview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import de.timfreiheit.progress.ProgressCompleteView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ProgressCompleteView progressCompleteView = (ProgressCompleteView) findViewById(R.id.progressComplete);

        findViewById(R.id.stateLoadingBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressCompleteView.setStatus(ProgressCompleteView.Status.LOADING);
            }
        });

        findViewById(R.id.stateSuccessBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressCompleteView.setStatus(ProgressCompleteView.Status.SUCCESS);
            }
        });

        findViewById(R.id.stateErrorBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressCompleteView.setStatus(ProgressCompleteView.Status.ERROR);
            }
        });

    }
}
