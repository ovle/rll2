package model.game.entity.item

import model.game.template.ItemTemplate

class Inventory(val items: MutableCollection<Item> = mutableListOf()) {
}

class Item(val template: ItemTemplate) {
}