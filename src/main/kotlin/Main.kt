object Main {

    @JvmStatic fun main(args: Array<String>) {

        listOf("T", "E", "S", "T")
                .reduce { acc, value -> acc + value }
                .let(::println)

        println(
                compose(
                        addQuestionMark(),
                        addQuestionMark(),
                        addQuestionMark()
                )("test")
        )

        val tripleQuestionMark = compose(addQuestionMark(), addQuestionMark())
        val composedReduce = composeReduce(tripleQuestionMark, tripleQuestionMark, tripleQuestionMark)

        println(composedReduce("Tast"))

        println(
                composeReduce(
                        addQuestionMark(),
                        addQuestionMark(),
                        addQuestionMark()
                )("test")
        )

        val composedString = composeGeneric<String>(
                { it + "?" },
                { it.hashCode().toString() }
        )("Wabaloba dup dup")

        println(composedString)

        val finalCompose = composedReduce andThen
                composedReduce andThen
                composedReduce

        println(finalCompose("Schezuan"))
    }


    infix fun <P1, IP, R> Function1<P1, IP>.andThen(f: (IP) -> R): (P1) -> R = forwardCompose(f)

    infix fun <P1, IP, R> Function1<P1, IP>.forwardCompose(f: (IP) -> R): (P1) -> R {
        return { p1: P1 -> f(this(p1)) }
    }

    fun <T> composeGeneric(vararg funcs: (T) -> T): (T) -> T =
            funcs.reduce { acc, function -> { acc(function(it)) } }

    fun composeReduce(vararg funcs: (String) -> String): (String) -> String {
        return funcs.reduce { acc, function -> { acc(function(it)) } }
    }

    fun compose(vararg funcs: (String) -> String): (String) -> String {
        return { initialText ->
            var result: String? = null
            for (func in funcs) {
                if (result == null) {
                    result = func(initialText)
                } else {
                    result = func(result)
                }
            }
            result!!
        }
    }

    fun addQuestionMark(): (String) -> String = { text -> text + "?" }
}
