package io.github.architpanigrahi.springbootdsl.feature

class FeatureRegistry(
    private val onFeatureSelected: (SpringFeature) -> Unit,
    private val onFeaturesChanged: (SpringFeature, Set<SpringFeature>) -> Unit,
) {
    private val selectedFeatures = linkedSetOf<SpringFeature>()

    fun select(feature: SpringFeature) {
        val isNewFeature = selectedFeatures.add(feature)

        if (!isNewFeature) {
            return
        }

        onFeatureSelected(feature)
        onFeaturesChanged(feature, selectedFeatures.toSet())
    }

    fun selectAll(vararg features: SpringFeature) {
        features.forEach(::select)
    }

    fun isSelected(feature: SpringFeature): Boolean {
        return feature in selectedFeatures
    }

    fun selectedFeatures(): Set<SpringFeature> {
        return selectedFeatures.toSet()
    }
}
