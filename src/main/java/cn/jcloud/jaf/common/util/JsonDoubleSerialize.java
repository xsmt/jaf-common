package cn.jcloud.jaf.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by weihan on 2016/9/30.
 */
public class JsonDoubleSerialize extends JsonSerializer<Double> {

    @Override
    public void serialize(Double value, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException,
            JsonProcessingException {
        jgen.writeString(DecimalUtil.smartString(value));
    }
}
