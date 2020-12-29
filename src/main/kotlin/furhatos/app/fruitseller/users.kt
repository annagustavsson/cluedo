package furhatos.app.fruitseller

import furhatos.app.fruitseller.nlu.NameList
import furhatos.app.fruitseller.nlu.Suspects
import furhatos.records.User

class NameData (
    var names : NameList = NameList()
)

val User.order : NameData
    get() = data.getOrPut(NameData::class.qualifiedName, NameData())

val User.suspect1 : Suspects
        get() = Suspects(
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
        "Oh, dear. I don’t know. Albert didn't have any enemies. As far as I know, no one knew " +
                "about his money problems. Then again, he. isn’t very good at keeping quiet. I mean, " +
                "I’ve had to shut him up countless times when he’s been close to telling people about " +
                "Harold’s forged degree. Harold has been very upset with him at times… Perhaps, Harold " +
                "had something to do with it. ",
        "See, this is the funny thing. I don’t remember using any [CLEANING PRODUCT] to clean " +
                "that day. ",
        0,
        0,
        0,
        0,
        0
)

val User.suspect2 : Suspects
    get() = Suspects(
    "Harold",
    "Hoffman",
    "Chemistry professor",
    "colleague",
    false,
    "Jeremy",
    "Brian",
    "Once I was done with all my classes at school I went by the liquor store, I had " +
    "promised Albert I would help him with some purchases, he has been struggling a bit " +
    "with money for some time now. So, I bought some stuff for the party and then I went " +
    "home. I got dressed after a bite of food and then I went next door, to the party. There " +
    "I was greeted by Albert and Carol. We talked a bit and then I went to the kitchen and " +
    "made some drinks for myself and some others. Then I saw Francis and talked a little. " +
    "Then I chatted to a few other people and then we found out what had happened. ",
    "I was making another cocktail by myself in the " +
    "kitchen. A martini, in fact. I am very careful about making my drinks, so it took " +
    "quite some time. ",
    "Well, as I have told you about my evening, you would know that I chatted with some " +
    "people at the party before that terrible thing happened. And after we all found out " +
    "about the murder, I walked home and went to bed. ",
    "Hmm… well, Francis and Albert used to play a lot of Poker together. Until Francis " +
    "owed Albert a lot of money… That could be something… ",
    "Carol Clark was a bit more uptight than usual, she was running around being very busy. " +
    "But Francis Franklin was acting a bit weird too, maybe he had breathed in too much " +
    "vapor from all his cleaning products. ",
    0,
    0,
    0,
    0,
    0
)

val User.suspect3 : Suspects
    get() = Suspects(
            "Francis",
            "Franklin",
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
                    "murder. That didn't change, I stayed in the kitchen and enjoyed my wine.",
            "Since Albert got poisoned it must be Professor Harold Hoffman. He’s the only one that " +
                    "knows chemistry and can create such dangerous chemical substances.",
            "Hmm... Carol Clark flirted with me the whole evening. She was trying to get eye " +
                    "contact with me, and it was obvious that she wanted to make me laugh at her jokes. She " +
                    "didn't seem to care about her husband Albert Adams at all.",
            0,
            0,
            0,
            0,
            0
    )