package furhatos.app.fruitseller.nlu

import furhatos.app.fruitseller.flow.Options
import furhatos.app.fruitseller.flow.TakingOrder
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.nlu.*
import furhatos.util.Language

class RequestOptions: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("What options do you have?",
                "Who were the suspects?",
                "What were the options?",
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
        //return listOf("Albert", "Albert Adams","Harold", "Harold Hoffman", "The professor", "Francis", "Francis Franklin", "the cleaner", "Carol", "Carol Clark", "the wife")
        return listOf("Harold", "Francis", "Carol")
    }
}

// Visit suspect
class VisitName(var names : NameList? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@names", "Let me speak to @names", "I would like to speak to @names", "I wanna see @names", "I choose @names")
    }
}

// This class contains methods for the actual gameplay.
class GamePlay : Intent() {
    fun guessMurder() : State = state(Options) {
        onEntry {
            furhat.ask("You have interviewed all the suspects. Who do you think is the murder?")
        }
        onResponse{
            if (it.text == "Francis") {
                furhat.say("That is correct! You win.")
            }else{
                furhat.say("That is incorrect! Game over.")
            }
        }
    }
}

// This class contains all the suspects, with methods containing the different interview questions
class Suspects constructor(
        firstName: String,
        lastName: String,
        job: String,
        relationshipAlbert: String,
        guilty: Boolean,
        texture: String,
        voice: String) {

    val initialConversation = state(Options) {
        onEntry {
            furhat.setTexture(texture)
            furhat.setVoice(Language.ENGLISH_GB, voice)
            furhat.say("Hello this is  ${"$firstName $lastName"}, i'm a $job.")
            if (guilty) {
                furhat.say("I'm guilty, oooops.")
            } else {
                furhat.say("I'm innocent. I was Albert's $relationshipAlbert for Gods sake! Good bye.")
            }
            furhat.setTexture("male")
            furhat.setVoice(Language.ENGLISH_GB, "Brian")
            if (firstName=="Carol") {
                furhat.say("Yeah she's pretty rude.")
            }else{
                furhat.say("Yeah he's pretty rude.")
            }
            goto(TakingOrder)
        }
    }
}


