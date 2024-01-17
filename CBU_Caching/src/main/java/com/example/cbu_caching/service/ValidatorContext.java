package com.example.cbu_caching.service;

import com.example.cbu_caching.annatation.Validator;
import com.example.cbu_caching.dto.BaseDto;
import com.example.cbu_caching.enums.ValidateEnum;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

@Component
public class ValidatorContext {

    private final Map<ValidateEnum, BiFunction<Object, Object, Boolean>> validators = new HashMap<>();

    @PostConstruct
    private void init() {
        validators.put(ValidateEnum.PATTERN, (val, val2) -> val.toString().matches(val2.toString()));
        validators.put(ValidateEnum.NOT_BLANK, (val, val2) -> val.toString().isBlank());
        validators.put(ValidateEnum.NOT_NULL, (val, val2) -> Objects.isNull(val));
//        validators.put(ValidateEnum.MAX, (val, val2) -> (Integer) val > (Integer) val2);
//        validators.put(ValidateEnum.MIN, (val, val2) -> (Integer) val < (Integer) val2);
    }


    public void validate(BaseDto dto) {
        Map<String, String> message = new HashMap<>();
        Class<? extends BaseDto> aClass = dto.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
            field.setAccessible(true);
            String field1 = getField(aClass, field);
            System.out.println(field1);
            Validator annotation = field.getAnnotation(Validator.class);
            for (ValidateEnum validateEnum : annotation.type()) {
                Boolean apply = validators.get(validateEnum).apply(field1, annotation.strValue());
                if (apply){
                    message.put(field.getName(),field1.concat(validateEnum.getVal()));
                }
            }
            System.out.println();
        }
        System.out.println(message);
    }

    private String getField(Class<? extends BaseDto> target, Field field){
        try {
            return field.get(target).toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
