package furhatos.app.fruitseller.nlu

import furhatos.app.fruitseller.flow.Options
import furhatos.app.fruitseller.flow.ChooseToQuestion
import furhatos.flow.kotlin.*
import furhatos.nlu.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.util.Language
import furhatos.gestures.Gestures
import furhatos.gestures.defineGesture
import furhatos.gestures.BasicParams
import zmq.socket.reqrep.Rep
import java.util.Arrays


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

class RepeatQuestion: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Can you +repeat the question?",
                "Can you +repeat?",
                "Can you +repeat that?",
                "Could you +repeat?")
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
    val autopsyResults = state {
        println("Anna05")
        onEntry {
                furhat.say("Detective! We just got the result from the autopsy. " +
                        "As suspected, they suggest that Albert might have been poisoned by inhalation." +
                        " He has traces of Ammonia in his lungs and around his mouth and nose." +
                        " In this amount and high concentration, it’s both toxic and deadly." +
                        " He must've died within a matter of minutes." +
                        " All I can say is, whoever did this must have ${furhat.voice.emphasis("really")} wanted him dead.")
                furhat.listen()
                //terminate() //calling this state will resume the execution in ChooseToQuestion (Takingorder)
                 }

        onResponse<RepeatQuestion> {
            furhat.say("Of course.")
            furhat.say("Detective! We just got the result from the autopsy. " +
                    "As suspected, they suggest that Albert might have been poisoned by inhalation." +
                    " He has traces of Ammonia in his lungs and around his mouth and nose." +
                    " In this amount and high concentration, it’s both toxic and deadly." +
                    " He must've died within a matter of minutes." +
                    " All I can say is, whoever did this must have ${furhat.voice.emphasis("really")} wanted him dead.")
            terminate()
        }

        onNoResponse {
            terminate()
        }
    }

    fun chooseToGuess() : State = state(Options) {
        onEntry {
            furhat.ask("Do you wanna guess on the murderer?")
        }
        onResponse<Yes> {
            goto(guessMurder())
        }
        onResponse<No> {
            furhat.say("Okay.. I understand")
            terminate()
        }

        onNoResponse {
            furhat.say("Sorry, I didn't hear you.")
            furhat.ask("Do you wanna guess on the murderer?")
        }

        onResponse<RepeatQuestion> {
            furhat.say("Of course.")
            furhat.ask("Would you like to guess who the murderer is?")
        }
    }

    fun guessMurder() : State = state(Options) {
        onEntry {
            furhat.ask("You have interviewed all the suspects. Who do you think is the murder?")
        }
        onResponse{
            if (it.text == "Francis") {
                furhat.say("That is correct! Congratulations on figuring this mystery out and on finding the murderer! ")
            }else{
                furhat.say("That is incorrect! You have used all your guesses and the game is over. But you are welcome to try the game again.")
            }
        }

        onNoResponse {
            furhat.say("Sorry, I didn't hear you.")
            furhat.ask("Who do you think is the murderer?")
        }

        onResponse<RepeatQuestion> {
            furhat.say("Of course.")
            furhat.say("You have interviewed all the suspects.")
            furhat.ask("Would you like to guess who the murderer is?")
        }
    }
}

// This class contains all the suspects, with methods containing the different interview questions
class Suspect(
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
        suspicious: String,
        var relationTracker: Int,
        var eveningTracker: Int,
        var timeOfMurderTracker: Int,
        var beforeAfterTracker: Int,
        var responsibleTracker: Int,
        var active_question: String
) {

    val initialConversation = state(Options) {
        onEntry {
            furhat.setTexture(texture)
            furhat.setVoice(Language.ENGLISH_GB, voice)
            furhat.say("Hello this is  ${"$firstName $lastName"}, i'm a $job.")
            delay(1000)
            // if we are to use onResponse RepeatQuestion, we should have furhat.listen instead of goto
            //furhat.listen()
            goto(interviewConversation)
        }
        // unnecessary here? Or do we want to be able to repeat their job?
        /*onNoResponse {
            goto(interviewConversation)
        }
        onResponse<RepeatQuestion> {
            furhat.say("Of course.")
            furhat.say("I am ${"$firstName $lastName"}, and I work as a $job.")
            goto(interviewConversation)
        }*/
    }

    val interviewConversation = state(Options) {
        onEntry {
            furhat.ask("So what questions did you have for me?")
        }
        val quickLeftGlance = defineGesture {
            frame(0.22, 0.44) {
                BasicParams.LOOK_LEFT to 0.0
            }
            frame(0.66, 0.88) {
                BasicParams.LOOK_LEFT to 0.3
            }
            frame(0.95, 1.05) {
                BasicParams.LOOK_LEFT to 0.0
            }
        }

        val quickRightGlance = defineGesture {
            frame(0.22, 0.44) {
                BasicParams.LOOK_RIGHT to 0.3
            }
            frame(0.50, 0.60) {
                BasicParams.LOOK_RIGHT to 0.0
            }
        }

        val rollEyes = defineGesture {
            frame(0.50, 0.60) {
                BasicParams.LOOK_UP to 0.3
            }
            frame(0.80, 0.90) {
                BasicParams.LOOK_UP to 0.0
            }
        }

        val nonHappyEyes = defineGesture {
            // Used with the smile-gesture to resemble non-genuine smiles
            frame(2.22, 6.44) {
                BasicParams.EYE_SQUINT_LEFT to 0.0
                BasicParams.EYE_SQUINT_RIGHT to 0.0

            }
            frame(7.00, 8.00) {
                BasicParams.EYE_SQUINT_LEFT to 0.0
                BasicParams.EYE_SQUINT_RIGHT to 0.0
            }
        }


        onResponse<QuestionRelation> {
            // to know which answer to repeat
            active_question = "relation"
            if (relationTracker > 0) {
                furhat.say("You already asked that question. But fine, I can answer again")
            }
            furhat.say("I was Albert's $relationshipAlbert for a long time.")
            relationTracker += 1 // This probably only updates the value locally, would a setter-method help?
            furhat.ask("Anything else you wonder?")
        }

        onResponse<QuestionEvening> {
            active_question = "evening"
            if (eveningTracker > 0) {
                furhat.say("You already asked that question. But fine, I can answer again")
            }

            if (firstName == "Francis"){
                furhat.gesture(Gestures.GazeAway(strength = 0.50, duration=6.00), async = true)
            }
            furhat.say(evening)


            eveningTracker += 1 // This probably only updates the value locally, would a setter-method help?
            furhat.ask("Anything else you wonder?")
        }

        onResponse<QuestionTimeOfMurder> {
            active_question = "timeOfMurder"
            if (timeOfMurderTracker > 0) {
                furhat.say("You already asked that question. But fine, I can answer again")
            }
            if (firstName == "Francis"){
                furhat.gesture(rollEyes, async=true)
                furhat.say("What a stupid question")
                furhat.gesture(Gestures.ExpressDisgust(strength = 0.3, duration = 2.50), async = true)
                furhat.gesture(Gestures.BrowFrown(strength = 0.3, duration = 2.50), async = true)
            }else{
                furhat.gesture(Gestures.Nod(strength = 0.2, duration = 0.50), async = true)
            }
            furhat.say(timeOfMurder)
            timeOfMurderTracker += 1 // This probably only updates the value locally, would a setter-method help?
            furhat.ask("Anything else you wonder?")
        }

        onResponse<QuestionBeforeAndAfter> {
            active_question = "beforeAndAfter"
            if (beforeAfterTracker > 0) {
                furhat.say("You already asked that question. But fine, I can answer again")
            }
            if (firstName == "Francis"){
                furhat.gesture(Gestures.BigSmile(strength = 0.3, duration = 8.0), async = true)
                furhat.gesture(Gestures.BrowFrown( duration = 8.0), async = true)
                furhat.gesture(nonHappyEyes, async = true)
            }
            furhat.say(beforeAndAfter)
            beforeAfterTracker += 1 // This probably only updates the value locally, would a setter-method help?
            furhat.ask("Anything else you wonder?")
        }

        onResponse<QuestionResponsible> {

            active_question = "responsible"
            if (responsibleTracker > 0) {
                furhat.say("You already asked that question. But fine, I can answer again")
            }
            if (firstName == "Francis"){
                furhat.gesture(quickRightGlance)
            }
            furhat.say(responsible)
            responsibleTracker += 1 // This probably only updates the value locally, would a setter-method help?
            random(
                    { furhat.ask("Do you have even more questions?")},
                    { furhat.ask("Anything else you wonder?")}
            )
        }

        onResponse<QuestionSuspicious> {
            active_question = "suspicious"
            if (firstName == "Francis"){
                furhat.gesture(quickLeftGlance)
            }else{
                furhat.gesture(Gestures.Nod(strength = 0.2, duration = 0.50), async = true)
            }
            furhat.say(suspicious)
            furhat.ask("Anything else you wonder?")
        }

        onResponse<No> {
            active_question = "null"
            furhat.say("Very well then")
            furhat.setTexture("male")
            furhat.setVoice(Language.ENGLISH_GB, "Brian")
            goto(ChooseToQuestion)
        }

        onNoResponse {
            furhat.say("Sorry, I didn't hear you.")
            furhat.ask("What question did you have for me?")
        }

        // active_question to remember what answer to repeat
        onResponse<RepeatQuestion> {
            furhat.say("Of course.")
            if (active_question == "relation") {
                furhat.say("I was Albert's $relationshipAlbert for a long time.")
            }
            if (active_question == "evening") {
                furhat.say(evening)
            }
            if (active_question == "timeOfMurder") {
                furhat.say(timeOfMurder)
            }
            if (active_question == "beforeAndAfter") {
                furhat.say(beforeAndAfter)
            }
            if (active_question == "responsible") {
                furhat.say(responsible)
            }
            if (active_question == "suspicious") {
                furhat.say(suspicious)
            }
            if (active_question == "null") {
                furhat.say("Which question should I repeat?")
            }
            furhat.ask("Anything else you wonder?")
        }
    }
}

class QuestionRelation: Intent() {

    override fun getExamples(lang: Language): List<String>{
        return listOf("How did you know +Albert", "How did you know +Albert Adams", "What's you relationship with +Albert")
    }
}

class QuestionEvening: Intent() {
    override fun getExamples(lang: Language): List<String>{
        return listOf("Tell me about your +evening", "Tell me about your +night")
    }
}

class QuestionTimeOfMurder: Intent() {
    override fun getExamples(lang: Language): List<String>{
        return listOf("Where were +you during the +time of the +murder", "Where were +you at the +time of the +murder", "Where were you?")
    }
}

class QuestionBeforeAndAfter: Intent() {
    override fun getExamples(lang: Language): List<String>{
        return listOf("What did you do +before", "What did you do +before and after", "What did you do +before and after the murder?", "+before", "+after")
    }
}

class QuestionResponsible: Intent() {
    override fun getExamples(lang: Language): List<String>{
        return listOf("Who is +responsible", "Who do you believe is +responsible for the murder", "Who did it?", "Who do you think did it?")
    }
}

class QuestionSuspicious: Intent() {
    override fun getExamples(lang: Language): List<String>{
        return listOf("Did you notice anything +suspicious that night?",  "Anyone who looked or acted particularly +suspicious?", "Anything +weird?", "Anything +odd?")
    }
}
