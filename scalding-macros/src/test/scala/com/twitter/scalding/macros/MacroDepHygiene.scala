package com.twitter.scalding.macros

import org.scalatest.WordSpec
import com.twitter.scalding.macros.{ _ => _ }

/**
 * This test is intended to ensure that the macros do not require any imported code in scope. This is why all
 * references are via absolute paths.
 */
class MacroDepHygiene extends WordSpec {
  import com.twitter.bijection.macros.impl.IsCaseClassImpl

  case class A(x: Int, y: String)
  case class B(x: A, y: String, z: A)
  class C

  def isMg(a: Any) = a.isInstanceOf[com.twitter.bijection.macros.MacroGenerated]

  "TupleSetter macro" should {
    def isTupleSetterAvailable[T](implicit proof: com.twitter.scalding.TupleSetter[T]) = isMg(proof)

    "work fine without any imports" in {
      com.twitter.scalding.macros.Macros.caseClassTupleSetter[A]
      com.twitter.scalding.macros.Macros.caseClassTupleSetter[B]
    }

    "implicitly work fine without any imports" in {
      import com.twitter.scalding.macros.MacroImplicits.materializeCaseClassTupleSetter
      assert(isTupleSetterAvailable[A])
      assert(isTupleSetterAvailable[B])
    }

    "fail if not a case class" in {
      assert(!isTupleSetterAvailable[C])
    }
  }

  "TupleConverter macro" should {
    def isTupleConverterAvailable[T](implicit proof: com.twitter.scalding.TupleConverter[T]) = isMg(proof)

    "work fine without any imports" in {
      com.twitter.scalding.macros.Macros.caseClassTupleConverter[A]
      com.twitter.scalding.macros.Macros.caseClassTupleConverter[B]
    }

    "implicitly work fine without any imports" in {
      import com.twitter.scalding.macros.MacroImplicits.materializeCaseClassTupleConverter
      assert(isTupleConverterAvailable[A])
      assert(isTupleConverterAvailable[B])
    }

    "fail if not a case class" in {
      assert(!isTupleConverterAvailable[C])
    }
  }
}
