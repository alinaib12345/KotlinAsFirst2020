package lesson11.task1

import lesson11.task1.Complex.Companion.toComplex
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag

internal class ComplexTest {

    private fun assertApproxEquals(expected: Complex, actual: Complex, eps: Double) {
        assertEquals(expected.re, actual.re, eps)
        assertEquals(expected.im, actual.im, eps)
    }

    @Test
    @Tag("2")
    fun plus() {
        assertApproxEquals(Complex(4.257, -2.17), toComplex("1.25+2i") + toComplex("3.007-4.17i"), 1e-10)
        assertApproxEquals(Complex(0.0, -6.17), toComplex("-2i") + toComplex("-4.17i"), 1e-10)
    }

    @Test
    @Tag("2")
    operator fun unaryMinus() {
        assertApproxEquals(Complex(-2.0, 1.0), -Complex(2.0, -1.0), 1e-10)
    }

    @Test
    @Tag("2")
    fun minus() {
        assertApproxEquals(Complex(2.1, 6.17), toComplex("-1.1+2.17i") - toComplex("-3.2-4i"), 1e-10)
    }

    @Test
    @Tag("4")
    fun times() {
        assertApproxEquals(Complex(16.0, 3.0), toComplex("2.4-2.2i") * toComplex("3+4i"), 1e-10)
    }

    @Test
    @Tag("4")
    fun div() {
        assertApproxEquals(Complex(3.0, 4.0), toComplex("16+3i") / toComplex("2.4-2.2i"), 1e-10)
    }

    @Test
    @Tag("2")
    fun equals() {
        assertTrue(Complex(1.0, 2.0) == toComplex("1+2i"))
        assertFalse(Complex(1.0, 0.0) == Complex(-1.0))
    }
}