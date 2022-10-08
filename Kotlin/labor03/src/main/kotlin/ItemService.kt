class ItemService(private var itemRepository: ItemRepository) {
    constructor() : this(ItemRepository())

    fun selectRandomItems(count: Int): List<Item> {
        val items = mutableListOf<Item>()
        var counter = count

        if(count > itemRepository.size() || count < 0) {
            counter = itemRepository.size()
        }

        while(items.size < counter) {
            val item = itemRepository.randomItem()
            if (!items.contains(item)) {
                items.add(item)
            }
        }

        return items
    }
}