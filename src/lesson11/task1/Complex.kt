@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import lesson1.task1.sqr
import java.lang.IllegalArgumentException

/**
 * Класс "комплексное число".
 *
 * Общая сложность задания -- лёгкая, общая ценность в баллах -- 8.
 * Объект класса -- комплексное число вида x+yi.
 * Про принципы работы с комплексными числами см. статью Википедии "Комплексное число".
 *
 * Аргументы конструктора -- вещественная и мнимая часть числа.
 */
class Complex(var re: Double, var im: Double) {
    /**
     * Конструктор из вещественного числа
     */
    constructor(x: Double) : this(x, 0.0)

    companion object {
        /**
         * Функция преобразования из строки вида x+yi
         */
        fun toComplex(s: String): Complex {
            val match = Regex("""^(-?\d+(\.\d+)*)?(([+-]\d+(\.\d+)*)i)?$""").find(s)
                ?: throw IllegalArgumentException("Illegal string format")
            val re = if (match.groupValues[1].isEmpty()) 0.0 else match.groupValues[1].toDouble()
            val im = if (match.groupValues[4].isEmpty()) 0.0 else match.groupValues[4].toDouble()
            return Complex(re, im)
        }
    }

    /**
     * Сложение.
     */
    operator fun plus(other: Complex): Complex = Complex(re + other.re, im + other.im)

    /**
     * Смена знака (у обеих частей числа)
     */
    operator fun unaryMinus(): Complex = Complex(-re, -im)

    /**
     * Вычитание
     */
    operator fun minus(other: Complex): Complex = Complex(re - other.re, im - other.im)

    /**
     * Умножение
     */
    operator fun times(other: Complex): Complex = Complex(re * other.re - im * other.im, re * other.im + im * other.re)

    /**
     * Деление
     */
    operator fun div(other: Complex): Complex {
        val a = sqr(other.re) + sqr(other.im)
        return Complex((re * other.re + im * other.im) / a, (im * other.re - re * other.im) / a)
    }

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = other is Complex && re == other.re && im == other.im


    /**
     * Преобразование в строку
     */
    override fun toString(): String = "${re}+${im}i"

    override fun hashCode(): Int {
        var result = re.hashCode()
        result = 31 * result + im.hashCode()
        return result
    }

}



