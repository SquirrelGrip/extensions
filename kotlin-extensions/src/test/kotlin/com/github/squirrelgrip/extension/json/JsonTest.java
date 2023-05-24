package com.github.squirrelgrip.extension.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonTest {
    @Test
    public void jsonUsesObjectMapperPassedIn() {
        ObjectMapper objectMapper1 = new ObjectMapper();
        ObjectMapper objectMapper2 = Json.INSTANCE.getObjectMapper();
        assertThat(objectMapper2).isNotSameAs(objectMapper1);
    }
}
