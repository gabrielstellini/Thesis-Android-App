package Services;

import android.os.AsyncTask;

/**
 * Created by gabri on 25/02/2018.
 */

public class DemoAsyncService extends AsyncTask<String,String ,String>  implements APIServiceCallback{

    @Override
    protected void onPreExecute() {
        System.out.println("Loading");
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    public void apiResponseListener(boolean isSuccess, String payload, String apiUrl) {

    }

    @Override
    public void loginStatus(int statusCode) {

    }
}
