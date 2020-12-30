package furhatos.app.fruitseller.flow

import furhatos.app.fruitseller.nlu.*
import furhatos.app.fruitseller.order
import furhatos.app.fruitseller.suspect1
import furhatos.app.fruitseller.suspect2
import furhatos.app.fruitseller.suspect3
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.*
import furhatos.records.User
import java.util.Arrays


var autopsyInformation = false



val Start = state(Interaction){
    println("Anna02")
    onEntry {
        furhat.ask("Thank god you're here detective! What is your name?")
    }

    onResponse{
        println("Anna03")
        val username = it.text
        // The name of the person playing
        // TODO: Save username variable at a better place. In GamePlay-class? In users.kt?
        furhat.gesture(Gestures.BigSmile, async = true)
        // async = true means that the gesture does not block the following speech
        furhat.say("Detective  $username! There has been a murder!")
        furhat.gesture(Gestures.ExpressFear, async = true)
        furhat.say("And we need your help to solve it. " +
                "The victim is the city millionaire, Albert Adams. He was found dead in his library. " +
                "The suspects are his wife Carol, the chemistry professor Harold and his childhood friend Francis. " +
                "They are all here ready to be questioned by you.")
        furhat.say("Keep in mind that you can guess on the murderer only once.")
        goto(TakingOrder)
    }

    onNoResponse {
        furhat.say("Sorry, I didn't hear you.")
        furhat.ask("What is your name, detective?")
    }
}

val Options = state(Interaction) {
    onResponse<VisitName> {
        println("Anna01")
        val names = it.intent.names
        if (names != null) {
            goto(orderReceived(names))
        }
        else {
            propagate()
        }
    }

    // If the user requests to hear the options again.
    onResponse<RequestOptions> {
        furhat.say("You may speak to ${Name().optionsToText()}")
        furhat.ask("Who do you pick?")
    }
}

val TakingOrder = state(Options) {

    onEntry {
        println("Anna04")
        if (users.current.order.names.list.size == 2 && !autopsyInformation) {
                autopsyInformation = true
                call(GamePlay().autopsyResults) // note: 'call' instead of 'goto'
        }

        when {
            users.current.order.names.list.isEmpty() -> {
                furhat.ask("Who do you want to question first?")
            }

            users.current.order.names.list.size == 1 || users.current.order.names.list.size == 2 || users.current.order.names.list.size == 3 -> {
                call(GamePlay().chooseToGuess())
                furhat.ask("Who do you want to question next?")

            }

            //users.current.order.names.list.size == 3 -> {
                //goto(GamePlay().guessMurder())
            //    call(GamePlay().chooseToGuess())
             //   furhat.ask("Who do you want to question next?")
            //}

        }
    }

    onResponse<No> {
        furhat.gesture(Gestures.ExpressAnger, async = true) // Express anger but continue execution immediately
        furhat.say("Okay, that's a shame. Guess we will never find the murder.")
        furhat.gesture(Gestures.BigSmile, async = true) // Do a smile
        furhat.say("Have a splendid day though!")
        goto(Idle)
    }

    //passar detta???
    onNoResponse {
        furhat.say("Sorry, I didn't hear you.")
        furhat.ask("Who do you want to question?")
    }

}

fun orderReceived(names: NameList) : State = state(Options) {
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
            }
        }


        // TODO: The creation of the suspects objects should probably be created somewhere else. And only called on here:
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
        // TODO: Fix voices to the different suspects (Currently they all have language: English_GB,
        //  also Francis is a girl and Harold has the same voice as FurHat.)
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
}