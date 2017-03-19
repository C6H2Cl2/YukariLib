package c6h2cl2.YukariLib

/**
 * @author C6H2Cl2
 */
class PlayerNotOfficialPurchasedException : RuntimeException {
    companion object {
        const val template_message = "You must buy Minecraft from Official Site!!!"
    }

    constructor() : super(template_message)
    constructor(message: String) : super(message)
    constructor(exception: Throwable) : super(template_message, exception)
    constructor(message: String, exception: Throwable) : super(message, exception)
}