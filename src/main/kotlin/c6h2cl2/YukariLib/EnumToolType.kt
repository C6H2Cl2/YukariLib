package c6h2cl2.YukariLib

/**
 * @author C6H2Cl2
 */
enum class EnumToolType(recipe1: String, recipe2: String, recipe3: String) {
    PICKAXE(" R ", " R ", "HHH"), AXE(" R ", "HR ", "HH "), SHOVEL(" R ", " R ", " H "), HOE(" R ", " R ", "HH "), SWORD("  H", "RH ", "HR ");

    val recipe = arrayListOf(recipe1, recipe2, recipe3)
}