package util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeStamp {
    public long getCurrentTime(){
        return (System.currentTimeMillis());
    }
    public String convertToDateTimeString(long time){
        Timestamp tsTemp = new Timestamp(time);
        return tsTemp.toString();
    }

    public long convertDateToLong(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(dateTime);
        long miliseconds = date.getTime();
        return miliseconds;
    }
}
