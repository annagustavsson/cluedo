package furhatos.app.fruitseller.flow

import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onUserLeave
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.gestures.BasicParams
import furhatos.gestures.defineGesture
import furhatos.nlu.Intent
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.util.Language
import java.util.*

val IdleTest : State = state {

    init {
        // Face
        furhat.setTexture("male")
        // Voice
        furhat.setVoice(Language.ENGLISH_GB, "Brian")
        if (users.count > 0) {
            furhat.attend(users.random)
            goto(ChooseTest)
        }
    }

    onEntry {
        if (users.count > 0) {
            furhat.attendNobody()
        }
    }

    onUserEnter {
        furhat.attend(it)
        goto(ChooseTest)
    }
}

val InteractionTest : State = state {

    onUserLeave(instant = true) {
        if (users.count > 0) {
            if (it == users.current) {
                furhat.attend(users.other)
                goto(ChooseTest)
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


val tests = (1..3).shuffled()

object UserTestSet {

    var count : Int = 0
    var current: Int = tests[Random().nextInt(tests.lastIndex)]

    fun next() {
        count++
        if (count >= tests.size)
            count = 0
        current = tests[count]
    }
}

val ChooseTest = state(InteractionTest) {

    onEntry {
        println(UserTestSet.next())
        println(UserTestSet.current)

        if (UserTestSet.current == 1) {
            furhat.say("You will now do test A.")
            delay(1500)
            furhat.say(" In this test, you will listen to me say a few sentences.")
            delay(1000)
            furhat.say(" After each interaction, you will have time to answer a few questions in form A.")
            delay(1000)
            furhat.say("The test starts right now.")
            delay(4000)
            goto(Ai)
        } else if (UserTestSet.current == 2) {
            furhat.say("You will now do test B.")
            delay(1500)
            furhat.say(" In this test, you will listen to me say a few sentences.")
            delay(1000)
            furhat.say(" After each sentence, you will fill in the corresponding section in form B.")
            delay(1000)
            furhat.say("The test starts right now.")
            delay(4000)
            goto(B)
        } else if (UserTestSet.current == 3){
            furhat.say("You will now do test C.")
            delay(1500)
            furhat.say(" In this test, I will ask a couple of questions for you to answer.")
            delay(1000)
            furhat.say(" Afterwards, I will ask you to fill in form C.")
            delay(1000)
            furhat.say("The test starts right now.")
            delay(4000)
            goto(C)
        } else {
            furhat.say("thank you for your participation.")
        }
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
        delay(6000)
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

object SentenceSet {

    var count : Int = 0
    var current: Sentence = sentences[Random().nextInt(sentences.lastIndex)]

    init {
        sentences.shuffle()
    }

    fun next() {
        count++
        if (count >= sentences.size)
            count = 0
        current = sentences[count]
        //AnswerOption().forget()
    }
}

class Sentence(val text: String, sentenceNo: String) {
    val sentenceNo = sentenceNo
}

const val maxRounds = 12
var rounds = 0

val sentences = mutableListOf(
        Sentence("Tiffany felt lazy. She grabbed a book. She flopped on the couch. She read.", sentenceNo="A"),
        Sentence("Feeling lazy, tiffany grabbed a book and flopped on the couch.", sentenceNo="B"),
        Sentence("I went to the bar. Grabbed a bottle. Poured myself a glass. ", sentenceNo="C"),
        Sentence("At the bar, I poured myself some wine from a bottle.", sentenceNo="D"),
        Sentence("I heard the wheels squealing as I saw her coming down the street with the stroller. ", sentenceNo="E"),
        Sentence("I heard the wheels squeal. I saw the stroller. She turned down my street. ", sentenceNo="F"),
        Sentence("I like reading more than Diane", sentenceNo="G"),
        Sentence("I like reading more than Diane does", sentenceNo="H"),
        Sentence("Let’s go!", sentenceNo="I"),
        Sentence("Let’s go now because the shows will be shutting down in half an hour!", sentenceNo="J"),
        Sentence("As the number one car slammed its brakes around the turn, my foot hit the gas, and I swung around him, crossing the finish line and winning the race.", sentenceNo="K"),
        Sentence("The number one car slammed its brakes around the turn. My foot hit the gas, and I swung around him. I crossed the finish line, winning the race.", sentenceNo="L")
)


// Test B2: Long and short sentences.
val B : State = state(parent = InteractionTest){
    onEntry {
        rounds = 0
        SentenceSet.next()
        goto(SaySentence)
    }
}

val NewSentence : State = state(parent = InteractionTest){
    onEntry {
        SentenceSet.next()
        goto(SaySentence)
    }
}

val SaySentence : State = state(parent = InteractionTest){
    onEntry {
        furhat.say(SentenceSet.current.text + " ")
        delay(1000)
        furhat.say("Please fill in section, ${SentenceSet.current.sentenceNo}.")
        furhat.ask("When you're done, say yes.", timeout = 200000)
    }

    onResponse<Yes> {
        furhat.say("Perfect!")
        if (++rounds >= maxRounds) {
            furhat.say("That was the last sentence. Thank you!")
            delay(6000)
            goto(ChooseTest)
        } else {
            furhat.say("Here's the next sentence.")
            goto(NewSentence)
        }
    }

    onNoResponse { // Catches silence
        furhat.say("I'm sorry, I did not understand that")
        furhat.say("Please fill in section, ${SentenceSet.current.sentenceNo}.")
        furhat.ask("When you're done, say yes.", timeout = 200000)
    }
}




// Test C: furhat with chat or furhat with face. C is followed by D in the code.
// Make sure you are ready when the participant has filled out the form to switch interface.
val C = state(InteractionTest) {
    onEntry {
        furhat.say("What will you do later today?")
        furhat.listen()
    }

    onResponse {
        furhat.say("That sounds nice. What else will you do?")
        goto(CC)
    }
}

val CC = state(InteractionTest) {
    onEntry {
        furhat.listen()
    }

    onResponse {
        furhat.say("That sounds interesting too. What else will you do?")
        goto(CCC)
    }
}

val CCC = state(InteractionTest) {
    onEntry {
        furhat.listen()
    }

    onResponse {
        furhat.say("Cool. Thank you for a lovely conversation.")
        goto(CCCC)
    }
}

val CCCC = state(InteractionTest){
    onEntry {
        furhat.ask("You can go ahead and fill out the form now. When you are done, just say: Yes, " +
                "and we will continue with the last part of C.", timeout = 120000)
    }

    onResponse<Yes> {
        goto(D)
    }

    onResponse {// catches answers that are not "Yes"
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for the interaction, now. When you are done say: Yes. ", timeout = 120000)
    }

    onNoResponse { // Catches silence
        furhat.ask("I'm sorry, I did not understand that. You can fill in the form for the interaction, now. When you are done say: Yes. ", timeout = 120000)
    }
}
// D is the second part of test C, if C is with the furhat face, then D is only with the chat.
// D is followed directly after C.
val D = state(InteractionTest) {
    onEntry {
        furhat.say("What will you do later today?")
        furhat.listen()
    }

    onResponse {
        furhat.say("That sounds nice. What else will you do?")
        goto(DD)
    }
}

val DD = state(InteractionTest) {
    onEntry {
        furhat.listen()
    }

    onResponse {
        furhat.say("That sounds interesting too. What else will you do?")
        goto(DDD)
    }
}

val DDD = state(InteractionTest) {
    onEntry {
        furhat.listen()
    }

    onResponse {
        furhat.say("Cool. Thank you for a lovely conversation.")
        furhat.say("You can go ahead and fill out the form now. Then you are done with test C.")
        delay(6000)
        goto(IdleTest)
    }
}