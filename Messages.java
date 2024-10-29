package com.devops.toolbox.cmftemplates;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

    private static final String BUNDLE_NAME = "languages/messages";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());

    private Messages(){

    }

    public static String getString(String key, Object... args){
        try {
            String message = RESOURCE_BUNDLE.getString(key);
            return MessageFormat.format(message, args);
        } catch (MissingResourceException e){
            return '!' + key + '!';
        }
    }

    public static String getString(String key){
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e){
            return '!'+ key + '!';
        }
    }
}
