/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot
 */
package org.jscience.mathematics.number;

import org.jscience.mathematics.algebra.Semiring;
import org.jscience.mathematics.algebra.Lattice;
import org.jscience.mathematics.algebra.FiniteSet;

/**
 * Boolean algebraic type - matches Real/Integer pattern.
 * <p>
 * Uses singleton pattern + instance methods (like Real).
 * Fastest implementation: singletons + primitive operations.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public final class Boolean implements Semiring<Boolean>, Lattice<Boolean>, FiniteSet<Boolean>, Comparable<Boolean> {

   public static final Boolean TRUE = new Boolean(true);
   public static final Boolean FALSE = new Boolean(false);

   private final boolean value;

   private Boolean(boolean value) {
      this.value = value;
   }

   public static Boolean of(boolean value) {
      return value ? TRUE : FALSE;
   }

   public boolean booleanValue() {
      return value;
   }

   // Instance methods (like Real.add(Real other))
   public Boolean add(Boolean other) {
      return of(value || other.value);
   }

   public Boolean multiply(Boolean other) {
      return of(value && other.value);
   }

   public Boolean not() {
      return of(!value);
   }

   public Boolean xor(Boolean other) {
      return of(value ^ other.value);
   }

   // Semiring interface (2-parameter algebraic style)
   @Override
   public Boolean add(Boolean a, Boolean b) {
      return a.add(b);
   }

   @Override
   public Boolean zero() {
      return FALSE;
   }

   @Override
   public Boolean multiply(Boolean a, Boolean b) {
      return a.multiply(b);
   }

   @Override
   public Boolean one() {
      return TRUE;
   }

   @Override
   public boolean isMultiplicationCommutative() {
      return true;
   }

   // Lattice interface
   @Override
   public Boolean join(Boolean a, Boolean b) {
      return a.add(b); // OR = join
   }

   @Override
   public Boolean meet(Boolean a, Boolean b) {
      return a.multiply(b); // AND = meet
   }

   // FiniteSet interface
   @Override
   public boolean contains(Boolean element) {
      return true;
   }

   @Override
   public long size() {
      return 2;
   }

   @Override
   public java.util.Iterator<Boolean> iterator() {
      return java.util.Arrays.asList(FALSE, TRUE).iterator();
   }

   // Set interface
   @Override
   public boolean isEmpty() {
      return false;
   }

   @Override
   public String description() {
      return "𝔹 = {0, 1}";
   }

   // Magma interface
   @Override
   public Boolean operate(Boolean left, Boolean right) {
      return add(left, right);
   }

   // Standard Java
   @Override
   public int compareTo(Boolean other) {
      return java.lang.Boolean.compare(value, other.value);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!(obj instanceof Boolean))
         return false;
      return value == ((Boolean) obj).value;
   }

   @Override
   public int hashCode() {
      return java.lang.Boolean.hashCode(value);
   }

   @Override
   public String toString() {
      return java.lang.Boolean.toString(value);
   }
}
