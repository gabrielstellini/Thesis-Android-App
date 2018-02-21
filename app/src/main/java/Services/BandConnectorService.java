package Services;

//Band imports
import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandIOException;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.HeartRateConsentListener;

//Async import
import android.os.AsyncTask;

public class BandConnectorService extends AsyncTask<Void, Void, Void>{
    private BandClient client = null;

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }
}
