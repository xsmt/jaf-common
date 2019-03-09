package cn.jcloud.jaf.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Created by weihan on 2016/9/30.
 */
public class JsonDateDeserialize extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String value = jsonParser.getValueAsString();
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        try {
            return DateUtils.parseDate(value, Locale.CHINESE, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            return null;
        }
    }
}
