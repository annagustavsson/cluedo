package furhatos.app.fruitseller.flow
import furhatos.app.fruitseller.nlu.RequestOptions
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.util.Language

import furhatos.app.fruitseller.nlu.Room
import furhatos.app.fruitseller.nlu.ChooseRoom
import furhatos.app.fruitseller.nlu.RoomList



val Start : State = state(Interaction) {

    onEntry {
        random(
                { furhat.say("Hi there") },
                { furhat.say("Oh, hello there") }
        )
        goto(StartCluedo)
    }
}

val StartCluedo = state {
    onEntry {
        random(
                { furhat.ask("Do you wanna play Cluedo?") },
                { furhat.ask("Would you like to play Cluedo?") }
        )
    }

    onResponse<Yes> {
        //Before this, give a background story and
        furhat.say ("Miss Scarlett, Mr. Green, Colonel Mustard, Professor Plum, Mrs. Peacock and Mrs. White" +
                " have been invited to XX house and a murder have happened. You have to find out who the murderer is" +
                " what room the the crime was committed in and what weapon the murder used")
        random(
                { furhat.ask("Which room do you wanna enter?") },
                { furhat.ask("What room do you wanna go to?") }
        )
    }

    onResponse<No> {
        furhat.say("Okay, that's a shame. Have a splendid day!")
        goto(Idle)
    }

    onResponse<ChooseRoom> {
        val rooms = it.intent.rooms
        println("Message 1")
        if (rooms != null) {
            furhat.say("${rooms.text}, I hear you!")
            goto(OrderReceived(rooms))
        }
        else {
            propagate()
        }
    }
}

// come up with more inuitive name function name
fun OrderReceived(rooms: RoomList) : State = state {
    println("Message 2")
    onEntry {
        furhat.say("${rooms.text}, what a lovely choice!")
        rooms.list.forEach {
            users.current.order.rooms.list.add(it) // change in users.kt from "order" to smh intuitive
        }
        furhat.ask("Anything else?")
    }

    onReentry {
        furhat.ask("Did you want something else?")
    }

    onResponse<ChooseRoom> {
        //val fruits = it.intent.fruits
        val rooms = it.intent.rooms
        if (rooms != null) {
            goto(OrderReceived(rooms))
        }
        else {
            propagate()
        }
    }

    onResponse<RequestOptions> {
        //furhat.say("We have ${Fruit().getEnum(Language.ENGLISH_US).joinToString(", ")}")
        furhat.say("We have ${Room().getEnum(Language.ENGLISH_US).joinToString(", ")}")
        furhat.ask("Which do you wanna enter?")
    }

    onResponse<Yes> {
        random(
                { furhat.ask("What kind of fruit do you want?") },
                { furhat.ask("What type of fruit?") }
        )
    }

    onResponse<No> {
        furhat.say("Okay, here is your order of ${users.current.order.rooms}.")
        goto(Confirmation)
    }
}

val Options = state(Interaction) {
    onResponse<ChooseRoom> {
        //val fruits = it.intent.fruits
        val rooms = it.intent.rooms
        if (rooms != null) {
            goto(OrderReceived(rooms))
        }
        else {
            propagate()
        }
    }

    onResponse<RequestOptions> {
        furhat.say("We have ${Room().getEnum(Language.ENGLISH_US).joinToString(", ")}")
        furhat.ask("Do you want some?")
    }

    onResponse<Yes> {
        random(
                { furhat.ask("What kind of fruit do you want?") },
                { furhat.ask("What type of fruit?") }
        )
    }
}

val Confirmation = state(Interaction) {
    onEntry {
        furhat.ask("Do you wanna enter this room?")
    }

    onResponse<Yes> {
        furhat.say("That is fantastic!")
        goto(Idle)
    }

    onResponse<No> {
        furhat.say("That's not good. Let's try again")
        //users.current.order.fruits.list.clear()
        users.current.order.rooms.list.clear()
        goto(StartCluedo)
    }
}


