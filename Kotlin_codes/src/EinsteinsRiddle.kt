/*
 Einstein's Riddle is a logic puzzle. It is often claimed that only 2% of the population can solve the puzzle.

It has several variants, one of them is this:
 
1. There are five houses.
2. The Mathematician lives in the red house.
3. The Hacker writes in Python.
4. Brackets (code editor) is used in the green house.
5. The Analyst uses Atom (code editor).
6. The green house is immediately to the right of the ivory house.
7. The man who uses Redis (Database System) writes in Java. 
8. Cassandra (Database System) is used in the yellow house.
9. Notepad++ (code editor) is used in the middle house.
10. The Developer lives in the first house.
11. The man who uses Hadoop (Database System) lives in the house next to the man writing in JavaScript.
12. Cassandra is used in the house next to the house where the primary language is C#.
13. The man who uses ArangoDB uses Sublime Text (code editor).
14. The Engineer uses MongoDB.
15. The Developer lives next to the blue house.

Now, who uses Vim? Who writes in C++? 
 */
fun main(LukArToDo: Array<String>) {
    val perm = IntArray(5) { it }
    for (i in 0..119) {
        for (j in 0..4) p[i][j] = perm[j]
        allPerm(perm)
    }
    val solutions = fillHouses()
    val plural = if (solutions == 1) "" else "s"
    println("$solutions solution$plural found")
}

fun allPerm(perm: IntArray): Boolean {
    val size = perm.size
    var k = -1
    for (i in size - 2 downTo 0) {
        if (perm[i] < perm[i + 1]) {
            k = i
            break
        }
    }
    if (k == -1) return false  // last permutation
    for (l in size - 1 downTo k) {
        if (perm[k] < perm[l]) {
           val temp = perm[k]
           perm[k] = perm[l]
           perm[l] = temp
           var m = k + 1
           var n = size - 1
           while (m < n) {
               val temp2 = perm[m]
               perm[m++] = perm[n]
               perm[n--] = temp2
           }
           break
        }
    }
    return true
}
 
fun check(a1: Int, a2: Int, v1: Int, v2: Int): Boolean {
    for (i in 0..4)
        if (p[a1][i] == v1) return p[a2][i] == v2
    return false
}
 
fun checkLeft(a1: Int, a2: Int, v1: Int, v2: Int): Boolean {
    for (i in 0..3)
        if (p[a1][i] == v1) return p[a2][i + 1] == v2
    return false
}
 
fun checkRight(a1: Int, a2: Int, v1: Int, v2: Int): Boolean {
    for (i in 1..4)
        if (p[a1][i] == v1) return p[a2][i - 1] == v2
    return false
}
 
fun checkAdjacent(a1: Int, a2: Int, v1: Int, v2: Int): Boolean {
    return checkLeft(a1, a2, v1, v2) || checkRight(a1, a2, v1, v2)
}

val colors  = listOf("Red", "Green","Ivory","Yellow","Blue")
val freaks = listOf("Mathemat.","Hacker", "Analyst","Developer","Engineer")
val languages = listOf("Python","Java", "JScript","C#","C++")
val editors  = listOf("Atom","Brackets", "Notepad","Sublime","Vim")
val bases  = listOf("Redis","Cassandra", "Hadoop","ArangoDB","MongoDB")
 
val p = Array(120) { IntArray(5) { -1 } } //  stores all permutations of numbers 0..4
 
fun fillHouses(): Int {
    var solutions = 0
    for (c in 0..119) {
        if (!checkLeft(c, c, 1, 2)) continue                      
        for (f in 0..119) {
            if (p[f][0] != 3) continue                            
            if (!check(f, c, 0, 0)) continue                     
            if (!checkAdjacent(f, c, 3, 4)) continue              
            for (l in 0..119) {
                if (!check(l, f, 0, 1)) continue                  
                for (e in 0..119) {
                    if (p[e][2] != 2) continue                    
                    if (!check(e, f, 0, 2)) continue              
                    if (!check(e, c, 1, 1)) continue              
                    for (b in 0..119) {
                        if (!check(b, l, 0, 1)) continue         
                        if (!check(b, c, 1, 3)) continue          
                        if (!check(b, e, 3, 3)) continue          
                        if (!check(b, f, 4, 4)) continue          
                        if (!checkAdjacent(b, l, 2, 2)) continue  
                        if (!checkAdjacent(b, l, 1, 3)) continue  
                        if (!checkAdjacent(b, e, 2, 4)) continue  
                        solutions++
                        printHouses(c, f, l, e, b)
                    }
                }
            }
        }
    }
    return solutions
}
 
fun printHouses(c: Int, f: Int, l: Int, e: Int, b: Int) {
    var freakL: String = ""
	var freakE: String =""
    println("House  Color   Freaks     Languages   Editors   Bases")
    println("=====  ======  =========  ==========  ========  ===========")
    for (i in 0..4) {
        val form = "%3d    %-6s  %-9s  %-10s  %-8s  %-11s\n"
        System.out.printf(form, i + 1, colors[p[c][i]], freaks[p[f][i]], languages[p[l][i]], editors[p[e][i]], bases[p[b][i]])
        if (languages[p[l][i]] == "C++") freakL = freaks[p[f][i]]
		if(editors[p[e][i]]=="Vim") freakE=freaks[p[f][i]]
    }
    println("\nThe $freakL write in C++")
	println("The $freakE use the Vim\n")
}
 
