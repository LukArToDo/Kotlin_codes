
fun main(LukArToDo: Array<String>) {
    var missiles = 20

    var speedE = (1..enemy).map { rand(1, 4) }.toMutableList()
    var patternE = (1..enemy).map { (0..3).map { "RLD"[rand(0, 3)] }.joinToString() }.toMutableList()
    var positionE = (1..enemy).map { Pair(rand(-2 * r, 2 * r), rand(-r, r)) }.toMutableList()

    val speedA = (1..allies).map { rand(1, 4) }
    val patternA = (1..allies).map { (0..3).map { "RLD"[rand(0, 3)] }.joinToString() }.toList()
    val positionA = (1..allies).map { Pair(rand(-2 * r, 2 * r), rand(-r, r)) }.toMutableList()
    var (count, hit, miss) = arrayOf(0, 0, 0)
    var visibleEnemy= positionE.filter{it.first*it.first/4+ it.second*it.second<=r*r}.count()

    while (missiles > 0  && positionE.isNotEmpty() && visibleEnemy > 0) {
        infoMessage(missiles, speedE, patternE, positionE,positionA)
        radar(positionE, positionA)
        val succesHit = (0 until positionE.size).filter { positionE[it].first*positionE[it].first/4+positionE[it].second*positionE[it].second<=r*r && "RDL"[rand(0, 3)] == patternE[it][count % 4] }.take(missiles)
        missiles-= if(missiles>=visibleEnemy) visibleEnemy else missiles
        hit = succesHit.size
        miss = visibleEnemy - hit
        positionE = positionE.filterIndexed { index, pair -> index !in succesHit }.toMutableList()
        patternE = patternE.filterIndexed { index, s -> index !in succesHit }.toMutableList()
        speedE = speedE.filterIndexed { index, i -> index !in succesHit }.toMutableList()
        println("Sucessuful hits: $hit \nMissing: $miss \n******************************************\n")
        for (j in 0 until positionE.size) {
            speed = speedE[j]
            val pair = when (patternE[j][count % 4]) {
                'R' -> Move.R.dir(positionE[j])
                'L' -> Move.L.dir(positionE[j])
                else -> Move.D.dir(positionE[j])
            }
            positionE[j] = pair
        }
        for (j in 0 until positionA.size) {
            speed = speedA[j]
            val pair = when (patternA[j][count % 4]) {
                'R' -> Move.R.dir(positionA[j])
                'L' -> Move.L.dir(positionA[j])
                else -> Move.D.dir(positionA[j])
            }
            positionA[j] = pair
        }
        count++
        visibleEnemy= positionE.filter{it.first*it.first/4+ it.second*it.second<=r*r}.count()
    }
    infoMessage(missiles, speedE, patternE, positionE, positionA)
    radar(positionE, positionA)
    println("\n ${if(positionE.isNotEmpty()&&positionE.filter { it.first * it.first / 4 + it.second * it.second <= r * r }.count() > 0)"Defense system is fail! Bye, bye! :(" else if(positionE.isEmpty()) "Success!!! The enemy is destroyed! :)" else "Success! The enemy is gone but not destroyed!"}")

}

fun infoMessage(missiles: Int, speedE: List<Int>, patternE: List<String>, posE: MutableList<Pair<Int, Int>>, posA: MutableList<Pair<Int, Int>>) {
    println()
    println("Missiles: $missiles")
    println("Aircraft: ${allies+posE.size}")
    println("Allies: $allies")
    println("Allies visible on radar: ${posA.filter{it.first*it.first/4+ it.second*it.second<=r*r}.count()}")  // visible on radar
    println("Enemy: ${posE.size}")
    for(i in 0 until posE.size)
        println("   ${i+1}. position: ${posE[i]}, speed: ${speedE[i]}, moves: ${patternE[i]}")
    println("Enemy visible on radar: ${posE.filter{it.first*it.first/4+ it.second*it.second<=r*r}.count()}")  // visible on radar
    println()
}

val r=7 // radius of radar
var speed=0
var airCraft=10
var enemy=rand(0,airCraft)
var allies=airCraft-enemy

fun radar(posE:List<Pair<Int,Int>>,posA:List<Pair<Int,Int>>){
    for(y in -r..r)
        for(x in -2*r..2*r)
            print(if(x==2*r)"\n" else if(x==0&&y==0)"X" else if(Pair(x,y) in posE && x*x/4+y*y<=r*r)"E" else if(Pair(x,y) in posE) "*" else if(Pair(x,y) in posA&&x*x/4+y*y<=r*r) "A" else if(Pair(x,y) in posA) "*" else if(x*x/4+y*y<=r*r)"-" else " ")
}

// returns random number in range [0-max]
fun rand(min:Int,max: Int) = (Math.random()*(max-min)).toInt()+min

enum class Move : Direction {
    R{ override fun dir(pos:Pair<Int,Int>) = Pair(pos.first-1*speed,pos.second) },
    L{ override fun dir(pos:Pair<Int,Int>) = Pair(pos.first+1*speed,pos.second) },
    D{ override fun dir(pos:Pair<Int,Int>) = Pair(pos.first,pos.second+1*speed) }

}

interface Direction{
    fun dir(pos:Pair<Int,Int>):Pair<Int,Int>
}