package com.example.administrator.beta;

public class Usr_Data {
    private float Temperature;
    private float Humidity;
    private float Co2;
    private float PM25;
    private float Time;
    private float Total;
    public Usr_Data(float T,float H,float C,float P,float Time,float Total){
        this.Temperature = T;
        this.Humidity = H;
        this.Co2 = C;
        this.PM25 = P;
        this.Time = Time;
        this.Total = Total;
    }

    public float getTemperature() {
        return Temperature;
    }

    public float getHumidity() {
        return Humidity;
    }

    public float getCo2() {
        return Co2;
    }

    public float getPM25() {
        return PM25;
    }

    public float getTime() {
        return Time;
    }

    public float getTotal() {
        return Total;
    }
}
