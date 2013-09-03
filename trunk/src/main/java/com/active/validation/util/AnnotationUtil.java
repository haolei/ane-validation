package com.active.validation.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnnotationUtil {

  @SuppressWarnings("unchecked")
  public static <T> T getAnnotationParameter( Annotation annotation, String parameterName, Class<T> type ) {
    T result = null;
    Method m;
    try {
      m = annotation.getClass().getMethod( parameterName );
      m.setAccessible( true );
      Object o = m.invoke( annotation );
      if ( o.getClass().getName().equals( type.getName() ) ) { return (T)o; }
    }
    catch ( Exception e ) {
      e.printStackTrace();
    }

    return result;
  }

  public static Object getAnnotationParameter( Annotation annotation, Method m ) {
    try {
      m.setAccessible( true );
      return m.invoke( annotation );
    }
    catch ( Exception e ) {
      e.printStackTrace();
    }

    return null;
  }

  public static Map<String, Object> getAnnotationParameters( Annotation annotation ) {
    Map<String, Object> parameters = new HashMap<String, Object>();
    for ( Method m : ReflectionUtil.getAllMethods( annotation.annotationType(),
        ReflectionUtil.withParametersCount( 0 ) ) ) {
      try {
        m.setAccessible( true );
        parameters.put( m.getName(), m.invoke( annotation ) );
      }
      catch ( Exception e ) {
        // ignore
      }
    }
    return parameters;
  }

  @SuppressWarnings("unchecked")
  public static <A extends Annotation> A newAnnotationInstance( Class<A> annotationType, Map<String, Object> elements ) {
    if ( elements == null ) {
      elements = Collections.emptyMap();
    }
    return (A)Proxy.newProxyInstance( annotationType.getClassLoader(), new Class[]{ annotationType },
        new AnnotaionProxy( annotationType, elements ) );
  }

  static class AnnotaionProxy implements Annotation, InvocationHandler {

    private static final String ANNOTATION_TYPE_METHOD = "annotationType";
    private final Class<? extends Annotation> annotationType;
    private final Map<String, Object> values;

    AnnotaionProxy( Class<? extends Annotation> annotationType, Map<String, Object> values ) {
      this.annotationType = annotationType;
      this.values = values;
    }

    public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {
      if ( values.containsKey( method.getName() ) ) { return values.get( method.getName() ); }
      if ( method.getName().equals( ANNOTATION_TYPE_METHOD ) ) return annotationType;
      return method.getDefaultValue();
    }

    public Class<? extends Annotation> annotationType() {
      // TODO Auto-generated method stub
      return null;
    }
  }
}
