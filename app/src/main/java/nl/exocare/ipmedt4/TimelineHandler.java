package nl.exocare.ipmedt4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimelineHandler {
    Date dateBegin, dateEind, dateControle, dateRevalidatie;

    public TimelineHandler() throws ParseException {
        String beginDatum = "01 05 2017";
        String eindDatum = "19 08 2017";
        String controleDatum = "07 06 2017";
        String revalidatieDatum = "11 07 2017";

        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        this.dateBegin = myFormat.parse(beginDatum);
        this.dateEind = myFormat.parse(eindDatum);
        this.dateControle = myFormat.parse(controleDatum);
        this.dateRevalidatie = myFormat.parse(revalidatieDatum);
    }

    public Date getBeginDatum() {
        return dateBegin;
    }

    public Date getEindDatum() {
        return dateEind;
    }

    public Date getControleDatum() {
        return dateControle;
    }

    public Date getRevalidatieDatum() {
        return dateRevalidatie;
    }

    public long getTrajectduur(Date begin, Date eind) throws ParseException  {
        long diff = eind.getTime() - begin.getTime();

        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public long getVerstrekentijd() {
        Date currentTime = getCurrentTime();

        long verstrekentijd = currentTime.getTime() - dateBegin.getTime();
        return TimeUnit.DAYS.convert(verstrekentijd, TimeUnit.MILLISECONDS);
    }

    public Date getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }
}
