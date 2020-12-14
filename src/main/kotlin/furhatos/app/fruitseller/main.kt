package furhatos.app.fruitseller

import furhatos.app.fruitseller.flow.*
import furhatos.skills.Skill
import furhatos.flow.kotlin.*

class FruitsellerSkill : Skill() {
    override fun start() {
        //--- Cluedo game ---
        // Flow().run(Idle)
        //--- User test ---
        Flow().run(IdleTest)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
