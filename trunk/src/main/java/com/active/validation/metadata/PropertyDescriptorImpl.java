package com.active.validation.metadata;

import java.util.List;
import java.util.Set;

import javax.validation.metadata.GroupConversionDescriptor;
import javax.validation.metadata.PropertyDescriptor;


public class PropertyDescriptorImpl extends ElementDescriptorImpl implements PropertyDescriptor {
  
  private final String propertyName;

  public PropertyDescriptorImpl( String propertyName, Class<?> type, Set<ConstraintDescriptorImpl<?>> constraintDescriptors,
      List<Class<?>> defaultGroupSequence ) {
    super( type, constraintDescriptors, defaultGroupSequence );
    this.propertyName = propertyName;
  }

  public boolean isCascaded() {
    return false;
  }

  public Set<GroupConversionDescriptor> getGroupConversions() {
    return null;
  }

  public String getPropertyName() {
    return propertyName;
  }

}
