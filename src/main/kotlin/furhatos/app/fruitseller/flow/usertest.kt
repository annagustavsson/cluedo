package furhatos.app.fruitseller.flow

import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onUserLeave
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.*
import furhatos.gestures.BasicParams
import furhatos.gestures.Gestures
import furhatos.gestures.defineGesture
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
        //furhat.ask("Are you ready for the user test?") // Kommentera tillbaka
        //goto(C)
        goto(A1) // ta bort
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
        furhat.say("You have chosen test A. I will talk for a bit, then you have some time to fill in in form. " +
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

// Test A1-A5 Furhat appear less trustworthy

val A1 = state(InteractionTest){

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
            BasicParams.LOOK_RIGHT to 0.6
        }
        frame(0.50, 0.60) {
            BasicParams.LOOK_RIGHT to 0.0
        }
    }

    // 1a. More facial gestures, a lot of gazing and glancing
    onEntry {
        furhat.gesture(Gestures.Surprise(strength = 4.00, duration=6.00), async = true)
        furhat.gesture(Gestures.Blink(duration=0.5), async=true)
        furhat.gesture(quickRightGlance)
        furhat.gesture(Gestures.Blink(duration=0.5), async=true)
        furhat.say("You are asking what I did at the night of the murder?")
        furhat.gesture(quickLeftGlance)
        furhat.gesture(Gestures.Blink(duration=0.1), async=true)
        furhat.gesture(Gestures.Blink(duration=0.1), async=true)
        furhat.gesture(Gestures.Blink(duration=0.1), async=true)
        furhat.gesture(quickRightGlance)
        furhat.say(" I was at home. I went to bed around 11. ")
        furhat.gesture(Gestures.GazeAway, async = true)
        furhat.say(" I was a close friend of Albert, I would never hurt him.")
        furhat.gesture(quickRightGlance)
        furhat.say(" Trust me.")
        furhat.gesture(Gestures.Blink(duration=0.5), async=true)
        furhat.say(" I'm innocent. ")
        delay(4000) // TODO: Change to listens for "yes" bf continuing
        goto(A2)
    }

    onResponse<Yes> {
        // goto(A2) TODO: add
    }
}

val A2 = state(InteractionTest){
    // 1b Non-genuine smiles



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


    onEntry {
        furhat.gesture(Gestures.Smile(strength = 2.00), async = true)
        furhat.gesture(Gestures.BrowFrown)
        furhat.gesture(nonHappyEyes)

        furhat.say("Hey I am so glad to see you detective!")
        furhat.gesture(Gestures.Smile(strength = 1.00, duration = 4.0), async = true)
        furhat.gesture(Gestures.BrowFrown)
        furhat.say("You are asking what I did at the night of the murder? I was at hame. ")
        furhat.gesture(Gestures.BrowFrown(strength = 4.0, duration = 5.0), async = true)
        furhat.gesture(Gestures.BigSmile(duration = 5.0), async = true)
        furhat.gesture(nonHappyEyes)
        furhat.say("Hahaha I mean at home, I was at home. I was a close friend of Albert, I would never hurt him.")
        furhat.gesture(Gestures.Smile(strength = 2.0, duration=3.00), async = true)
        furhat.gesture(nonHappyEyes)
        furhat.gesture(Gestures.BrowFrown)
        furhat.say("Trust me")
        delay(4000) // TODO: Change to listens for "yes" bf continuing
        goto(A3)
    }

    onResponse<Yes> {
        // goto(A2) TODO: add
    }
}

val A3 = state(InteractionTest){
    // 1c Total lack of smile
    val halfClosedEyes = defineGesture {
        frame(0.22, 3.44) {
            BasicParams.BLINK_RIGHT to 0.2
            BasicParams.BLINK_LEFT to 0.2
        }
        frame(3.50, 3.60) {
            BasicParams.BLINK_RIGHT to 0.0
            BasicParams.BLINK_LEFT to 0.0

        }
    }


    onEntry {
        furhat.gesture(halfClosedEyes)
        furhat.say("Hey I am so glad to see you detective! You are asking what I did at the night of the murder? I was at home. " +
                "I went to bed around 11" +
                "I was a close friend of Albert, I would never hurt him. Trust me.")
        delay(4000) // TODO: Change to listens for "yes" bf continuing
        goto(A4)
    }

    onResponse<Yes> {
        // goto(A2) TODO: add
    }
}

val A4 = state(InteractionTest){
    // 1d Displays a feeling of contempt.

    val rollEyes = defineGesture {
        frame(0.22, 0.44) {
            BasicParams.LOOK_UP to 1.0
        }
        frame(0.50, 0.60) {
            BasicParams.LOOK_UP to 0.0
        }
    }

    onEntry {
        furhat.gesture(Gestures.ExpressDisgust, async = true)
        furhat.gesture(Gestures.BrowFrown(strength = 2.00))
        furhat.say("You are asking what I did at the night of the murder? I was at home. ")
        furhat.gesture(Gestures.ExpressDisgust, async = true)
        furhat.gesture(Gestures.BrowFrown(strength = 1.00))
        furhat.gesture(rollEyes)
        furhat.say("I went to bet around 11. I was a close friend of Albert I would never hurt him. Trust me.")
        delay(4000) // TODO: Change to listens for "yes" bf continuing
        goto(A5)
    }

    onResponse<Yes> {
        // goto(A2) TODO: add
    }
}

val A5 = state(InteractionTest){
    // 1e Babbles a lot, and uses phrases such as hmm, errr.
    onEntry {
        furhat.say("Yes sorry what's that? You are asking what I did at the night of the murder? yes yes I can answer that.  mmm I was at home. " +
                "eee I went to bed around eeee 11 I think, eee yeah 11 Eee I was his friend. I was like really close, yes a" +
                " close friend of him, Albert that is, I would never hurt him. Never hurt Albert. I wouldn't! Trust me.")
        delay(4000) // TODO: Change to listens for "yes" bf continuing
        goto(A6)
    }

    onResponse<Yes> {
        // goto(A2) TODO: add
    }
}

// Test A6-A9 Furhat appear less trustworthy

val A6 = state(InteractionTest){
    // 1f. Strong emotional expressivity, that correlates to what they are saying.
    onEntry {
        furhat.gesture(Gestures.Smile(strength = 0.8), async = true)
        furhat.say("Hey I am so glad to see you detective!")
        furhat.gesture(Gestures.ExpressSad(strength=5.00, duration=10.00), async = true)
        furhat.say("You are asking what I did at the night of the murder? I was at home. " +
                "I went to bet around 11. I was a close friend of Albert, I would never hurt him.")
        furhat.gesture(Gestures.BrowFrown(strength = 3.00), async = true)
        furhat.say(" Trust me.")
        delay(4000) // TODO: Change to listens for "yes" bf continuing
        goto(A7)
    }

    onResponse<Yes> {
        // goto(A2) TODO: add
    }
}

val A7 = state(InteractionTest){
    // 1g. Genuine smiles.
    onEntry {
        furhat.gesture(Gestures.BigSmile(strength = 2.0, duration = 2.0), async = true)
        furhat.say("Hey! Detective! I am so glad to see you! It's been a crazy morning ")
        furhat.gesture(Gestures.Thoughtful(duration = 10.00))
        furhat.say("You are asking what I did at the night of the murder? I was at home. " +
                "I went to bet around 11. I was a close friend of Albert, I would never hurt him.")
        furhat.gesture(Gestures.ExpressSad, async = true)
        furhat.say("Trust me.")
        delay(4000) // TODO: Change to listens for "yes" bf continuing
        goto(A8)
    }

    onResponse<Yes> {
        // goto(A2) TODO: add
    }
}

val A8 = state(InteractionTest){
    // 1h. A lot of nodding and head shaking when listening.
    onEntry {
        furhat.gesture(Gestures.Nod(strength = 0.4, duration=2.00), async = true)
        furhat.say("You are asking what I did at the night of the murder? Ok yes I can answer that. I was at home. " +
                "I went to bet around 11. I was a close friend of Albert, I would never hurt him. Trust me.")
        delay(4000) // TODO: Change to listens for "yes" bf continuing
        goto(A9)
    }

    onResponse<Yes> {
        // goto(A2) TODO: add
    }
}

val A9 = state(InteractionTest){
    // 1i. Fewer head gestures (no glancing or gazing) shows attention and närvaro
    onEntry {
        furhat.gesture(Gestures.BrowRaise(duration = 10.00))
        furhat.say("You are asking what I did at the night of the murder? I was at home. " +
                "I went to bet around 11. I was a close friend of Albert, I would never hurt him. Trust me.")
        delay(4000) // TODO: Change to listens for "yes" bf continuing
        goto(A10)
    }

    onResponse<Yes> {
        // goto(A2) TODO: add
    }
}

val A10 = state(InteractionTest){
    // 1j. Furhat does not pay any attention
    onEntry {
        furhat.gesture(Gestures.BigSmile(strength = 2.0, duration = 10.0), async = true)
        furhat.gesture(Gestures.GazeAway, async = true)
        furhat.say("You are asking what I did at the night of the murder? I was at home. " +
                "I went to bet around 11. I was a close friend of Albert, I would never hurt him. Trust me.")
        delay(4000) // TODO: Change to listens for "yes" bf continuing
        goto(A11)
    }

    onResponse<Yes> {
        // goto(A2) TODO: add
    }
}

val A11 = state(InteractionTest){
    // 1k. . Neutral face.
    onEntry {
        furhat.gesture(Gestures.BigSmile(strength = 2.0, duration = 2.0), async = true)
        furhat.gesture(Gestures.GazeAway, async = true)
        furhat.say("You are asking what I did at the night of the murder? I was at home. " +
                "I went to bet around 11. I was a close friend of Albert, I would never hurt him. Trust me.")
        delay(100) // TODO: Change to listens for "yes" bf continuing
        goto(IdleTest)

    }

    onResponse<Yes> {
        // goto(IdleTest) TODO: add
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

