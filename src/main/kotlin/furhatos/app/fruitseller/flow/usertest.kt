package furhatos.app.fruitseller.flow

import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onUserLeave
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.Parameters.noSpeechTimeout
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
        furhat.ask("Are you ready for the user test?")
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
        furhat.say("You have chosen test A. In this test, you will listen to me say a few sentences." +
                " After each interaction, you will have time to answer a few questions in a form. The test starts right now.")
        goto(Ai)
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

// Test Aa-Ae Furhat appear less trustworthy
val Aa = state(InteractionTest){
    // 1a. More facial gestures, a lot of gazing and glancing

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

    onEntry {
        furhat.say("interaction 7")
        delay(2000)

        furhat.gesture(Gestures.Surprise(strength = 4.00, duration=6.00), async = true)
        furhat.say("Hey I am so glad to see you detective!")
        furhat.gesture(Gestures.Blink(duration=0.5), async=true)
        furhat.gesture(quickRightGlance)
        furhat.gesture(Gestures.Blink(duration=0.5), async=true)
        furhat.say("You are asking what I did at the night of the murder?")
        furhat.gesture(quickLeftGlance)
        furhat.gesture(Gestures.Blink(duration=0.1), async=true)
        furhat.gesture(Gestures.Blink(duration=0.1), async=true)
        furhat.gesture(Gestures.Blink(duration=0.1), async=true)
        furhat.gesture(quickRightGlance)
        furhat.say(" I was at home.")
        furhat.gesture(Gestures.Blink(duration=0.4), async=true)
        furhat.say("I went to bed around 11. ")
        furhat.gesture(Gestures.GazeAway, async = true)
        furhat.say(" I was a close friend of Albert, I would never hurt him.")
        furhat.gesture(quickRightGlance)
        furhat.gesture(Gestures.Blink(duration=0.5), async=true)
        furhat.say(" Trust me.")

        delay(3000)
        furhat.ask("You can fill in the form for interaction: 7, now. When you are done say: Yes. ", timeout = 120000)

    }


    onResponse<Yes> {
        goto(Af)
    }

    onResponse {// catches answers that are not "Yes"
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 7, now. When you are done say: Yes. ", timeout = 120000)
    }

    onNoResponse { // Catches silence
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 7, now. When you are done say: Yes. ", timeout = 120000)
    }
}

val Ab = state(InteractionTest){
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
        delay(2000)
        furhat.say("interaction 4")
        delay(2000)

        furhat.gesture(Gestures.BigSmile(strength = 2.0, duration=3.00), async = true)
        furhat.gesture(nonHappyEyes)
        furhat.gesture(Gestures.BrowFrown)
        furhat.say("Hey I am so glad to see you detective!")
        furhat.gesture(Gestures.Smile(strength = 1.00, duration = 4.0), async = true)
        furhat.gesture(Gestures.BrowFrown)
        furhat.say("You are asking what I did at the night of the murder? I was at hame. ")
        furhat.gesture(Gestures.BrowFrown(strength = 4.0, duration = 5.0), async = true)
        furhat.gesture(Gestures.BigSmile(duration = 5.0), async = true)
        furhat.gesture(nonHappyEyes)
        furhat.say("Hahaha I mean at home, I was at home. I was a close friend of Albert, I would never hurt him.")
        furhat.gesture(Gestures.BigSmile(strength = 2.0, duration=3.00), async = true)
        furhat.gesture(nonHappyEyes)
        furhat.gesture(Gestures.BrowFrown)
        furhat.say("Trust me")

        delay(3000)
        furhat.ask("You can fill in the form for interaction: 4, now. When you are done say: Yes. ", timeout = 120000)

    }


    onResponse<Yes> {
        goto(Aj)
    }

    onResponse {
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 4, now. When you are done say: Yes. ", timeout = 120000)
    }

    onNoResponse { // Catches silence
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 4, now. When you are done say: Yes. ", timeout = 120000)
    }
}

val Ac = state(InteractionTest){
    // 1c Total lack of smile
    val halfClosedEyes = defineGesture {
        frame(0.22, 5.44) {
            BasicParams.BLINK_RIGHT to 0.25
            BasicParams.BLINK_LEFT to 0.25
        }
        frame(5.50, 5.60) {
            BasicParams.BLINK_RIGHT to 0.0
            BasicParams.BLINK_LEFT to 0.0

        }
    }

    onEntry {
        delay(2000)
        furhat.say("interaction 10")
        delay(2000)

        furhat.gesture(halfClosedEyes, async = true)
        furhat.say("Hey I am so glad to see you detective!")
        furhat.gesture(halfClosedEyes, async = true)
        furhat.say("You are asking what I did at the night of the murder? I was at home. ")
        furhat.gesture(halfClosedEyes, async = true)
        furhat.say("I went to bed around 11" + "I was a close friend of Albert")
        furhat.gesture(halfClosedEyes, async = true)
        furhat.say("I would never hurt him. Trust me.")
        delay(3000)
        furhat.ask("You can fill in the form for interaction: 10, now. When you are done say: Yes. ", timeout = 120000)

    }


    onResponse<Yes> {
        goto(IdleTest)
    }

    onResponse {
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 10, now. When you are done say: Yes. ", timeout = 120000)
    }

    onNoResponse { // Catches silence
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 10, now. When you are done say: Yes. ", timeout = 120000)
    }
}

val Ad = state(InteractionTest){
    // 1d Displays a feeling of contempt.

    val rollEyes = defineGesture {
        frame(0.50, 0.60) {
            BasicParams.LOOK_UP to 0.5
        }
        frame(0.80, 0.90) {
            BasicParams.LOOK_UP to 0.0
        }
    }

    val resetFace = defineGesture {
        reset(0.00)
    }

    onEntry {
        delay(2000)
        furhat.say("interaction 2")
        delay(2000)

        furhat.say("Hey I am so glad to see you detective!")
        furhat.gesture(Gestures.ExpressDisgust(strength = 0.5, duration = 2.50), async = true)
        furhat.gesture(Gestures.BrowFrown(strength = 0.30, duration = 2.50), async = true)
        furhat.say("You are asking what I did at the night of the murder? I was at home. I went to bed around 11. If I knew Albert?")
        furhat.gesture(rollEyes, async=true)
        furhat.say("Yeah, yeah I knew him.")
        furhat.gesture(Gestures.ExpressDisgust(duration = 2.00), async = true)
        furhat.gesture(Gestures.BrowFrown(strength = 0.60, duration = 2.00), async = true)
        furhat.say(" I was a close friend of Albert. I would never hurt him. Trust me.")
        furhat.gesture(resetFace)

        delay(3000)
        furhat.ask("You can fill in the form for interaction: 2, now. When you are done say: Yes. ", timeout = 120000)

    }


    onResponse<Yes> {
        goto(Ah)
    }

    onResponse {
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 2, now. When you are done say: Yes. ", timeout = 120000)
    }
    onNoResponse { // Catches silence
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 2, now. When you are done say: Yes. ", timeout = 120000)
    }
}

val Ae = state(InteractionTest){
    // 1e Babbles a lot, and uses phrases such as hmm, errr.
    onEntry {
        delay(2000)
        furhat.say("interaction 9")
        delay(2000)

        furhat.say("Hey I'm so glad to see you detective. Yes sorry what's that? You are asking what I did at the night of the murder? yes yes I can answer that. I was at home. " +
                "And then I went to bed around 11 I think yes it must have been 11. I would never hurt him. I was his friend. We were really close, yes. " +
                "I was a close friend of his, I would never hurt him. Never hurt Albert. I wouldn't! Trust me.")

        delay(3000)
        furhat.ask("You can fill in the form for interaction: 9, now. When you are done say: Yes. ", timeout = 120000)

    }

    onResponse<Yes> {
        goto(Ac)
    }

    onResponse {
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 9, now. When you are done say: Yes. ", timeout = 120000)
    }
    onNoResponse { // Catches silence
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 9, now. When you are done say: Yes. ", timeout = 120000)
    }
}

// Test Af-Ai Furhat appear less trustworthy

val Af = state(InteractionTest){
    // 1f. Strong emotional expressivity, that correlates to what they are saying.
    onEntry {
        delay(2000)
        furhat.say("interaction 8")
        delay(2000)

        furhat.gesture(Gestures.BigSmile(strength = 2.00), async = true)
        furhat.say("Hey I am so glad to see you detective!")
        furhat.gesture(Gestures.ExpressFear(strength = 0.40), async = true)
        furhat.say("You are asking what I did at the night of the murder?")
        furhat.gesture(Gestures.Thoughtful(duration = 2.00), async = true)
        furhat.say("I was at home. I went to bed around 11. I was a close friend of Albert, I would never hurt him.")
        furhat.gesture(Gestures.BrowFrown(strength = 2.00), async = true)
        furhat.say(" Trust me.")

        delay(3000)
        furhat.ask("You can fill in the form for interaction: 8, now. When you are done say: Yes. ", timeout = 120000)

    }


    onResponse<Yes> {
        goto(Ae)
    }

    onResponse {
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 8, now. When you are done say: Yes. ", timeout = 120000)
    }
    onNoResponse { // Catches silence
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 8, now. When you are done say: Yes. ", timeout = 120000)
    }
}

val Ag = state(InteractionTest){
    // 1g. Genuine smiles.
    onEntry {
        delay(2000)
        furhat.say("interaction 6")
        delay(2000)

        furhat.gesture(Gestures.BigSmile(strength = 2.0, duration = 2.0), async = true)
        furhat.say("Hey I am so glad to see you detective!")
        furhat.gesture(Gestures.Thoughtful(duration = 10.00))
        furhat.say("You are asking what I did at the night of the murder? I was at home. " +
                "I went to bed around 11. I was a close friend of Albert, I would never hurt him.")
        furhat.gesture(Gestures.ExpressSad, async = true)
        furhat.say("Trust me.")

        delay(3000)
        furhat.ask("You can fill in the form for interaction: 6, now. When you are done say: Yes. ", timeout = 120000)

    }


    onResponse<Yes> {
        goto(Aa)
    }

    onResponse {
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 6, now. When you are done say: Yes. ", timeout = 120000)
    }
    onNoResponse { // Catches silence
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 6, now. When you are done say: Yes. ", timeout = 120000)
    }
}

val Ah = state(InteractionTest) {
    // 1h. A lot of nodding and head shaking when listening.
    onEntry {
        delay(2000)
        furhat.say("interaction 3")
        delay(2000)

        furhat.say("Hey I am so glad to see you detective!")
        furhat.gesture(Gestures.Nod(strength = 0.4, duration = 2.00), async = true)
        furhat.say("You are asking what I did at the night of the murder?")
        furhat.gesture(Gestures.Nod(strength = 0.4, duration = 2.00), async = true)
        furhat.say("Ok yes I can answer that. I was at home. " +
                "I went to bed around 11. I was a close friend of Albert, I would never hurt him. Trust me.")

        delay(3000)
        furhat.ask("You can fill in the form for interaction: 3, now. When you are done say: Yes. ", timeout = 120000)

    }


    onResponse<Yes> {
        goto(Ab)
    }

    onResponse {
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 3, now. When you are done say: Yes. ", timeout = 120000)
    }

    onNoResponse { // Catches silence
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 3, now. When you are done say: Yes. ", timeout = 120000)
    }
}

val Ai = state(InteractionTest){
    // 1i. Fewer head gestures (no glancing or gazing) shows attention and närvaro
    onEntry {
        delay(2000)
        furhat.say("interaction 1")
        delay(2000)

        furhat.gesture(Gestures.BrowRaise(duration = 10.00))
        furhat.say("Hey I am so glad to see you detective!")
        furhat.say("You are asking what I did at the night of the murder? I was at home. " +
                "I went to bed around 11. I was a close friend of Albert, I would never hurt him. Trust me.")

        delay(3000)
        furhat.ask("You can fill in the form for interaction: 1, now. When you are done say: Yes. ", timeout = 120000)

    }


    onResponse<Yes> {
        goto(Ad)
    }

    onResponse {
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 1, now. When you are done say: Yes. ", timeout = 120000)
    }
    onNoResponse { // Catches silence
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 1, now. When you are done say: Yes. ", timeout = 120000)
    }
}



val Aj = state(InteractionTest){
    // 1j. Neutral face.
    onEntry {
        delay(2000)
        furhat.say("interaction 5")
        delay(2000)

        furhat.say("Hey I am so glad to see you detective!")
        furhat.say("You are asking what I did at the night of the murder? I was at home. " +
                "I went to bed around 11. I was a close friend of Albert, I would never hurt him. Trust me.")

        delay(3000)
        furhat.ask("You can fill in the form for interaction: 5, now. When you are done say: Yes. ", timeout = 120000)

    }


    onResponse<Yes> {
        goto(Ag)
    }

    onResponse {
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 5, now. When you are done say: Yes. ", timeout = 120000)
    }
    onNoResponse { // Catches silence
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for interaction: 5, now. When you are done say: Yes. ", timeout = 120000)
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

