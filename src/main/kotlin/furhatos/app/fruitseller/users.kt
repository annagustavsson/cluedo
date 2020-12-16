package furhatos.app.fruitseller

import furhatos.app.fruitseller.nlu.NameList
import furhatos.records.User

class NameData (
        var names : NameList = NameList()
        //, var alreadyExecuted : Boolean = false

)

val User.order : NameData
    get() = data.getOrPut(NameData::class.qualifiedName, NameData())