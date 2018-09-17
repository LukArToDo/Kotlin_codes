import java.util.Collections
import java.util.TreeMap

fun main(args: Array<String>) {
    val sd = SpamDetector()
    sd.filter("Quick and easy jobsite")
    sd.filter("Will there be a Production Release tomorrow")
    sd.filter("Plan your retirement soon")
    sd.filter("You have won a state lottery claim your money now")
    sd.filter("register with our jobsite and get your dream job and money now")
    sd.filter("today with our jobsite making money is not all that hard")
    sd.markAsNotSpam("today with our jobsite making money is not all that hard")
    sd.filter("today with our jobsite making money is not all that hard")
    sd.filter("back to office today and started off coding")
}

class SpamDetector {

    var spamCount = 0
    var hamCount = 0
    var map = mutableMapOf<String, Email>()
    var commonWords= mutableListOf<String>()

    init {
        commonWords.addAll(arrayOf ("all","and","able", "been", "being","could", "does", "done", "first", "good", "have","just", "many", "most","our", "should", "the", "this", "that", "then", "they", "value", "would", "when", "with", "your","you","not","who"))

        val spam = arrayOf("Quick and easy way to make money",
                "Congratulations you have won a lottery",
                "Claim your prize now",
                "Thanks for applying to our jobsite",
                "Greetings from jobsite",
                "Solution for all your financial needs",
                "Buy home now at less money",
                "Dream of your home",
                "You are minutes away from retirement",
                "One stop solution for your financial needs",
                "Plan your retirement days",
                "Its all about money honey",
                "Our jobsite found that you have not been active",
                "You have won the US state lottery",
                "Help me sell this piece of land",
                "act now","apply now", "buy now", "click below","quick and easy jobsite", "click here", "click me to download",
                "click this link", "click to remove", "call free", "call now", "claim now", "contact us immediately",
                "get now", "sign up free", "shop now", "order now", "get paid","$$$", "£££", "accounts", "additional income",
                "bank", "bonus", "cash", "cost", "credit", "earn", "earn $", "earn money", "earn per week", "finance", "financial advice", "financial freedom", "free investment”, " +
                "get your money", "insurance", "investment", "investment advice", "investment decision", "invoice", "lowest price", "make £", "make money", "million", "money",
                "money back", "nominated bank account", "potential earnings", "profit", "refund", "risk free", "save", "save $", "stock alert", "thousands", "US Dollars", "amazing",
                "bargain", "beneficial offer", "cheap", "clearance", "congratulations", "dear", "direct marketing", "don’t delete", "email marketing", "fantastic", "free", "free trial",
                "gift certificate", "increase sales", "increase traffic", "internet marketing", "junk", "marketing", "marketing solution", "member", "message from", "month trial offer",
                "off everything", "offer", "offer expires", "offer extended", "online marketing", "opportunity", "opt in", "performance", "promise you", "sale", "search engine optimisation",
                "spam", "special promotion", "stop further distribution", "subscribe", "super promo", "the following form", "this isn’t junk", "this isn’t spam", "top urgent",
                "unbeatable offer", "unsubscribe", "urgent", "urgent response", "visit our website", "web traffic", "win", "winner","additional income", "be your own boss", "extra income",
                "free hosting", "get paid", "home based business", "home employment", "income from home", "profit", "sale", "work at home", "work from home", "while you sleep",",000", "!!$",
                "mlm", "$$$",  "@mlm", "100% satisfied", "cards accepted", "check or money order", "extra income", "for free?", "for free!", "Guarantee", "absolute", "money back",
                "money-back guarantee", "more info", "visit", "onetime mail", "order now!", "order today", "removal instructions", "SPECIAL PROMOTION")

        for(line in spam) moveToSpam(line)

        val ham= arrayOf("Code walk-thru meeting scheduled today",
                "I will be late to the office today",
                "We will have a day off tomorrow after the release",
                "Production is down today",
                "I will be transferring the money to you today",
                "Some of the changes you did are not right",
                "Lets hope that the code changes go smooth",
                "Have you finished your code changes")
        for(line in ham) markAsNotSpam(line)
    }

    fun markAsNotSpam(line: String){
        var line= line.toLowerCase()
        val words=line.split("\\s". toRegex()).filter{it.isNotEmpty()}. toTypedArray()
        for (s in words)
            if (s.length > 2 && !commonWords.contains(s)) {
                hamCount++
                if(map.containsKey (s))  map[s]!!.hamCount++
                else map[s]= Email(0,1)
            }
        setProbability()
    }

    fun moveToSpam(line: String) {
        var line=line.toLowerCase()
        val words = line.split("\\s". toRegex()).filter{it.isNotEmpty()}. toTypedArray()
        for (s in words)
            if (s.length > 2 && !commonWords.contains(s)) {
                spamCount++
                if(map.containsKey (s)) map[s]!!.spamCount++
                else map[s] = Email(1,0)
            }
        setProbability()
    }

    private fun setProbability() {
        val keys = map.keys
        for (key in keys) {
            val email = map[key]
            val res = email!!. spamCount / spamCount.toDouble() / (email!!.spamCount / spamCount. toDouble() + email!!.hamCount / hamCount.toDouble())
            email.probability = res
        }
    }

    fun filter(s: String) {
        var s = s.toLowerCase()
        var result = false
        val sArr = s.split("\\s". toRegex()).filter{it.isNotEmpty()}. toTypedArray()
        val interestMap = TreeMap<Double, MutableList<Double>> (Collections.reverseOrder())
        for (x in sArr)
            if (x.length > 2 && !commonWords.contains(x)) {
                var i = 0.5
                var p = 0.5
                if (map.containsKey (x)) p = map[x]!!.probability
                i = Math.abs(i - p)
                if (!interestMap. containsKey(i)) {interestMap[i] =mutableListOf(p)
                } else { interestMap[i]!!.add(p) }
            }
        val probabilities = mutableListOf<Double>()
        var count = 0
        val setK = interestMap.keys
        for (d in setK) {
            val list = interestMap[d]
            for (x in list!!) {
                count++
                probabilities.add(x)
                if (count == 15) {
                    break
                }
            }
            if (count == 15) {
                break
            }
        }

        var res = 1.0
        var numerator = 1.0
        var denominator = 1.0
        for (d in probabilities) {
            numerator = numerator * d
            denominator = denominator * (1 - d)
        }
        res = numerator / (numerator + denominator)
        if (res >= 0.9) {
            result = true
        }

        println("'" + s + if (result) "' is spam" else "' is not a spam")

    }

}

class Email(var spamCount: Int, var hamCount: Int) {
    var probability: Double = 0.0

    override fun toString() = probability.toString()

}