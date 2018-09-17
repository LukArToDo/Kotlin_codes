fun main(LukArToDo: Array<String>) {
    print("$s = ")
    nextChar();
    val n=eval();
    print(if(index<s.length) "Unexpected char: ${c.toChar()}" else n)
}
var index=-1
var c=0
val s:String=readLine()!!

fun nextChar(){
    index=index.inc()
    c= if(index<s.length) s.get(index).toInt() else -1
}

fun eval(): Double{
    var n:Double = term();
    generateSequence(0){it+1}.forEach{
        n=if(operator('+')) n+term() else if(operator('-')) n-term() else return n }
    return n
}

fun term():Double{
    var n:Double=factor()
    generateSequence(0){it+1}.forEach{
        n= if(operator('*')) n*factor() else if(operator('/')) n/factor() else  if(operator('^')) Math.pow(n, factor()) else return n  }
    return n;
}

fun operator(ch:Char):Boolean{
    while(c.toChar()==' ') nextChar()
    if(c.toChar()==ch){
        nextChar()
        return true
    }
    return false
}

fun factor():Double{
    if(operator('+')) return factor()
    if(operator('-')) return -factor()
    var n:Double=0.0
    val startIndex=index
    if(operator('(')){
        n=eval()
        operator(')')
    } else if(c.toChar() in '0'..'9'||c== '.'.toInt()){
        while(c.toChar() in '0'..'9'||c== '.'.toInt()) nextChar()
        n=s.substring(startIndex,index). toDouble()
    } else if(c.toChar() in 'a'..'z'){
        while(c.toChar() in 'a'..'z') nextChar()
        val func=s.substring(startIndex, index)
        n=factor()
        n=if(func=="sqrt") Math.sqrt(n) else if(func=="sin") Math.sin(Math. toRadians(n)) else if(func=="cos") Math.cos(Math.toRadians(n)) else if (func=="tan") Math.tan(Math.toRadians (n)) else if(func=="abs") {if(n<0) -n else n} else{ print("Unknown function: $func");System.exit(1);return n }
    } else{
        print("Unexpected character: ${c.toChar()}")
        System.exit(1)
    }
    return n
}