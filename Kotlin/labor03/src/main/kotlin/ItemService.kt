class ItemService(private var itemRepository: ItemRepository) {
    constructor() : this(ItemRepository())

    // select a given number of items from the repository
    fun selectRandomItems(count: Int): List<Item> {
        val items = mutableListOf<Item>()
        var counter = count

        // if the given number is greater than the number of items in the repository or if its negative, return all items
        if(count > itemRepository.size() || count < 0) {
            counter = itemRepository.size()
        }

        while(items.size < counter) {
            val item = itemRepository.randomItem()

            // make sure we don't select the same item twice
            if (!items.contains(item)) {
                items.add(item)
            }
        }

        return items
    }
}