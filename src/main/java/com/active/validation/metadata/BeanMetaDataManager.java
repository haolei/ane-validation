package com.active.validation.metadata;

import java.util.concurrent.ConcurrentHashMap;


public class BeanMetaDataManager {

  private static final ConcurrentHashMap<Class<?>, BeanDescriptorImpl<?>> beanDescriptorCache = new ConcurrentHashMap<Class<?>, BeanDescriptorImpl<?>>();

  @SuppressWarnings("unchecked")
  public static <T> BeanDescriptorImpl<T> getBeanDescriptor( Class<T> beanClass ) {
    if ( beanDescriptorCache.containsKey( beanClass ) ) { return (BeanDescriptorImpl<T>)beanDescriptorCache
        .get( beanClass ); }

    BeanDescriptorImpl<T> bd = new BeanDescriptorImpl<T>( beanClass );
    beanDescriptorCache.putIfAbsent( beanClass, bd );

    return bd;
  }
  
}
