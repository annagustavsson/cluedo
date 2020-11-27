package furhatos.app.fruitseller.flow

import furhatos.records.User
import furhatos.app.fruitseller.nlu.RoomList


class RoomData (
        var rooms : RoomList = RoomList()
)

val User.order : RoomData
    get() = data.getOrPut(RoomData::class.qualifiedName, RoomData())

