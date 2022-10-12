package com.pastawater.vendingmachine.common.security.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.pastawater.vendingmachine.common.security.dto.SecCharSequence;

import java.io.CharArrayWriter;
import java.io.IOException;

public class SecCharSequenceDeserializer extends JsonDeserializer<SecCharSequence> {
    @Override
    public SecCharSequence deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        jsonParser.getText(charArrayWriter);

        return new SecCharSequence(charArrayWriter.toCharArray());
    }
}