package com.guo.uitil;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

public class MyBeanUtils {

    public static String[] getNullPropertyName(Object source){
        BeanWrapper wrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = wrapper.getPropertyDescriptors();
        List<String> nullPropertyNames = new ArrayList<>();
        for (PropertyDescriptor pd : pds){
            String propertyName = pd.getName();
            if (wrapper.getPropertyValue(propertyName) == null)
                nullPropertyNames.add(propertyName);
        }
        return nullPropertyNames.toArray(new String[nullPropertyNames.size()]);
    }
}
