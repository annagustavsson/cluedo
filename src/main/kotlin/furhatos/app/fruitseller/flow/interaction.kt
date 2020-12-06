package furhatos.app.fruitseller.flow

import furhatos.app.fruitseller.nlu.*
import furhatos.app.fruitseller.order
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.*

val Start = state(Interaction){

    onEntry {
        furhat.ask("Thank god your here detective! May I ask your name?")
    }

    onResponse{
        val username = it.text
        // The name of the person playing
        // TODO: Save username variable at a better place. In GamePlay-class? In users.kt?
        furhat.gesture(Gestures.BigSmile, async = true)
        // async = true means that the gesture does not block the following speech
        furhat.say("Ah! Detective  $username! There has been a murder!")
        furhat.gesture(Gestures.ExpressFear, async = true)
        furhat.say("And we need your help to solve it. " +
                "The victim is the city millionaire, Albert Adams. He was found dead in his library. " +
                "The suspects are his wife Carol, the chemistry professor Harold and his childhood friend Francis. " +
                "They are all here ready to be questioned by you $username. ")
        goto(TakingOrder)
    }
}

val Options = state(Interaction) {
    // If the user requests to hear the options again.

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
        }else if(users.current.order.names.list.size == 3) {
            goto(GamePlay().guessMurder())
        } else{
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
        // TODO: The creation of the suspects objects should probably be created somewhere else. And only called on here:
        if(names.text=="Carol") {
            goto(Suspects(
                    "Carol",
                    "Clark",
                    "Construction worker",
                    "wife",
                    false,
                    "Ursula",
                    "Amy",
                    "Me and Albert had dinner around six, then we Then, as the hostess of the party I was " +
                            "busy running around, making sure that all the guests were content and that their " +
                            "glasses were never empty. You have no idea how much cleaning one has to do before a " +
                            "party like this.",
                    "I was with Harold in the kitchen. ",
                    "I was accompanying Harold who was all alone in the kitchen, but then I saw that " +
                            "there was a bottle of [CLEANING PRODUCT]  laying in the corner of the room. I thought " +
                            "it was very odd, and embarrassing! What kind of hostess leaves a bottle of some " +
                            "[CLEANING PRODUCT]  laying around like that? So I went to put it away, and that’s when " +
                            "I found Albert… ",
                    "Oh, dear. I don’t know. Albert didn’t have any enemies. As far as I know, no one knew " +
                            "about his money problems. Then again, he. isn’t very good at keeping quiet. I mean, " +
                            "I’ve had to shut him up countless times when he’s been close to telling people about " +
                            "Harold’s forged degree. Harold has been very upset with him at times… Perhaps, Harold " +
                            "had something to do with it. ",
                    "See, this is the funny thing. I don’t remember using any [CLEANING PRODUCT] to clean " +
                            "that day. "

                ).initialConversation)

        }else if(names.text=="Harold"){
            goto(Suspects(
                    "Harold",
                    "Hoffman",
                    "Chemistry professor",
                    "colleague",
                    false,
                    "Geremy",
                    "Brian",
                    "Once I was done with all my classes at school I went by the liquor store, I had " +
                            "promised Albert I would help him with some purchases, he has been struggling a bit " +
                            "with money for some time now. So, I bought some stuff for the party and then I went " +
                            "home. I got dressed after a bite of food and then I went next door, to the party. There " +
                            "I was greeted by Albert and Carol. We talked a bit and then I went to the kitchen and " +
                            "made some drinks for myself and some others. Then I saw Francis and talked a little. " +
                            "Then I chatted to a few other people and then we found out what had happened. ",
                    "At exactly what time… nevermind. I was making another cocktail by myself in the " +
                            "kitchen. A martini, in fact. I am very careful about making my drinks, so it took " +
                            "quite some time. ",
                    "Well, as I have told you about my evening, you would know that I chatted with some " +
                            "people at the party before that terrible thing happened. And after we all found out " +
                            "about the murder, I walked home and went to bed. ",
                    "Hmmm… well, Francis and Albert used to play a lot of Poker together. Until Francis " +
                            "owed Albert a lot of money… That could be something… ",
                    "Carol Clark was a bit more uptight than usual, she was running around being very busy. " +
                            "But Francis Franklin was acting a bit weird too, maybe he had breathed in too much " +
                            "vapor from all his cleaning products. "

            ).initialConversation)
        }else{
            goto(Suspects(
                    "Francis",
                    "Franclin",
                    "Cleaner",
                    "childhood friend",
                    true,
                    "Ted",
                    "Emma",
                    "I worked my 9 to 5 shift as usual. Then I went home, took a shower, " +
                            "got ready for the night, and drove out to Albert Adams and Carol Clark’s " +
                            "house. Then I was greeted by Carol, who offered me a cold beverage and winked " +
                            "at me. Very sweet of her, but a bit strange since she got a Husband. After that, " +
                            "I chatted around and talked to pretty much all of the guests. Albert and I had a " +
                            "lovely talk about golf…. I just can’t believe he’s gone.",
                    "What a stupid question, I was in the kitchen like everyone else. If I remember " +
                            "right I saw the Professor making a drink. ",
                    "Silly, silly, silly. I already told you that I was in the kitchen during the " +
                            "murder. That didn’t change, I stayed in the kitchen and enjoyed my wine.",
                    "Since Albert got poisoned it must be Professor Harold Hoffman. He’s the only one that " +
                            "knows chemistry and can create such dangerous chemical substances.",
                    "Hmm... Carol Clark flirted with me the whole evening. She was trying to get eye " +
                            "contact with me, and it was obvious that she wanted to make me laugh at her jokes. She " +
                            "didn’t seem to care about her husband Albert Adams at all."

            ).initialConversation)
        }
        // TODO: Fix voices to the different suspects (Currently they all have language: English_GB,
        //  also Francis is a girl and Harold has the same voice as Furhat.)
    }

    onReentry {
        furhat.ask("Did you want something else?")
    }

    onResponse<No> {
        furhat.say("You have so far spoken to ${users.current.order.names}. Have a great day!")
    }
}