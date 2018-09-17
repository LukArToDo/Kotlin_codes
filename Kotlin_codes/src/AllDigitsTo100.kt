fun main(LukArToDo: Array<String>) {
    // call searchResult method and display result:
    println("All possible combinations of digits [1-9] and +/- sign to get 100:\n")
    findResult("123456789", "", 0, 0, 1, "+")
    println("\n\nAll possible combinations of digits [9-1] and +/- sign to get 100:\n")
    findResult("987654321", "", 0, 0, 1, "+")
}

fun findResult(input: String, currString: String, sum: Int, start: Int, next: Int, sign: String) {
    var sum = sum
    /* Legend: 👇
⏩ String input ➡️ string which contain all digits in the corresponding order
⏩ String currString ➡️ the part of the input processed so far
⏩ int sum ➡️ intermediate sum
⏩ int start ➡️ start of a current operand
⏩ int next ➡️ current position in the input string
⏩ String sign ➡️ sign (+ or -)
*/
    val current = input.substring(start, next)
    val num = Integer.parseInt(current)
    if (next == input.length) {
        sum += if (sign == "+") num else -num
        if (sum == 100)
            println("$currString$current=100")
        return
    }
    // skip sign
    findResult(input, currString, sum, start, next + 1, sign)
    // calculate an intermediate sum
    sum += if (sign == "+") num else -num
    // continue with next operand
    // add +
    findResult(input, "$currString$current+", sum, next, next + 1, "+")
    // add -
    findResult(input, "$currString$current-", sum, next, next + 1, "-")
}