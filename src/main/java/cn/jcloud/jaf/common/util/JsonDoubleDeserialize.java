package cn.jcloud.jaf.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by weihan on 2016/9/30.
 */
public class JsonDoubleDeserialize extends JsonDeserializer<Double> {

    @Override
    public Double deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Double value = jsonParser.getValueAsDouble();
        BigDecimal b = new BigDecimal(value);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
