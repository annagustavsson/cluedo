package furhatos.app.fruitseller.flow

import furhatos.app.fruitseller.nlu.*
import furhatos.app.fruitseller.order
import furhatos.flow.kotlin.*
import furhatos.nlu.common.*

val Start = state(Interaction) {
    onEntry {
        random(
                {   furhat.say("Hi there") },
                {   furhat.say("Oh, hello there") }
        )

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

    onResponse<Yes> {
        random(
                { furhat.ask("Who do you wish to talk to?") },
                { furhat.ask("Which suspect do you wanna talk to?") }
        )
    }
}

val TakingOrder = state(Options) {
    onEntry {
        random(
                { furhat.ask("Do you want to play Cluedo?") },
                { furhat.ask("Do you wanna play a mystery game?") }
        )
    }

    onResponse<No> {
        furhat.say("Okay, that's a shame. Have a splendid day!")
        goto(Idle)
    }
}

fun OrderReceived(names: NameList) : State = state(Options) {
    onEntry {
        furhat.say("Alright, I'll go get ${names.text}!")
        names.list.forEach {
            users.current.order.names.list.add(it)
        }
        furhat.ask("Anything else?")
    }

    onReentry {
        furhat.ask("Did you want something else?")
    }

    onResponse<No> {
        furhat.say("You have so far spoken to ${users.current.order.names}. Have a great day!")
    }
}
