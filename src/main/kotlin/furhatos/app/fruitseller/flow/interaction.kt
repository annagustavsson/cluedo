package furhatos.app.fruitseller.flow

import furhatos.app.fruitseller.nlu.*
import furhatos.app.fruitseller.order
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.*
import furhatos.util.Language

val Start = state(Interaction){
    onEntry {
        furhat.say("Thank god your here! There has been a murder, we have all the suspects here.")
        // TODO: change username to actual players name
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
        furhat.say("Okay, that's a shame. Guess we will never find the murder.")
        furhat.gesture(Gestures.ExpressAnger, async = false) // Express anger but continue execution immediately
        furhat.say("Have a splendid day though!")
        furhat.gesture(Gestures.BigSmile) // Do a smile

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
            goto(TalkToCarol)
        }else if(names.text=="Harold"){
            goto(TalkToHarold)
        }else{
            goto(TalkToFrancis)
        }
        //furhat.ask("Anything else?")
    }

    onReentry {
        furhat.ask("Did you want something else?")
    }

    onResponse<No> {
        furhat.say("You have so far spoken to ${users.current.order.names}. Have a great day!")
    }
}

val TalkToCarol = state(Options) {
    onEntry {
        furhat.setTexture("Ursula")
        furhat.setVoice(Language.ENGLISH_GB, "Amy")
        furhat.say("Hello this is Carol, I do not have time for this. Bye.")
        furhat.setTexture("male")
        furhat.setVoice(Language.ENGLISH_GB, "Brian")
        furhat.say("Yeah she's pretty rude.")
        goto(TakingOrder)
    }
}

val TalkToHarold = state(Options) {
    onEntry {
        furhat.setTexture("Geremy")
        furhat.setVoice(Language.ENGLISH_GB, "Geraint")
        furhat.say("Hello this is Harold, I'm innocent Bye.")
        furhat.setTexture("male")
        furhat.setVoice(Language.ENGLISH_GB, "Brian")
        furhat.say("Yeah he's also pretty rude.")
        goto(TakingOrder)
    }
}

val TalkToFrancis = state(Options) {
    onEntry {
        furhat.setTexture("Ted")
        furhat.setVoice(Language.ENGLISH_AU, "Russel")
        furhat.say("Hey this is Francis, I'm obviously innocent")
        furhat.gesture(Gestures.Wink)
        furhat.say("Bye!")
        furhat.setTexture("male")
        furhat.setVoice(Language.ENGLISH_GB, "Brian")
        furhat.say("Yeah he's also pretty rude.")
        goto(TakingOrder)
    }
}
