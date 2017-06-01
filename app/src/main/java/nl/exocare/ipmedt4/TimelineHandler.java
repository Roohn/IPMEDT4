package nl.exocare.ipmedt4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimelineHandler {
    Date dateBegin, dateEind;

    public TimelineHandler() throws ParseException {
        String beginDatum = "01 05 2017";
        String eindDatum = "09 10 2017";

        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        this.dateBegin = myFormat.parse(beginDatum);
        this.dateEind = myFormat.parse(eindDatum);
    }

    public Date getBeginDatum() {
        return dateBegin;
    }

    public Date getEindDatum() {
        return dateEind;
    }

    public long getTrajectduur() throws ParseException  {
        long diff = dateEind.getTime() - dateBegin.getTime();

        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public long getVerstrekentijd() {
        Calendar cal = Calendar.getInstance();
        Date currentTime = cal.getTime();

        long verstrekentijd = currentTime.getTime() - dateBegin.getTime();
        return TimeUnit.DAYS.convert(verstrekentijd, TimeUnit.MILLISECONDS);
    }
}
