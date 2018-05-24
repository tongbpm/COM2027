package org.com2027.group11.beerhere.utilities.database.TypeConverters;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by river on 05/03/18.
 */

public class DateTypeConverter {
    @TypeConverter
    public Date fromTimestamp(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }

}
