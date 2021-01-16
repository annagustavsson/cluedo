package furhatos.app.fruitseller.flow

import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onUserLeave
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.*
import furhatos.util.Language

val Idle : State = state {

    init {
        furhat.setTexture("male")
        furhat.setVoice(Language.ENGLISH_GB,  "Brian")
        if (users.count > 0) {
            furhat.attend(users.random)
            goto(Start)
        }
    }

    onEntry {
        if (users.count > 0) {
            furhat.attendNobody()
        }
    }

    onUserEnter {
        furhat.attend(it)
        goto(Start)
    }
}

val Interaction : State = state {

    onUserLeave(instant = true) {
        if (users.count > 0) {
            if (it == users.current) {
                furhat.attend(users.other)
                goto(Start)
            } else {
                furhat.glance(it)
            }
        } else {
            goto(Idle)
        }
    }

    onUserEnter(instant = true) {
        furhat.glance(it)
    }
}