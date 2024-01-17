package com.example.version_1.service.context;


import com.example.version_1.enums.RemoteApiType;
import com.example.version_1.service.RemoteService;
import com.example.version_1.service.composite.CompositeRemoteRemoteService;
import com.example.version_1.service.remoteServices.ConcreteSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Service
public class InstanceContextService {

    @Autowired
    private ApplicationContext context;

    public RemoteService getService(String serviceName) {
        if (serviceName.equals("*"))
            return context.getBean(CompositeRemoteRemoteService.class);
        return (RemoteService) context.getBean(findBeanClass(serviceName));
    }

    public List<RemoteService> getServices() {
        return getServiceBeans().stream()
                .map(bean -> (RemoteService) bean)
                .toList();
    }
    public List<RemoteService> getConcreteServices() {
        return getConcreteServiceBeans().stream()
                .map(bean -> (RemoteService) bean)
                .toList();
    }

    private Class<?> findBeanClass(String serviceName) {
        for (Object serviceBean : getServices()) {
            Field[] declaredFields = serviceBean.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.getType().isEnum() && field.getName().equals("remoteType")) {
                    String fieldValue = getFieldValue(field, serviceBean);
                    if (RemoteApiType.checkRemoteApiType(fieldValue, serviceName)) {
                        return serviceBean.getClass();
                    }
                }
            }
        }
        throw new RuntimeException("Enum Not Found Exception!");
    }

    private List<Object> getServiceBeans() {
        return Arrays.stream(context.getBeanDefinitionNames())
                .map(bean -> context.getBean(bean))
                .filter(bean -> bean instanceof RemoteService)
                .toList();
    }
    private List<Object> getConcreteServiceBeans() {
        return Arrays.stream(context.getBeanDefinitionNames())
                .map(bean -> context.getBean(bean))
                .filter(bean -> bean instanceof ConcreteSubService)
                .toList();
    }

    private String getFieldValue(Field field, Object target) {
        try {
            return field.get(target).toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
