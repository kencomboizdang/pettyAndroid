package util;

import java.sql.Timestamp;

public class DateTimeStamp {
    public long getCurrentTime(){
        return (System.currentTimeMillis());
    }
    public String convertToDateTimeString(long time){
        Timestamp tsTemp = new Timestamp(time);
        return tsTemp.toString();
    }
}
