package Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.gson.Gson;


import java.util.Iterator;

import Model.RequestResponseTypes.DataPoint;
import Model.RequestType;
import Services.APIService;
import Services.CalibrationCallback;
import Utils.BandRecorder;
import Utils.DataStore;
import gabrieltechnologies.sehm.R;


public class CalibrateFragment extends Fragment {

    CalibrationCallback callback;
    BandRecorder bandRecorder;
    private final int recordPeriodMillis = 20000;
    private boolean buttonPressed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        // Inflate the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_calibrate, parent, false);

        //Hide keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        //initialise microsoft band
        bandRecorder = new BandRecorder(getActivity());
//        recordData(view);
        recordData(view);
        return view;
    }

    private void recordData(View view){
        TextView statusText = view.findViewById(R.id.statusTextView);

        //start recording band data
        bandRecorder.startRecording(statusText);
        finishCalibration();
    }

    /**
     * Calibration will finish if some of the datapoints are valid. If not, it will recurse
     */
    public void finishCalibration(){
        final Handler bandHandler = new Handler();


        bandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int correctDataPoints = 0;
                DataStore.pauseRecording = true;
                for(Iterator<DataPoint> iterator = DataStore.dataPoints.iterator(); iterator.hasNext();){

                    DataPoint datapoint = iterator.next();

                    if (datapoint.getQuality().equals("LOCKED") && datapoint.getContactStatus().equals("WORN"))
                    {
                        correctDataPoints++;
                    }
                }

                if (correctDataPoints > 0) {
                    //stop recording after 10minutes if data is valid
                    bandRecorder.stopRecording();
                    pushCalmData();
                    callback.calibrationDone();
                    DataStore.pauseRecording = false;
                }
                else {
                    finishCalibration();
                    DataStore.pauseRecording = false;
                }
            }
        }, recordPeriodMillis);
    }

    public void pushCalmData(){
        Gson gson = new Gson();
        String payload = gson.toJson(DataStore.dataPoints.toArray());
        APIService.getInstance().callAPI("datapoint/calm", getActivity(), RequestType.POST, payload);
    }

    public void onDestroyView() {
        super.onDestroyView();
        DataStore.dataPoints.clear();
        bandRecorder.onDestroy();
        bandRecorder = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public CalibrationCallback getCallback() {
        return callback;
    }

    public void setCallback(CalibrationCallback callback) {
        this.callback = callback;
    }
}
