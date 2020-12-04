package furhatos.app.fruitseller.flow

import furhatos.app.fruitseller.nlu.*
import furhatos.app.fruitseller.order
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.Intent
import furhatos.nlu.common.*
import furhatos.util.Language

val Start = state(Interaction){
    onEntry {
        furhat.ask("Thank god your here detective! May I ask your name?")
    }

    onResponse{
        val username = it.text
        // The name of the person playing
        furhat.gesture(Gestures.BigSmile, async = true)
        // async = true means that the gesture does not block the following speach
        furhat.say("Ah! Detective  $username! There has been a murder!")
        furhat.gesture(Gestures.ExpressFear, async = true)
        furhat.say("We have the suspects here.")
        /*furhat.say("And we need your help to solve it. " +
                "The victim is the city millionaire, Albert Adams. He was found dead in his library. " +
                "The suspects are his wife Carol, the chemistry professor Harold and his childhood friend Francis. " +
                "They are all here ready to be questioned by you $username. ")*/
        goto(TakingOrder)
    }
}

val Options = state(Interaction) {
    onResponse<VisitName> {
        val names = it.intent.names
        if (names != null) {
            goto(OrderReceived(names))
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

val TakingOrder = state(Options) {

    onEntry {
        if(users.current.order.names.list.isEmpty()){
            furhat.ask("Who do you want to question first?")
        }else {
            furhat.ask("Who do you want to question next?")
        }
    }

    onResponse<No> {
        furhat.gesture(Gestures.ExpressAnger, async = true) // Express anger but continue execution immediately
        furhat.say("Okay, that's a shame. Guess we will never find the murder.")
        furhat.gesture(Gestures.BigSmile, async = true) // Do a smile
        furhat.say("Have a splendid day though!")
        goto(Idle)
    }
}


fun OrderReceived(names: NameList) : State = state(Options) {
    onEntry {
        furhat.say("Alright, I'll go get ${names.text}!")
        names.list.forEach {
            users.current.order.names.list.add(it)
        }
        if(names.text=="Carol") {
            goto(Suspects(
                    "Carol",
                    "Clark",
                    "Construction worker",
                    "wife",
                    false,
                    "Ursula",
                    "Amy"
                ).initialConversation)

        }else if(names.text=="Harold"){
            goto(Suspects(
                    "Harold",
                    "Hoffman",
                    "Chemistry professor",
                    "colleague",
                    false,
                    "Geremy",
                    "Brian"
            ).initialConversation)
        }else{
            goto(Suspects(
                    "Francis",
                    "Franclin",
                    "Cleaner",
                    "childhood friend",
                    true,
                    "Ted",
                    "Brian"
            ).initialConversation)
        }
        // TODO: Add different voices to the different suspects.
    }

    onReentry {
        furhat.ask("Did you want something else?")
    }

    onResponse<No> {
        furhat.say("You have so far spoken to ${users.current.order.names}. Have a great day!")
    }
}

class Suspects constructor(
        firstName: String,
        lastName: String,
        job: String,
        relationshipAlbert: String,
        guilty: Boolean,
        texture: String,
        voice: String
) {

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