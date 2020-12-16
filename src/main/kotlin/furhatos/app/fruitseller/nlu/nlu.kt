package furhatos.app.fruitseller.nlu

import furhatos.app.fruitseller.flow.Options
import furhatos.app.fruitseller.flow.TakingOrder
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.nlu.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.util.Language

class RequestOptions: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("What options do you have?",
                "Who were the suspects?",
                "What were the options?",
                "Who do you have?",
                "Who can I talk to?",
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

    val autopsyResults = state() {
        onEntry {
                furhat.say("Detective! We just got the result from the autopsy. " +
                        "As suspected, they suggest that Albert might have been poisoned by inhalation." +
                        " He has traces of Ammonia in his lungs and around his mouth and nose." +
                        " In this amount and high concentration, it’s both toxic and deadly." +
                        " He must've died within a matter of minutes." +
                        " All I can say is, whoever did this must have ${furhat.voice.emphasis("really")} wanted him dead.")
            goto(TakingOrder)
                 } 
    }

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
class Suspects(
        firstName: String,
        lastName: String,
        job: String,
        relationshipAlbert: String,
        guilty: Boolean,
        texture: String,
        voice: String,
        evening: String,
        timeOfMurder: String,
        beforeAndAfter: String,
        responsible: String,
        suspicious: String
) {

    val initialConversation = state(Options) {
        onEntry {
            furhat.setTexture(texture)
            furhat.setVoice(Language.ENGLISH_GB, voice)
            furhat.say("Hello this is  ${"$firstName $lastName"}, i'm a $job.")
            furhat.ask("Did you have some questions for me?")
           /* if (guilty) {
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
            goto(TakingOrder)*/
        }

        onResponse<Yes> {
            goto(interviewConversation)
        }
    }

    val interviewConversation = state(Options) {
        onEntry {
            furhat.ask("So what questions did you have for me?")
        }

        onResponse<QuestionRelation> {
            furhat.say("I was Albert's $relationshipAlbert for a long time.")
            furhat.ask("Anything else you wonder?")
        }

        onResponse<QuestionEvening> {
            furhat.say(evening)
            furhat.ask("Anything else you wonder?")
        }

        onResponse<QuestionTimeOfMurder> {
            furhat.say(timeOfMurder)
            furhat.ask("Anything else you wonder?")
        }

        onResponse<QuestionBeforeAndAfter> {
            furhat.say(beforeAndAfter)
            furhat.ask("Anything else you wonder?")
        }

        onResponse<QuestionResponsible> {
            furhat.say(responsible)
            random(
                    { furhat.ask("Do you have even more questions?")},
                    { furhat.ask("Anything else you wonder?")}
            )
        }

        onResponse<QuestionSuspicious> {
            furhat.say(suspicious)
            furhat.ask("Anything else you wonder?")
        }

        onResponse<No> {
            furhat.say("Very well then")
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

class QuestionRelation: Intent() {

    override fun getExamples(lang: Language): List<String>{
        return listOf("How did you know +Albert", "How did you know +Albert Adams")
    }
}

class QuestionEvening: Intent() {
    override fun getExamples(lang: Language): List<String>{
        return listOf("Tell me about your +evening", "Tell me about your +evening friday the 13th, leading up to the murder")
    }
}

class QuestionTimeOfMurder: Intent() {
    override fun getExamples(lang: Language): List<String>{
        return listOf("Where were +you during the +time of the +murder", "Where were +you at the +time of the +murder")
    }
}

class QuestionBeforeAndAfter: Intent() {
    override fun getExamples(lang: Language): List<String>{
        return listOf("What did you do +before", "What did you do +before and after", "What did you do +before and after the murder?")
    }
}

class QuestionResponsible: Intent() {
    override fun getExamples(lang: Language): List<String>{
        return listOf("Who is +responsible", "Who do you believe is +responsible for the murder")
    }
}

class QuestionSuspicious: Intent() {
    override fun getExamples(lang: Language): List<String>{
        return listOf("Did you notice anything +suspicious that night?",  "Anyone who looked or acted particularly +suspicious?")
    }
}


