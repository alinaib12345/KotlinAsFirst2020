@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import kotlin.math.abs
import kotlin.math.max

/**
 * Точка (гекс) на шестиугольной сетке.
 * Координаты заданы как в примере (первая цифра - y, вторая цифра - x)
 *
 *       60  61  62  63  64  65
 *     50  51  52  53  54  55  56
 *   40  41  42  43  44  45  46  47
 * 30  31  32  33  34  35  36  37  38
 *   21  22  23  24  25  26  27  28
 *     12  13  14  15  16  17  18
 *       03  04  05  06  07  08
 *
 * В примерах к задачам используются те же обозначения точек,
 * к примеру, 16 соответствует HexPoint(x = 6, y = 1), а 41 -- HexPoint(x = 1, y = 4).
 *
 * В задачах, работающих с шестиугольниками на сетке, считать, что они имеют
 * _плоскую_ ориентацию:
 *  __
 * /  \
 * \__/
 *
 * со сторонами, параллельными координатным осям сетки.
 *
 * Более подробно про шестиугольные системы координат можно почитать по следующей ссылке:
 *   https://www.redblobgames.com/grids/hexagons/
 */
data class HexPoint(val x: Int, val y: Int) {
    /**
     * Средняя (3 балла)
     *
     * Найти целочисленное расстояние между двумя гексами сетки.
     * Расстояние вычисляется как число единичных отрезков в пути между двумя гексами.
     * Например, путь межу гексами 16 и 41 (см. выше) может проходить через 25, 34, 43 и 42 и имеет длину 5.
     */
    fun distance(other: HexPoint): Int = when {
        other.y > y && other.x <= x && other.y + other.x >= y + x ||
                other.y < y && other.x >= x && other.y + other.x <= y + x -> abs(y - other.y)
        other.y >= y && other.x > x || other.y <= y && other.x < x -> abs(y - other.y) + abs(x - other.x)
        else -> abs(y - other.y) + abs(other.y + other.x - y - x)
    }


    override fun toString(): String = "$y.$x"
}

/**
 * Правильный шестиугольник на гексагональной сетке.
 * Как окружность на плоскости, задаётся центральным гексом и радиусом.
 * Например, шестиугольник с центром в 33 и радиусом 1 состоит из гексов 42, 43, 34, 24, 23, 32.
 */
data class Hexagon(val center: HexPoint, val radius: Int) {

    /**
     * Средняя (3 балла)
     *
     * Рассчитать расстояние между двумя шестиугольниками.
     * Оно равно расстоянию между ближайшими точками этих шестиугольников,
     * или 0, если шестиугольники имеют общую точку.
     *
     * Например, расстояние между шестиугольником A с центром в 31 и радиусом 1
     * и другим шестиугольником B с центром в 26 и радиуоом 2 равно 2
     * (расстояние между точками 32 и 24)
     */
    fun distance(other: Hexagon): Int =
        max(0, center.distance(other.center) - radius - other.radius)

    /**
     * Тривиальная (1 балл)
     *
     * Вернуть true, если заданная точка находится внутри или на границе шестиугольника
     */
    fun contains(point: HexPoint): Boolean = center.distance(point) <= radius

    /**
     * Проверяет, содержится ли точка на границе
     */

    fun isOnBorder(point: HexPoint): Boolean =
        point.x == center.x + radius && point.y in center.y - radius..center.y ||
                point.x == center.x - radius && point.y in center.y..center.y + radius ||
                point.y == center.y + radius && point.x in center.x - radius..center.x ||
                point.y == center.y - radius && point.x in center.x..center.x + radius ||
                point.x + point.y == center.x + center.y + radius && point.y in center.y..center.y + radius ||
                point.x + point.y == center.x + center.y - radius && point.y in center.y - radius..center.y

}

/**
 * Прямолинейный отрезок между двумя гексами
 */
class HexSegment(val begin: HexPoint, val end: HexPoint) {
    /**
     * Простая (2 балла)
     *
     * Определить "правильность" отрезка.
     * "Правильным" считается только отрезок, проходящий параллельно одной из трёх осей шестиугольника.
     * Такими являются, например, отрезок 30-34 (горизонталь), 13-63 (прямая диагональ) или 51-24 (косая диагональ).
     * А, например, 13-26 не является "правильным" отрезком.
     */
    fun isValid(): Boolean =
        begin != end && (begin.x == end.x || begin.y == end.y || begin.x + begin.y == end.x + end.y)

    /**
     * Средняя (3 балла)
     *
     * Вернуть направление отрезка (см. описание класса Direction ниже).
     * Для "правильного" отрезка выбирается одно из первых шести направлений,
     * для "неправильного" -- INCORRECT.
     */
    fun direction(): Direction = when {
        !isValid() -> Direction.INCORRECT
        begin.x == end.x -> {
            if (begin.y > end.y) Direction.DOWN_LEFT
            else Direction.UP_RIGHT
        }
        begin.y == end.y -> {
            if (begin.x > end.x) Direction.LEFT
            else Direction.RIGHT
        }
        else -> if (begin.y > end.y) Direction.DOWN_RIGHT
        else Direction.UP_LEFT
    }

    /**
     *Определяет направления кратчайшего пути для неправильного отрезка
     */
    fun directionForInvalid(): Pair<Direction, Direction> = when {
        begin.y > end.y && end.x in begin.x + 1 until begin.x + begin.y - end.y ->
            Pair(Direction.DOWN_LEFT, Direction.DOWN_RIGHT)
        begin.y < end.y && end.x in begin.x + begin.y + 1 - end.y until begin.x ->
            Pair(Direction.UP_LEFT, Direction.UP_RIGHT)
        begin.y < end.y && begin.x < end.x -> Pair(Direction.RIGHT, Direction.UP_RIGHT)
        begin.y < end.y && begin.x > end.x -> Pair(Direction.RIGHT, Direction.DOWN_RIGHT)
        begin.y > end.y && begin.x > end.x -> Pair(Direction.LEFT, Direction.DOWN_LEFT)

        else -> Pair(Direction.LEFT, Direction.UP_LEFT)
    }

    override fun equals(other: Any?) =
        other is HexSegment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()
}

/**
 * Направление отрезка на гексагональной сетке.
 * Если отрезок "правильный", то он проходит вдоль одной из трёх осей шестугольника.
 * Если нет, его направление считается INCORRECT
 */
enum class Direction {
    RIGHT,      // слева направо, например 30 -> 34
    UP_RIGHT,   // вверх-вправо, например 32 -> 62
    UP_LEFT,    // вверх-влево, например 25 -> 61
    LEFT,       // справа налево, например 34 -> 30
    DOWN_LEFT,  // вниз-влево, например 62 -> 32
    DOWN_RIGHT, // вниз-вправо, например 61 -> 25
    INCORRECT;  // отрезок имеет изгиб, например 30 -> 55 (изгиб в точке 35)

    /**
     * Простая (2 балла)
     *
     * Вернуть направление, противоположное данному.
     * Для INCORRECT вернуть INCORRECT
     */
    fun opposite(): Direction = when (this) {
        RIGHT -> LEFT
        LEFT -> RIGHT
        UP_LEFT -> DOWN_RIGHT
        DOWN_RIGHT -> UP_LEFT
        UP_RIGHT -> DOWN_LEFT
        DOWN_LEFT -> UP_RIGHT
        else -> INCORRECT
    }

    /**
     * Средняя (3 балла)
     *
     * Вернуть направление, повёрнутое относительно
     * заданного на 60 градусов против часовой стрелки.
     *
     * Например, для RIGHT это UP_RIGHT, для UP_LEFT это LEFT, для LEFT это DOWN_LEFT.
     * Для направления INCORRECT бросить исключение IllegalArgumentException.
     * При решении этой задачи попробуйте обойтись без перечисления всех семи вариантов.
     */
    fun next(): Direction = when (this) {
        INCORRECT -> throw IllegalArgumentException("Incorrect direction")
        DOWN_RIGHT -> RIGHT
        else -> values()[this.ordinal + 1]
    }

    /**
     * Простая (2 балла)
     *
     * Вернуть true, если данное направление совпадает с other или противоположно ему.
     * INCORRECT не параллельно никакому направлению, в том числе другому INCORRECT.
     */
    fun isParallel(other: Direction): Boolean = this != INCORRECT && (this == other || this == other.opposite())
}

/**
 * Средняя (3 балла)
 *
 * Сдвинуть точку в направлении direction на расстояние distance.
 * Бросить IllegalArgumentException(), если задано направление INCORRECT.
 * Для расстояния 0 и направления не INCORRECT вернуть ту же точку.
 * Для отрицательного расстояния сдвинуть точку в противоположном направлении на -distance.
 *
 * Примеры:
 * 30, direction = RIGHT, distance = 3 --> 33
 * 35, direction = UP_LEFT, distance = 2 --> 53
 * 45, direction = DOWN_LEFT, distance = 4 --> 05
 */
fun HexPoint.move(direction: Direction, distance: Int): HexPoint {
    if (direction == Direction.INCORRECT) throw IllegalArgumentException("Incorrect direction")
    if (distance == 0) return this
    return when (direction) {
        Direction.RIGHT -> HexPoint(x + distance, y)
        Direction.LEFT -> HexPoint(x - distance, y)
        Direction.UP_LEFT -> HexPoint(x - distance, y + distance)
        Direction.DOWN_RIGHT -> HexPoint(x + distance, y - distance)
        Direction.UP_RIGHT -> HexPoint(x, y + distance)
        Direction.DOWN_LEFT -> HexPoint(x, y - distance)
        else -> HexPoint(x, y)
    }
}

/**
 * Сложная (5 баллов)
 *
 * Найти кратчайший путь между двумя заданными гексами, представленный в виде списка всех гексов,
 * которые входят в этот путь.
 * Начальный и конечный гекс также входят в данный список.
 * Если кратчайших путей существует несколько, вернуть любой из них.
 *
 * Пример (для координатной сетки из примера в начале файла):
 *   pathBetweenHexes(HexPoint(y = 2, x = 2), HexPoint(y = 5, x = 3)) ->
 *     listOf(
 *       HexPoint(y = 2, x = 2),
 *       HexPoint(y = 2, x = 3),
 *       HexPoint(y = 3, x = 3),
 *       HexPoint(y = 4, x = 3),
 *       HexPoint(y = 5, x = 3)
 *     )
 */
fun pathBetweenHexes(from: HexPoint, to: HexPoint): List<HexPoint> {
    val result = mutableListOf<HexPoint>()
    var nextFrom = from
    val validity = HexSegment(from, to).isValid()
    if (validity) {
        val direction = HexSegment(from, to).direction()
        while (nextFrom != to) {
            result.add(nextFrom)
            nextFrom = nextFrom.move(direction, 1)
        }
    } else {
        val directionForInvalid = HexSegment(from, to).directionForInvalid()
        val firstD = directionForInvalid.first
        val secondD = directionForInvalid.second
        while (nextFrom.y != to.y && nextFrom.x + nextFrom.y != to.x + to.y && nextFrom.x != to.x) {
            result.add(nextFrom)
            nextFrom = nextFrom.move(firstD, 1)
        }
        while (nextFrom != to) {
            result.add(nextFrom)
            nextFrom = nextFrom.move(secondD, 1)
        }
    }
    result.add(to)
    return result
}

/**
 * Очень сложная (20 баллов)
 *
 * Дано три точки (гекса). Построить правильный шестиугольник, проходящий через них
 * (все три точки должны лежать НА ГРАНИЦЕ, а не ВНУТРИ, шестиугольника).
 * Все стороны шестиугольника должны являться "правильными" отрезками.
 * Вернуть null, если такой шестиугольник построить невозможно.
 * Если шестиугольников существует более одного, выбрать имеющий минимальный радиус.
 *
 * Пример: через точки 13, 32 и 44 проходит правильный шестиугольник с центром в 24 и радиусом 2.
 * Для точек 13, 32 и 45 такого шестиугольника не существует.
 * Для точек 32, 33 и 35 следует вернуть шестиугольник радиусом 3 (с центром в 62 или 05).
 *
 * Если все три точки совпадают, вернуть шестиугольник нулевого радиуса с центром в данной точке.
 */
fun hexagonByThreePoints(a: HexPoint, b: HexPoint, c: HexPoint): Hexagon? {
    if (setOf(a, b, c).size == 1) return Hexagon(a, 0)
    val maxRadius = maxOf(a.distance(b), a.distance(c), b.distance(c))
    val sideForA = mutableSetOf<Int>()
    //Определяет, на какой стороне может лежать точка а (начиная с самой верхней стороны против часовой стрелки)
    if (a.y >= b.y && a.y >= c.y) sideForA.add(0)
    if (a.x <= b.x && a.x <= c.x) sideForA.add(1)
    if (a.x + a.y <= b.x + b.y && a.x + a.y <= c.x + c.y) sideForA.add(2)
    if (a.y <= b.y && a.y <= c.y) sideForA.add(3)
    if (a.x >= b.x && a.x >= c.x) sideForA.add(4)
    if (a.x + a.y >= b.x + b.y && a.x + a.y >= c.x + c.y) sideForA.add(5)

    for (radius in maxRadius / 2..maxRadius) for (j in 0..radius) {
        val center = arrayOf(//Возможные варианты точки центра, в зависимости от положения точки а
            HexPoint(a.x + j, a.y - radius),
            HexPoint(a.x + radius, a.y - radius + j),
            HexPoint(a.x + radius - j, a.y + j),
            HexPoint(a.x - j, a.y + radius),
            HexPoint(a.x - radius, a.y + radius - j),
            HexPoint(a.x - radius + j, a.y - j)
        )
        for (k in sideForA)
            if (Hexagon(center[k], radius).isOnBorder(b) && Hexagon(center[k], radius).isOnBorder(c))
                return Hexagon(center[k], radius)
    }
    return null
}

/**
 * Очень сложная (20 баллов)
 *
 * Дано множество точек (гексов). Найти правильный шестиугольник минимального радиуса,
 * содержащий все эти точки (безразлично, внутри или на границе).
 * Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит один гекс, вернуть шестиугольник нулевого радиуса с центром в данной точке.
 *
 * Пример: 13, 32, 45, 18 -- шестиугольник радиусом 3 (с центром, например, в 15)
 */
fun minContainingHexagon(vararg points: HexPoint): Hexagon = TODO()
