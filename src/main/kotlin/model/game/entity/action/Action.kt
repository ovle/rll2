package model.game.entity.action

import model.game.entity.traits.ActionOwner
import model.game.entity.traits.ActionTarget
import view.*

enum class ActionType() {
    SlashHit,
    StabHit,
    Shoot,
    Block,
    Invoke,
    Die, //todo test
    Common;
}

//todo remove
fun actionName(actionType: ActionType): String {
    return when (actionType) {
        ActionType.SlashHit -> SLASH_HIT_ANIMATION_NAME
        ActionType.StabHit -> STAB_HIT_ANIMATION_NAME
        ActionType.Block -> BLOCK_ANIMATION_NAME
        ActionType.Invoke -> INVOKE_ANIMATION_NAME
        ActionType.Common -> COMMON_ANIMATION_NAME
        ActionType.Die -> DIE_ANIMATION_NAME
        else -> throw UnsupportedOperationException("not supported action ${actionType}")
    }
}

class Action(val type: ActionType, val targets: Collection<ActionTarget> = listOf()) {

}