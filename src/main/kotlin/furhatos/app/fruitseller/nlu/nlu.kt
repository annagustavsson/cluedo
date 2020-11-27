package furhatos.app.fruitseller.nlu

import furhatos.nlu.ComplexEnumEntity
import furhatos.nlu.EnumEntity
import furhatos.nlu.Intent
import furhatos.nlu.ListEntity
import furhatos.util.Language
import furhatos.nlu.common.Number


class Room : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Kitchen", "Ballroom", "Library", "Cellar")
    }
}


class ChooseRoom(var rooms : RoomList? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        //return listOf("@fruits", "I want @fruits", "I would like @fruits", "I want to buy @fruits")
        return listOf("@rooms", "I wanna go to @rooms", "I would like to go to @rooms", "I wanna enter @rooms")
    }
}


class RequestOptions: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("What rooms do you have?",
                "Which rooms are available?",
                "What are the alternatives?",
                "What do you have?",
                "What are the rooms?",
                "Which are the rooms?")
    }
}

class RoomList : ListEntity<QuantifiedRooms>()

class QuantifiedRooms(
        val count : Number? = Number(1),
        //val fruit : Fruit? = null) : ComplexEnumEntity() {
        val room : Room? = null) : ComplexEnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        //return listOf("@count @fruit", "@fruit")
        return listOf("@rooms, @count @rooms")
    }

    override fun toText(): String {
        //return generate("$count $fruit")
        return generate("$count $room")
    }
}

