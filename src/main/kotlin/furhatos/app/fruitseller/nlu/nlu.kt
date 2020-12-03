package furhatos.app.fruitseller.nlu

import furhatos.nlu.*
import furhatos.util.Language

class RequestOptions: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("What options do you have?",
                "Who do you have?",
                "Whom can I talk to?",
                "Who can I visit?",
                "What persons do you have?",
                "What names do you have?",
                "What are my options?")
    }
}

class NameList : ListEntity<GetName>()

class GetName(
        var name : Name? = null) : ComplexEnumEntity() {
        override fun getEnum(lang: Language): List<String> {
        return listOf("@name")
    }
    override fun toText(): String {
        return generate(name?.value)
    }
}

class Name : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Albert", "Albert Adams","Harold", "Harold Hoffman", "The professor", "Francis", "Francis Franklin", "the cleaner", "Carol", "Carol Clark", "the wife")
    }
}

// BuyFruit
class VisitName(var names : NameList? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@names", "Let me speak to @names", "I would like to speak to @names", "I wanna see @names", "I choose @names")
    }
}