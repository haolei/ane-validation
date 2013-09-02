package com.active.validation.metadata;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.groups.Default;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ElementDescriptor;
import javax.validation.metadata.Scope;


public class ElementDescriptorImpl implements ElementDescriptor {

  private final Class<?> type;
  private final Set<ConstraintDescriptorImpl<?>> constraintDescriptors;
  private final List<Class<?>> defaultGroupSequence;

  public ElementDescriptorImpl( Class<?> type, Set<ConstraintDescriptorImpl<?>> constraintDescriptors,
      List<Class<?>> defaultGroupSequence ) {
    this.type = type;
    this.constraintDescriptors = Collections.unmodifiableSet( constraintDescriptors );
    this.defaultGroupSequence = Collections.unmodifiableList( defaultGroupSequence );
  }

  public boolean hasConstraints() {
    return constraintDescriptors.size() != 0;
  }

  public Class<?> getElementClass() {
    return type;
  }

  public Set<ConstraintDescriptor<?>> getConstraintDescriptors() {
    return findConstraints().getConstraintDescriptors();
  }

  public ConstraintFinder findConstraints() {
    return new ConstraintFinderImpl();
  }

  protected Set<ConstraintDescriptorImpl<?>> getAllConstraintDescriptors() {
    return constraintDescriptors;
  }

  private class ConstraintFinderImpl implements ConstraintFinder {

    private List<Class<?>> groups;
    private Set<Scope> definedInSet;
    private Set<ElementType> elementTypes;

    ConstraintFinderImpl() {
      elementTypes = new HashSet<ElementType>();
      elementTypes.add( ElementType.FIELD );

      definedInSet = new HashSet<Scope>();
      definedInSet.add( Scope.LOCAL_ELEMENT );
      definedInSet.add( Scope.HIERARCHY );
      groups = Collections.emptyList();
    }

    private boolean defaultGroupSequenceRedefined() {
      return defaultGroupSequence != null && !defaultGroupSequence.isEmpty();
    }

    public ConstraintFinder unorderedAndMatchingGroups( Class<?>... classes ) {
      this.groups = new ArrayList<Class<?>>();
      for ( Class<?> clazz : classes ) {
        if ( Default.class.equals( clazz ) && defaultGroupSequenceRedefined() ) {
          this.groups.addAll( defaultGroupSequence );
        }
        else {
          groups.add( clazz );
        }
      }
      return this;
    }

    public ConstraintFinder lookingAt( Scope visibility ) {
      if ( visibility.equals( Scope.LOCAL_ELEMENT ) ) {
        definedInSet.remove( Scope.HIERARCHY );
      }
      return this;
    }

    public ConstraintFinder declaredOn( ElementType... elementTypes ) {
      this.elementTypes.clear();
      this.elementTypes.addAll( Arrays.asList( elementTypes ) );
      return this;
    }

    public Set<ConstraintDescriptor<?>> getConstraintDescriptors() {

      Set<ConstraintDescriptor<?>> matchingDescriptors = new HashSet<ConstraintDescriptor<?>>();
      findMatchingDescriptors( matchingDescriptors );
      return Collections.unmodifiableSet( matchingDescriptors );
    }

    private void findMatchingDescriptors( Set<ConstraintDescriptor<?>> matchingDescriptors ) {
      if ( !groups.isEmpty() ) {
        for ( Class<?> clazz : groups ) {
          addMatchingDescriptorsForGroup( clazz, matchingDescriptors );
        }
      }
      else {
        for ( ConstraintDescriptorImpl<?> descriptor : getAllConstraintDescriptors() ) {
          if ( definedInSet.contains( descriptor.getDefinedOn() )
              && elementTypes.contains( descriptor.getElementType() ) ) {
            matchingDescriptors.add( descriptor );
          }
        }
      }
    }

    public boolean hasConstraints() {
      return !getConstraintDescriptors().isEmpty();
    }

    private void addMatchingDescriptorsForGroup( Class<?> group, Set<ConstraintDescriptor<?>> matchingDescriptors ) {
      for ( ConstraintDescriptorImpl<?> descriptor : getAllConstraintDescriptors() ) {
        if ( definedInSet.contains( descriptor.getDefinedOn() ) && elementTypes.contains( descriptor.getElementType() )
            && descriptor.getGroups().contains( group ) ) {
          matchingDescriptors.add( descriptor );
        }
      }
    }
  }
}
