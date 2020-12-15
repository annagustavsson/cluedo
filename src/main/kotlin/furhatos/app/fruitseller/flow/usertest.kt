package furhatos.app.fruitseller.flow

import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onUserLeave
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.Intent
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.util.Language

val IdleTest : State = state {

    init {
        // Face
        furhat.setTexture("male")
        // Voice
        furhat.setVoice(Language.ENGLISH_GB, "Brian")
        if (users.count > 0) {
            furhat.attend(users.random)
            goto(StartTest)
        }
    }

    onEntry {
        if (users.count > 0) {
            furhat.attendNobody()
        }
    }

    onUserEnter {
        furhat.attend(it)
        goto(StartTest)
    }
}

val InteractionTest : State = state {

    onUserLeave(instant = true) {
        if (users.count > 0) {
            if (it == users.current) {
                furhat.attend(users.other)
                goto(StartTest)
            } else {
                furhat.glance(it)
            }
        } else {
            goto(IdleTest)
        }
    }

    onUserEnter(instant = true) {
        furhat.glance(it)
    }
}

val StartTest = state(InteractionTest){
    onEntry {
        furhat.ask("Are you ready for the user test?")
        //goto(C)
    }

    onResponse<Yes> {
        goto(ChooseTest)
    }

    onResponse<No> {
        furhat.say("Oh, that's too bad. Thank you anyways.")
        goto(IdleTest)
    }
}

val ChooseTest = state(InteractionTest){
    onEntry {
        furhat.ask("Are you going to do test A, B or C?")
    }

    onResponse<TestA> {
        furhat.say("You have chosen test A. I will ask you a couple of questions for you to answer. " +
                "The test starts right now.")
        goto(A1)
    }

    onResponse<TestB> {
        furhat.say("You have chosen test B. I will ask you a couple of questions for you to answer. " +
                "The test starts right now.")
        goto(B1)
    }

    onResponse<TestC> {
        furhat.say("You have chosen test C. I will ask you a couple of questions for you to answer. " +
                "The test starts right now.")
        goto(C)
    }
}

class TestA: Intent() {
    override fun getExamples(lang: Language): List<String>{
        return listOf("Test A", "I wanna do test A", "A", "The first", "the first one")
    }
}

class TestB: Intent() {
    override fun getExamples(lang: Language): List<String>{
        return listOf("Test B", "I wanna do test B", "B", "The second", "the second one")
    }
}

class TestC: Intent() {
    override fun getExamples(lang: Language): List<String>{
        return listOf("Test C", "I wanna do test C", "C", "The third", "the third one")
    }
}

// Test A1: furhat appear less trustworthy
val A1 = state(InteractionTest){
    onEntry {
        furhat.say("What did you have for breakfast yesterday?")
        furhat.gesture(Gestures.BigSmile(strength = 2.0, duration = 2.0))
        delay(400)
        furhat.gesture(Gestures.GazeAway)
        furhat.listen()
        //furhat.listen(timeout = 5000)
    }

    onResponse {
        delay(400)
        furhat.say("That sounds really good.")
        goto(A2)
    }
}

// Test A2: furhat appear more trustworthy
val A2 = state(InteractionTest){
    onEntry {
        furhat.say("What did you have for lunch yesterday?")
        furhat.gesture(Gestures.Smile)
        delay(400)
        furhat.gesture(Gestures.Nod)
        furhat.listen()
    }

    onResponse {
        furhat.say("That sounds really good.")
        goto(A3)
    }
}

// Test A3: furhat appear neutral
val A3 = state(InteractionTest){
    onEntry {
        furhat.say("What did you have for dinner yesterday?")
        furhat.listen()
    }

    onResponse {
        furhat.say("That sounds really good.")
        furhat.say("That was all for test A. Go ahead and fill out the form.")
        goto(IdleTest)
    }
}

// For Test B:
// Should we test both longer and shorter answers from furhat with furhat nodding/gazing?
// i.e. showing whether he is listening? That will be six combinations if we follow the document.


// Test B1: long answer from furhat
val B1 = state(InteractionTest){
    onEntry {
        furhat.ask("Tell me about your day?")
    }

    onResponse<No> {
        furhat.say("Okay then.")
        goto(B2)
    }

    onResponse {
        furhat.ask("That sounds really lovely indeed. And a lot of more babbling and so on and so forth. " +
                "But could there possible be anything else you would like to add on that note?")
    }
}

// Test B2: short answer from furhat
val B2 = state(InteractionTest) {
    onEntry {
        furhat.ask("Tell me about what you will do tomorrow?")
    }

    onResponse<No> {
        furhat.say("Okay then. You can fill out the form now.")
        goto(IdleTest)
    }

    onResponse {
        furhat.ask("That's nice. Anything else you will do?")
    }
}

// Test C: furhat with chat or furhat with face, but same code for both
val C = state(InteractionTest) {
    onEntry {
        furhat.say("What will you do later today?")
        furhat.gesture(Gestures.Nod)
        furhat.listen()
    }

    onResponse<No> {
        furhat.say("Okay then. You can fill out the form now.")
        goto(IdleTest)
    }

    onResponse {
        furhat.say("That sounds nice.")
        furhat.ask("Anything else?")
    }
}

