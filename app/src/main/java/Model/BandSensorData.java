/*
 * Companion for Band
 * Copyright (C) 2016  Adithya J
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package Model;

import com.microsoft.band.InvalidBandVersionException;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandContactEvent;
import com.microsoft.band.sensors.BandContactState;
import com.microsoft.band.sensors.BandDistanceEvent;
import com.microsoft.band.sensors.BandGsrEvent;
import com.microsoft.band.sensors.BandGyroscopeEvent;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandRRIntervalEvent;
import com.microsoft.band.sensors.BandSkinTemperatureEvent;
import com.microsoft.band.sensors.HeartRateQuality;
import com.microsoft.band.sensors.MotionType;

public class BandSensorData {
    private BandAccelerometerEvent accelerometerData;
    private BandContactEvent contactData;
    private BandGsrEvent gsrData;
    private BandGyroscopeEvent gyroscopeData;
    private BandDistanceEvent distanceData;
    private BandHeartRateEvent heartRateData;
    private BandRRIntervalEvent rrIntervalData;
    private BandSkinTemperatureEvent skinTemperatureData;

    public BandSensorData() {
        accelerometerData = new BandAccelerometerEvent() {
            @Override
            public float getAccelerationX() {
                return 0;
            }

            @Override
            public float getAccelerationY() {
                return 0;
            }

            @Override
            public float getAccelerationZ() {
                return 0;
            }

            @Override
            public long getTimestamp() {
                return 0;
            }
        };
        contactData = new BandContactEvent() {
            @Override
            public BandContactState getContactState() {
                return null;
            }

            @Override
            public long getTimestamp() {
                return 0;
            }
        };
        distanceData = new BandDistanceEvent() {
            @Override
            public long getTotalDistance() {
                return 0;
            }

            @Override
            public long getDistanceToday() throws InvalidBandVersionException {
                return 0;
            }

            @Override
            public float getSpeed() {
                return 0;
            }

            @Override
            public float getPace() {
                return 0;
            }

            @Override
            public MotionType getMotionType() {
                return null;
            }

            @Override
            public long getTimestamp() {
                return 0;
            }
        };
        gsrData = new BandGsrEvent() {
            @Override
            public int getResistance() {
                return 0;
            }

            @Override
            public long getTimestamp() {
                return 0;
            }
        };
        gyroscopeData = new BandGyroscopeEvent() {
            @Override
            public float getAccelerationX() {
                return 0;
            }

            @Override
            public float getAccelerationY() {
                return 0;
            }

            @Override
            public float getAccelerationZ() {
                return 0;
            }

            @Override
            public float getAngularVelocityX() {
                return 0;
            }

            @Override
            public float getAngularVelocityY() {
                return 0;
            }

            @Override
            public float getAngularVelocityZ() {
                return 0;
            }

            @Override
            public long getTimestamp() {
                return 0;
            }
        };
        heartRateData = new BandHeartRateEvent() {
            @Override
            public int getHeartRate() {
                return 0;
            }

            @Override
            public HeartRateQuality getQuality() {
                return null;
            }

            @Override
            public long getTimestamp() {
                return 0;
            }
        };
        rrIntervalData = new BandRRIntervalEvent() {
            @Override
            public double getInterval() {
                return 0;
            }

            @Override
            public long getTimestamp() {
                return 0;
            }
        };
        skinTemperatureData = new BandSkinTemperatureEvent() {
            @Override
            public float getTemperature() {
                return 0;
            }

            @Override
            public long getTimestamp() {
                return 0;
            }
        };
    }

    public BandAccelerometerEvent getAccelerometerData() {
        return accelerometerData;
    }

    public void setAccelerometerData(BandAccelerometerEvent accelerometerData) {
        this.accelerometerData = accelerometerData;
    }

    public BandContactEvent getContactData() {
        return contactData;
    }

    public void setContactData(BandContactEvent contactData) {
        this.contactData = contactData;
    }

    public BandGsrEvent getGsrData() {
        return gsrData;
    }

    public void setGsrData(BandGsrEvent gsrData) {
        this.gsrData = gsrData;
    }

    public BandGyroscopeEvent getGyroscopeData() {
        return gyroscopeData;
    }

    public void setGyroscopeData(BandGyroscopeEvent gyroscopeData) {
        this.gyroscopeData = gyroscopeData;
    }

    public BandHeartRateEvent getHeartRateData() {
        return heartRateData;
    }

    public void setHeartRateData(BandHeartRateEvent heartRateData) {
        this.heartRateData = heartRateData;
    }

    public BandRRIntervalEvent getRrIntervalData() {
        return rrIntervalData;
    }

    public void setRrIntervalData(BandRRIntervalEvent rrIntervalData) {
        this.rrIntervalData = rrIntervalData;
    }

    public BandSkinTemperatureEvent getSkinTemperatureData() {
        return skinTemperatureData;
    }

    public void setSkinTemperatureData(BandSkinTemperatureEvent skinTemperatureData) {
        this.skinTemperatureData = skinTemperatureData;
    }

    public BandDistanceEvent getDistanceData() {
        return distanceData;
    }

    public void setDistanceData(BandDistanceEvent distanceData) {
        this.distanceData = distanceData;
    }
}
