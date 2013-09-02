package com.active.validation.meta;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.junit.Assert;
import org.junit.Test;

import com.active.validation.util.AnnotationUtil;


public class AnnotationUtilTest {

  @Test
  public void testCreateAnnotationInstance() {
    Map<String, Object> overrides = new HashMap<String, Object>();
    overrides.put( "message", "orms.plugin" );
    NotNull nn = AnnotationUtil.newAnnotationInstance( NotNull.class, overrides );
    Assert.assertNotNull( nn );
    Assert.assertEquals( nn.message(), "orms.plugin" );
  }

  @Test
  public void testGetAnnotationParameters() {
    Map<String, Object> attributes = AnnotationUtil.getAnnotationParameters( AnnotationUtil.newAnnotationInstance(
        NotNull.class, null ) );
    Assert.assertFalse( attributes.isEmpty() );
  }
}
