package furhatos.app.fruitseller.flow

import furhatos.app.fruitseller.nlu.*
import furhatos.app.fruitseller.order
import furhatos.app.fruitseller.suspect1
import furhatos.app.fruitseller.suspect2
import furhatos.app.fruitseller.suspect3
import furhatos.flow.kotlin.*
import furhatos.gestures.BasicParams
import furhatos.gestures.Gestures
import furhatos.gestures.defineGesture
import furhatos.nlu.common.*
import furhatos.records.User
import furhatos.util.Language
import java.util.Arrays

var autopsyInformation = false

val Start = state(Interaction){

    onEntry {
        furhat.gesture(Gestures.BigSmile, async = true)
        furhat.say("Thank god you're here detective!")
        delay(500)
        furhat.ask("What is your name?")
    }

    onResponse{
        furhat.gesture(Gestures.BigSmile, async = true)
        furhat.say("Good to meet you detective!")
        furhat.gesture(Gestures.ExpressFear(strength = 0.2), async = true)
        furhat.say("There has been a murder. And we need your help to solve it. " +
                "I'm Officer Furhat, and I'm here to assist you. The victim of this murder mystery is the city millionaire, " +
                "Albert Adams. He was found dead in his library. The suspects are his wife Carol, the chemistry professor " +
                "Harold and his childhood friend Francis. They are all here ready to be questioned by you Keep in mind that " +
                "you can guess on the murderer only once.")
        goto(ChooseToQuestion)
    }

    onNoResponse {
        furhat.say("Sorry, I didn't hear you.")
        furhat.ask("What is your name, detective?")
    }
}

val Options = state(Interaction) {
    onResponse<VisitName> {

        val names = it.intent.names
        if (names != null) {
            goto(getSuspect(names))
        }
        else {
            propagate()
        }
    }

    onResponse<RequestOptions> {
        furhat.say("You may speak to ${Name().optionsToText()}")
        furhat.ask("Who do you pick?")
    }
}

val ChooseToQuestion = state(Options) {
    onEntry {
        if (users.current.order.names.list.size == 2 && !autopsyInformation) {
                autopsyInformation = true
                call(GamePlay().autopsyResults)
        }

        when {
            users.current.order.names.list.isEmpty() -> {
                furhat.ask("Who do you want to question first?")
            }
            users.current.order.names.list.size == 1 || users.current.order.names.list.size == 2 -> {
            call(GamePlay().chooseToGuess())
                furhat.ask("Who do you want to question next?")

            }
            users.current.order.names.list.size == 3 -> {
                furhat.say("You have now interviewed all the suspects")
                //goto(GamePlay().guessMurder())
                call(GamePlay().chooseToGuess())
                furhat.ask("Who do you want to interview again?")
            }

        }
    }

    onResponse<No> {
        furhat.gesture(Gestures.ExpressAnger, async = true) // Express anger but continue execution immediately
        furhat.say("Okay, that's a shame. Guess we will never find the murder.")
        furhat.gesture(Gestures.BigSmile, async = true) // Do a smile
        furhat.say("Have a splendid day though!")
        goto(Idle)
    }

    onNoResponse {
        furhat.say("Sorry, I didn't hear you.")
        furhat.ask("Who do you want to question?")
    }

    onResponse<RepeatQuestion> {
        furhat.say("Of course.")
        furhat.ask("I wonder who you would like to question?")
    }
}

fun getSuspect(names: NameList) : State = state(Options) {
    onEntry {
        furhat.say("Alright, I'll go get ${names.text}!")

        val currentSuspectName : String = names.text
        var suspectFound = false

        for (suspect in users.current.order.names.list) {
            val suspect = suspect.toString()
            if (suspect == currentSuspectName) {
                suspectFound = true
                break
            }
        }

        if (!suspectFound) {
            names.list.forEach {
                users.current.order.names.list.add(it)
                println("suspect not interviewed already, added to list")
            }
        }

        when (names.text) {
            "Carol" -> {
                goto(users.current.suspect1.initialConversation)
            }
            "Harold" -> {
                goto(users.current.suspect2.initialConversation)
            }
            else -> {
                goto(users.current.suspect3.initialConversation)
            }
        }
    }

    onReentry {
        furhat.ask("Did you want something else?")
    }

    onResponse<No> {
        furhat.say("You have so far spoken to ${users.current.order.names}. Have a great day!")
    }

    onNoResponse {
        furhat.say("Sorry, I didn't hear you.")
        furhat.ask("Did you want something else?")
    }

    onResponse<RepeatQuestion> {
        furhat.say("Of course.")
        furhat.ask("Did you want something else?")
    }
}