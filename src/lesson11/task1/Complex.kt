@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import lesson1.task1.sqr

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

    /**
     * Конструктор из строки вида x+yi
     */
    constructor(s: String) : this(
        Regex("""^(-?\d+(\.\d+)*)?(([+-]\d+(\.\d+)*)i)?$""").find(s)?.groupValues?.get(1)?.toDouble() ?: 0.0,
        Regex("""^(-?\d+(\.\d+)*)?(([+-]\d+(\.\d+)*)i)?$""").find(s)?.groupValues?.get(4)?.toDouble() ?: 0.0
    )

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
